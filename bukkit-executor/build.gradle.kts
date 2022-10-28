plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    `maven-publish`
}

group = "com.newestaf"
version = "1.0.0"

repositories {
    mavenCentral()
}

gradlePlugin{
    plugins {
        create("bukkit-executor") {
            id = "com.newestaf.bukkit-executor"
            implementationClass = "com.newestaf.BukkitExecutorPlugin"
        }
    }
}
