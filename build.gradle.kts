plugins {
    `kotlin-dsl`
    id("org.jetbrains.kotlin.jvm") version "1.3.70"
    id("com.jfrog.bintray") version "1.8.4"
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

publishing {
    publications {
        register<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}

bintray {
    user = System.getenv("BINTRAY_USER")
    key = System.getenv("BINTRAY_KEY")
    setPublications("maven")
    pkg = PackageConfig().apply {
        repo = "maven"
        name = "${project.group}:${project.name}"
        setLicenses("Apache-2.0")
        vcsUrl = "https://github.com/wantedly/maven-repository.git"
        version = VersionConfig().apply {
            name = "${project.version}"
        }
    }
}
