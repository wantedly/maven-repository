plugins {
    `java-gradle-plugin`
    id("org.jetbrains.kotlin.jvm") version "1.3.70"
}

repositories {
    jcenter()
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation(kotlin("gradle-plugin"))
    implementation(kotlin("stdlib-jdk8"))
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
}

gradlePlugin {
    val mavenRepository by plugins.creating {
        id = "com.wantedly.maven.repository"
        implementationClass = "com.wantedly.maven.repository.MavenRepositoryPlugin"
    }
}
