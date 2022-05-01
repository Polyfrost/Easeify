pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://maven.fabricmc.net")
        maven("https://maven.architectury.dev/")
        maven("https://maven.minecraftforge.net")
        maven("https://repo.essential.gg/repository/maven-public")
        maven("https://server.bbkr.space/artifactory/libs-release/")
        maven("https://maven.quiltmc.org/repository/release")
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "io.github.juuxel.loom-quiltflower-mini") {
                useModule("com.github.wyvest:loom-quiltflower-mini:${requested.version}")
            }
        }
    }
    plugins {
        val egtVersion = "0.1.4"
        id("gg.essential.multi-version.root") version egtVersion
    }
}

val mod_name: String by settings

rootProject.name = mod_name
rootProject.buildFileName = "root.gradle.kts"

listOf(
    "1.18.2-fabric"
).forEach { version ->
    include(":$version")
    project(":$version").apply {
        projectDir = file("versions/$version")
        buildFileName = "../../build.gradle.kts"
    }

}
