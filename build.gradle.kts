plugins {
    `kotlin-dsl`
    id("org.jetbrains.kotlin.jvm") version "1.3.70"
    `maven-publish`
}

group = "com.wantedly"
version = "0.0.1"

repositories {
    jcenter()
}

dependencies {
    compileOnly(gradleApi())
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation(kotlin("gradle-plugin"))
    implementation(kotlin("stdlib-jdk8"))
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
}
