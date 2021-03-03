plugins {
    `kotlin-dsl`
    kotlin("jvm") version embeddedKotlinVersion
    `maven-publish`
    signing
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

val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

publishing {
    publications {
        register<MavenPublication>("maven") {
            from(components["java"])
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
