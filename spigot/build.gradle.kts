import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    java
    kotlin("jvm") version "1.9.22"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
}

group = "com.cyprias.chunkspawnerlimiter"
version = "4.3.3"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.codemc.org/repository/maven-public")
    maven("https://repo.aikar.co/content/groups/aikar/")
    maven("https://repo.alessiodp.com/releases/")
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
    compileOnly(libs.spigot.api)
    compileOnly(libs.kotlin.stdlib)
    compileOnly(libs.bstats)
    compileOnly(libs.acf)
    implementation(libs.libby.bukkit) // Has to be shaded

    testImplementation(libs.kotlin.test)
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

tasks {
    build {
        dependsOn(shadowJar)
    }

    shadowJar {
        minimize()
        archiveBaseName.set("ChunkSpawnerLimiter")
        archiveClassifier.set("")
        relocate("com.alessiodp.libby", "com.cyprias.chunkspawnerlimiter.libs")
    }


}

bukkit {
    main = "com.cyprias.chunkspawnerlimiter.ChunkSpawnerLimiter"
    name = "ChunkSpawnerLimiter"
    author = "Cyprias"
    authors = listOf("sarhatabaot")
    apiVersion = "1.14"
    prefix = "CSL"
    description = "Limit entities in chunks."
    website = "https://github.com/sarhatabaot/ChunkSpawnerLimiter"
    version = project.version.toString()
    load = BukkitPluginDescription.PluginLoadOrder.POSTWORLD
}