plugins {
    kotlin("jvm") version "2.1.21"
    alias(libs.plugins.kotlin.serialization)


}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(libs.koog.agents)
    implementation(libs.koog.tools)
    implementation(libs.koog.executor.openai.client)
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}