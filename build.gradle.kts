plugins {
    `java-library`
}

group = "github.phateio.preloginevent"
version = "1.0"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    // spigot-api 1.15.1 transitively depends on net.md-5:bungeecord-chat:1.15-SNAPSHOT,
    // which is no longer hosted on the spigotmc nexus. Scarsz mirrors it.
    maven("https://nexus.scarsz.me/content/groups/public/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.15.1-R0.1-SNAPSHOT")
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
