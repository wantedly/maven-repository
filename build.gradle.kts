plugins {
    `kotlin-dsl`
    id("org.jetbrains.kotlin.jvm") version "1.3.71"
    id("com.jfrog.bintray") version "1.8.4"
    `maven-publish`
}

group = "com.wantedly"
version = "0.0.2"

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
