import gg.essential.gradle.util.*

plugins {
    kotlin("jvm")
    id("gg.essential.multi-version")
    id("gg.essential.defaults.repo")
    id("gg.essential.defaults.java")
    id("gg.essential.defaults.loom")
    id("com.github.johnrengelman.shadow")
    id("net.kyori.blossom")
    id("io.github.juuxel.loom-quiltflower")
}

val mod_name: String by project
val mod_version: String by project
val mod_id: String by project

blossom {
    replaceToken("@VER@", mod_version)
    replaceToken("@NAME@", mod_name)
    replaceToken("@ID@", mod_id)
}

version = mod_version
group = "cc.woverflow"
base {
    archivesName.set("$mod_name-$platform")
}

quiltflower {
    addToRuntimeClasspath.set(true)
}

tasks.compileKotlin.setJvmDefault(if (platform.mcVersion >= 11400) "all" else "all-compatibility")
loom.noServerRunConfigs()
loom {
    if (project.platform.isForge) {
        forge {
            mixinConfig("${mod_id}.mixins.json")
        }
    }
    launchConfigs {
        named("client") {
            property("mixin.debug.export", "true")
            property("mixin.dumpTargetOnFailure", "true")
        }
    }
    runConfigs {
        named("client") {
            ideConfigGenerated(true)
        }
    }
    mixin.defaultRefmapName.set("${mod_id}.mixins.refmap.json")
    accessWidenerPath.set(rootProject.file("src/main/resources/easeify.accesswidener"))
}

repositories {
    maven("https://repo.polyfrost.cc")
    maven("https://maven.isxander.dev/releases")
    maven("https://maven.shedaniel.me/")
    maven("https://maven.terraformersmc.com/releases")
    maven("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1")
}

val includeTransitive: Configuration by configurations.creating

fun DependencyHandlerScope.includeTransitive(
    root: ResolvedDependency?,
    dependencies: Set<ResolvedDependency>,
    fabricLanguageKotlinDependency: ResolvedDependency,
    checkedDependencies: MutableSet<ResolvedDependency> = HashSet()
) {
    dependencies.forEach {
        if (checkedDependencies.contains(it) || (it.moduleGroup == "org.jetbrains.kotlin" && it.moduleName.startsWith("kotlin-stdlib")) || (it.moduleGroup == "org.slf4j" && it.moduleName == "slf4j-api"))
            return@forEach

        if (fabricLanguageKotlinDependency.children.any { kotlinDep -> kotlinDep.name == it.name }) {
            println("Skipping -> ${it.name} (already in fabric-language-kotlin)")
        } else {
            include(it.name)
            println("Including -> ${it.name} from ${root?.name}")
        }
        checkedDependencies += it

        includeTransitive(root ?: it, it.children, fabricLanguageKotlinDependency, checkedDependencies)
    }
}

fun DependencyHandlerScope.handleIncludes(project: Project, configuration: Configuration) {
    includeTransitive(
        null,
        configuration.resolvedConfiguration.firstLevelModuleDependencies,
        project.configurations.getByName("modImplementation").resolvedConfiguration.firstLevelModuleDependencies
            .first { it.moduleGroup == "net.fabricmc" && it.moduleName == "fabric-language-kotlin" }
    )
}

dependencies {
    val kotlinVersion: String by System.getProperties()
    val fabricVersion: String by project
    val fabricKotlinVersion: String by project

    implementation(kotlin("stdlib-jdk8", kotlinVersion))
    "com.github.llamalad7:mixinextras:0.0.12".let {
        implementation(it)
        annotationProcessor(it)
        include(it)
    }

    modRuntimeOnly("me.djtheredstoner:DevAuth-${when (platform.loader) {
        gg.essential.gradle.multiversion.Platform.Loader.Fabric -> "fabric"
        gg.essential.gradle.multiversion.Platform.Loader.Forge -> "forge-latest"
    }
    }:1.0.0")

    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricVersion")
    modImplementation("net.fabricmc:fabric-language-kotlin:$fabricKotlinVersion+kotlin.$kotlinVersion")
    modImplementation("com.terraformersmc:modmenu:${if (platform.mcVersion >= 11900) "4.+" else "3.+"}")
    includeTransitive(modImplementation("gg.essential:vigilance-1.18.1-${platform.loaderStr}:258") {
        exclude(module = "kotlin-reflect")
        exclude(module = "kotlin-stdlib-jdk8")
        exclude(group = "net.fabricmc")
        exclude(module = "elementa-1.18.1-fabric")
        exclude(module = "universalcraft-1.18.1-fabric")
    })
    includeTransitive(modImplementation("gg.essential:elementa-1.18.1-${platform.loaderStr}:544") {
        exclude(module = "kotlin-reflect")
        exclude(module = "kotlin-stdlib-jdk8")
        exclude(group = "net.fabricmc")
        exclude(module = "universalcraft-1.18.1-fabric")
    })
    val ucVersion = if (platform.mcVersion == 11802) "1.18.1" else platform.mcVersionStr
    includeTransitive(modImplementation("gg.essential:universalcraft-$ucVersion-${platform.loaderStr}:236") {
        exclude(module = "kotlin-reflect")
        exclude(module = "kotlin-stdlib-jdk8")
        exclude(group = "net.fabricmc")
    })
    handleIncludes(project, includeTransitive)
}

kotlin {
    jvmToolchain {
        (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.processResources {
    inputs.property("id", mod_id)
    inputs.property("name", mod_name)
    val java = if (project.platform.mcMinor >= 18) {
        17
    } else {if (project.platform.mcMinor == 17) 16 else 8 }
    val compatLevel = "JAVA_${java}"
    inputs.property("java", java)
    inputs.property("java_level", compatLevel)
    inputs.property("version", mod_version)
    inputs.property("mcVersionStr", project.platform.mcVersionStr)
    filesMatching(listOf("mcmod.info", "${mod_id}.mixins.json", "mods.toml")) {
        expand(mapOf(
            "id" to mod_id,
            "name" to mod_name,
            "java" to java,
            "java_level" to compatLevel,
            "version" to mod_version,
            "mcVersionStr" to project.platform.mcVersionStr
        ))
    }
    filesMatching("fabric.mod.json") {
        expand(mapOf(
            "id" to mod_id,
            "name" to mod_name,
            "java" to java,
            "java_level" to compatLevel,
            "version" to mod_version,
            "mcVersionStr" to project.platform.mcVersionStr.let { if (platform.mcVersion in 11900..11902) it else it.substringBeforeLast(".") + ".x" }
        ))
    }
}

tasks {
    withType(Jar::class.java) {
        exclude("mcmod.info", "mods.toml")
    }
    jar {
        manifest {
            attributes(mapOf(
                "ModSide" to "CLIENT",
                "ForceLoadAsMod" to true
            ))
        }
    }
}
