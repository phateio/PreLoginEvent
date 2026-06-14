plugins {
    `java-library`
}

group = "io.github.phateio"
version = "1.1"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    // Fallback mirror for the net.md-5:bungeecord-chat transitive that the
    // official spigotmc nexus no longer hosts.
    maven("https://nexus.scarsz.me/content/groups/public/")
}

dependencies {
    // Paper API (a superset of Spigot/Bukkit) for PaperServerListPingEvent#getClient().getVirtualHost().
    // 1.15 paper-api is no longer published; 1.16.5 is the oldest still available, and every API used
    // here exists since 1.15, so the plugin still runs on Paper 1.15+.
    compileOnly("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set(8)
}

tasks.processResources {
    // Mirrors the Maven resource filtering of ${name} / ${version} in plugin.yml.
    val props = mapOf(
        "name" to project.name,
        "version" to project.version,
    )
    inputs.properties(props)
    filesMatching("plugin.yml") {
        expand(props)
    }
}
