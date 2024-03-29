plugins {
    id("fabric-loom")
    id("com.diffplug.spotless") version "6.12.0"
}

version = project.extra["mod_version"] as String
group = "dog.kaylen"

dependencies {
    minecraft("com.mojang", "minecraft", project.extra["minecraft_version"] as String)
    mappings("net.fabricmc", "yarn", project.extra["yarn_mappings"] as String, null, "v2")
    modImplementation("net.fabricmc", "fabric-loader", project.extra["loader_version"] as String)
    // fabric api
    modImplementation("net.fabricmc.fabric-api", "fabric-api", project.extra["fabric_version"] as String)
}

tasks {
    val javaVersion = JavaVersion.VERSION_17

    compileJava {
        options.encoding = "UTF-8"
        sourceCompatibility = javaVersion.toString()
        targetCompatibility = javaVersion.toString()
        options.release.set(javaVersion.toString().toInt())
    }

    jar { from("LICENSE") }

    processResources {
        filesMatching("fabric.mod.json") {
            expand(
                mutableMapOf(
                    "version" to project.extra["mod_version"] as String,
                    "fabricloader" to project.extra["loader_version"] as String,
                    "fabric_api" to project.extra["fabric_version"] as String,
                    "minecraft" to project.extra["minecraft_version"] as String
                )
            )
        }
        filesMatching("*.mixins.json") {
            expand(mutableMapOf("java" to project.extra["java_version"] as String))
        }
    }
}

spotless {
    ratchetFrom("origin/main")
    encoding("UTF-8")

    java {
        googleJavaFormat().aosp().reflowLongStrings()
        licenseHeaderFile(project.file("LICENSE_HEADER"))
    }
    kotlinGradle {
        ktlint()
    }
    json {
        target("src/**/*.json")
        // ignore fabric.mod.json
        targetExclude("fabric.mod.json")
        simple().indentWithSpaces(4)
    }
}
