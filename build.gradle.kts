plugins {
    id("java")
}

allprojects {
    apply(plugin = "java")
    
    group = property("mod_group_id").toString()
    version = property("mod_version").toString()
    
    repositories {
        mavenCentral()
        maven("https://maven.fabricmc.net/")
        maven("https://maven.neoforged.net/releases/")
        maven("https://maven.parchmentmc.org")
    }
    
    val javaVersionNum: Int = try {
        property("java_version").toString().toInt()
    } catch (e: Exception) {
        21
    }
    
    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release.set(javaVersionNum)
    }
    
    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(javaVersionNum))
        withSourcesJar()
    }
}
