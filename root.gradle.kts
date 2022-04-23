plugins {
    val kotlinVersion: String by System.getProperties()
    kotlin("jvm") version kotlinVersion apply false
    id("net.kyori.blossom") version "1.3.0" apply false
    id("com.github.johnrengelman.shadow") version "7.1.2" apply false
    id("gg.essential.multi-version.root")
}

preprocess {
    "1.18.2-fabric"(11802, "yarn") {

    }
}
