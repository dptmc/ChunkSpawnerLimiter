plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "spigot"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            library("spigot-api","org.spigotmc:spigot-api:1.14.4-R0.1-SNAPSHOT")
            library("bstats", "org.bstats:bstats-bukkit:3.0.2")
            library("acf", "co.aikar:acf-paper:0.5.1-SNAPSHOT")
            library("libby-bukkit", "com.alessiodp.libby:libby-bukkit:2.0.0-SNAPSHOT")
            library("kotlin-stdlib", "org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.22")
            library("kotlin-test", "org.jetbrains.kotlin:kotlin-test:1.9.22")
        }
    }
}