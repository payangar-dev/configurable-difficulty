plugins {
    id("java-library")
    id("fabric-loom") version "1.7.2"
}

val minecraftVersion = property("minecraft_version").toString()

dependencies {
    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings(loom.officialMojangMappings())

    compileOnly("dev.isxander:yet-another-config-lib:3.6.1+1.21-fabric")
}
