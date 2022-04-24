import gg.essential.gradle.util.*

plugins {
    kotlin("jvm")
    id("gg.essential.multi-version")
    id("gg.essential.defaults.repo")
    id("gg.essential.defaults.java")
    id("gg.essential.defaults.loom")
    id("com.github.johnrengelman.shadow")
    id("net.kyori.blossom")
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
        }
    }
    mixin.defaultRefmapName.set("${mod_id}.mixins.refmap.json")
}

repositories {
    maven("https://repo.woverflow.cc/")
}

val shade: Configuration by configurations.creating {
    configurations.implementation.get().extendsFrom(this)
}

val shadeMod: Configuration by configurations.creating {
    configurations.modImplementation.get().extendsFrom(this)
}

dependencies {
    val kotlinVersion: String by System.getProperties()
    val fabricVersion: String by project
    val fabricKotlinVersion: String by project

    implementation(kotlin("stdlib-jdk8", kotlinVersion))
    "com.github.llamalad7:mixinextras:0.0.+".let {
        implementation(it)
        annotationProcessor(it)
        include(it)
    }

    modRuntimeOnly("me.djtheredstoner:DevAuth-${when (platform.loader) {
        gg.essential.gradle.multiversion.Platform.Loader.Fabric -> "fabric"
        gg.essential.gradle.multiversion.Platform.Loader.Forge -> "forge-latest"
    }
    }:1.0.0")

    if (platform.isFabric) {
        modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricVersion")
        modImplementation("net.fabricmc:fabric-language-kotlin:$fabricKotlinVersion+kotlin.$kotlinVersion")
        modImplementation("com.terraformersmc:modmenu:3.+")
        shadeMod("gg.essential:vigilance-1.18.1-${platform.loaderStr}:215") {
            exclude(module = "kotlin-reflect")
            exclude(module = "kotlin-stdlib-jdk8")
            exclude(group = "net.fabricmc")
        }
    }
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
            "mcVersionStr" to project.platform.mcVersionStr.substringBeforeLast(".") + ".x"
        ))
    }
}

tasks {
    withType(Jar::class.java) {
        if (project.platform.isFabric) {
            exclude("mcmod.info", "mods.toml")
        } else {
            exclude("fabric.mod.json", "${mod_id}.mixins.json")
            if (project.platform.isLegacyForge) {
                exclude("mods.toml")
            } else {
                exclude("mcmod.info")
            }
        }
    }
    named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
        archiveClassifier.set("dev")
        configurations = listOf(shade, shadeMod)
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        relocate("gg.essential", "cc.woverflow.libs.essential")

        exclude(
            "README.md"
        )
        if (platform.isFabric) {
            exclude("pack.mcmeta", "mods.toml")
        } else {
            exclude("fabric.mod.json")
        }
    }
    remapJar {
        input.set(shadowJar.get().archiveFile)
        archiveClassifier.set("")
    }
    jar {
        manifest {
            attributes(mapOf(
                "ModSide" to "CLIENT",
                "ForceLoadAsMod" to true
            ))
        }
        dependsOn(shadowJar)
        archiveClassifier.set("")
        enabled = false
    }
}
