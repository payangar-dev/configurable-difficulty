plugins {
    id("net.neoforged.gradle.userdev") version "7.0.163"
}

val minecraftVersion: String = property("minecraft_version").toString()
val neoforgeVersion: String = property("neoforgeVersion").toString()
val modId: String = property("mod_id").toString()
val modName: String = property("modName").toString()
val modAuthor: String = property("mod_author").toString()

base {
    archivesName.set("configurable-difficulty-neoforge")
}

dependencies {
    implementation("net.neoforged:neoforge:$neoforgeVersion")
    implementation(project(":common"))
}

tasks.named<ProcessResources>("processResources") {
    val expandProps = mapOf(
        "version" to project.version,
        "mod_id" to modId,
        "mod_name" to modName,
        "mod_author" to modAuthor,
        "minecraft_version" to minecraftVersion
    )
    
    inputs.properties(expandProps)
    
    filesMatching("META-INF/neoforge.mods.toml") {
        expand(expandProps)
    }
}

minecraft {
    accessTransformers {
        file("$rootDir/common/src/main/resources/META-INF/accesstransformer.cfg")
    }
}

runs {
    configureEach {
        systemProperty("forge.logging.console.level", "debug")
        systemProperty("forge.enabledGameTestNamespaces", modId)
    }
}

tasks.jar {
    from(project(":common").sourceSets.main.get().output)
}
