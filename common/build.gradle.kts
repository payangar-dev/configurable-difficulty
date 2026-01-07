plugins {
    id("java-library")
    id("fabric-loom") version "1.7.2"
}

val minecraftVersion = property("minecraft_version").toString()

dependencies {
    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings(loom.officialMojangMappings())
    
    implementation("de.marhali:json5-java:2.0.0")
}
