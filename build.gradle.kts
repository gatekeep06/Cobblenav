plugins {
    id("java")
    id("fabric-loom") version("1.6-SNAPSHOT")
    kotlin("jvm") version ("1.8.20")
}

group = property("maven_group")!!
version = property("mod_version")!!

repositories {
    mavenCentral()
    maven("https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://maven.impactdev.net/repository/development/")
    maven("https://api.modrinth.com/maven")
}

dependencies {
    minecraft("com.mojang:minecraft:${property("minecraft_version")}")
    mappings("net.fabricmc:yarn:${property("yarn_mappings")}")
    modImplementation("net.fabricmc:fabric-loader:${property("loader_version")}")

    // Fabric API
    modImplementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_version")}")

    // Fabric Kotlin
    modImplementation("net.fabricmc:fabric-language-kotlin:${property("fabric_kotlin_version")}")

    // Cobblemon
    modImplementation("com.cobblemon:fabric:${property("cobblemon_version")}")

    // CobblemonTrainers
    modImplementation("maven.modrinth:cobblemontrainers:${property("trainers_version")}")
    modImplementation(files("/run/mods/SelfdotModLibs-2.0.2.jar",
        "run/mods/SelfdotModLibs-2.0.2-sources.jar"))

    modImplementation("maven.modrinth:cobblemon-pokedex:${property("cobbledex_version")}")

    // Cobblemon Counter
    modImplementation("maven.modrinth:cobblemon-counter:${property("cobblemon_counter_version")}")

    // Architectury API
    modImplementation("maven.modrinth:architectury-api:${property("architectury_version")}")

    // Permission API
    modImplementation("me.lucko:fabric-permissions-api:${property("permissions_api_version")}")?.let { include(it) }
}

tasks {
    processResources {
        inputs.property("version", project.version)

        filesMatching("fabric.mod.json") {
            expand(mutableMapOf("version" to project.version))
        }
    }

    jar {
        from("LICENSE")
    }

    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }
}