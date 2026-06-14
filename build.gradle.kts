plugins {
    `java-library`
}

group = "io.github.phateio"
version = "1.1"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    // Paper API for AsyncPlayerPreLoginEvent#getHostname() and PaperServerListPingEvent.
    // Pinned to the latest stable 1.21.x; every API used here is present there and on newer
    // Paper builds, so the plugin runs on Paper 1.21+.
    compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")

    // Legacy MaxMind GeoIP API for reading the .dat databases. Loaded at runtime
    // via plugin.yml `libraries`, so it stays compileOnly here.
    compileOnly("com.maxmind.geoip:geoip-api:1.3.1")
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set(21)
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
