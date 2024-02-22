# Current Problems

1. Kotlin JVM no class def found
2. Duplicate "code" between build.gradle.kts and Libby library loader:
   1. We define the versions and libraries twice here. We should write some code, 
   so we can automatically access these values:

   ### build.gradle.kts
   ```kotlin
   compileOnly(libs.kotlin.stdlib)
   compileOnly(libs.bstats)
   compileOnly(libs.acf)
   ```
   
   ### libby

   ```java
        Library kotlinLibrary = Library.builder()
                .groupId("org.jetbrains.kotlin")
                .artifactId("kotlin-stdlib-jdk8")
                .version("1.9.22")
                .isolatedLoad(true)
                .build();

        Library acfLibrary = Library.builder()
                .groupId("co.aikar")
                .artifactId("acf-paper")
                .version("0.5.1-SNAPSHOT")
                .relocate("co.aikar","com{}cyprias{}chunkspawnerlimiter{}libs")
                .isolatedLoad(true)
                .build();

        Library bstatsLibrary = Library.builder()
                .groupId("org.bstats")
                .artifactId("bstats-bukkit")
                .version("3.0.2")
                .relocate("org{}bstats","com{}cyprias{}chunkspawnerlimiter{}libs")
                .isolatedLoad(true)
                .build();
   ```
   
