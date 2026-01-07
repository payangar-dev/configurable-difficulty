plugins {
    id("fabric-loom") version "1.7.2"
}

val minecraftVersion: String = property("minecraft_version").toString()
val fabricLoaderVersion: String = property("fabricLoaderVersion").toString()
val fabricApiVersion: String = property("fabricApiVersion").toString()
val modId: String = property("mod_id").toString()
val modName: String = property("modName").toString()
val modAuthor: String = property("mod_author").toString()

base {
    archivesName.set("configurable-difficulty-fabric")
}

dependencies {
    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings(loom.officialMojangMappings())

    modImplementation("net.fabricmc:fabric-loader:$fabricLoaderVersion")
    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricApiVersion")

    implementation(project(":common"))

    modImplementation("dev.isxander:yet-another-config-lib:3.6.1+1.21-fabric")
}

tasks.named<ProcessResources>("processResources") {
    val expandProps = mapOf(
        "version" to project.version,
        "mod_id" to modId,
        "mod_name" to modName,
        "mod_author" to modAuthor,
        "minecraft_version" to minecraftVersion,
        "fabric_loader_version" to fabricLoaderVersion
    )
    
    inputs.properties(expandProps)
    
    filesMatching("fabric.mod.json") {
        expand(expandProps)
    }
}

tasks.jar {
    from(project(":common").sourceSets.main.get().output)
}
