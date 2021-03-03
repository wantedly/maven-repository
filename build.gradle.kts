plugins {
    `kotlin-dsl`
    kotlin("jvm") version embeddedKotlinVersion
    `maven-publish`
    signing
    id("org.jetbrains.dokka") version "1.4.20"
}

group = "com.wantedly"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(gradleApi())
    compileOnly(platform(kotlin("bom")))
    compileOnly(kotlin("gradle-plugin"))
}

val dokkaJavadoc by tasks

val javadocJar by tasks.registering(Jar::class) {
    dependsOn(dokkaJavadoc)
    archiveClassifier.set("javadoc")
    from(dokkaJavadoc.outputs)
}

val classes by tasks

val sourcesJar by tasks.registering(Jar::class) {
    dependsOn(classes)
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

publishing {
    publications {
        register<MavenPublication>("maven") {
            from(components["java"])
            artifact(javadocJar.get())
            artifact(sourcesJar.get())
            pom {
                setDescription("Wantedly Maven Repository")
                name.set("wantedly-maven-repository")
                url.set("https://github.com/wantedly/maven-repository")
                licenses {
                    license {
                        name.set("The Apache Software License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        distribution.set("repo")
                    }
                    developers {
                        developer {
                            id.set("wantedly-dev")
                            name.set("wantedly-dev")
                            email.set("dev@wantedly.com")
                        }
                    }
                    scm {
                        url.set("https://github.com/wantedly/maven-repository.git")
                    }
                }
            }
        }
    }
}
