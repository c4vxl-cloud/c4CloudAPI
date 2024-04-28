plugins {
    kotlin("jvm") version "1.9.23"
}

group = "de.c4vxl"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.json:json:20231013")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}