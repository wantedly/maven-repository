plugins {
    `kotlin-dsl`
    kotlin("jvm") version embeddedKotlinVersion
    id("com.jfrog.bintray") version "1.8.5"
    `maven-publish`
}

group = "com.wantedly"
version = "1.0.0-SNAPSHOT"

repositories {
    jcenter()
}

dependencies {
    compileOnly(gradleApi())
    compileOnly(platform(kotlin("bom")))
    compileOnly(kotlin("gradle-plugin"))
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
}

java {
    targetCompatibility = JavaVersion.VERSION_1_8
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

bintray {
    user = System.getenv("BINTRAY_USER")
    key = System.getenv("BINTRAY_KEY")
    setPublications("maven")
    publish = true
    pkg = PackageConfig().apply {
        // TODO(kubode): Needs to transfer ownership to the organization but seems can't do it now.
        // Now requesting the transferring ownership but no response from Bintray support.
//        userOrg = "wantedly"
        repo = "maven"
        name = "${project.group}:${project.name}"
        setLicenses("Apache-2.0")
        vcsUrl = "https://github.com/wantedly/maven-repository.git"
        setVersion(project.version)
    }
}
