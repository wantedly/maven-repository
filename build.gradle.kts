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
    jcenter()
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
    repositories {
        maven {
            val releasesRepoUrl = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
            val snapshotsRepoUrl = "https://s01.oss.sonatype.org/content/repositories/snapshots/"
            url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)
            credentials {
                val ossrhUsername: String? by project
                val ossrhPassword: String? by project
                username = ossrhUsername
                password = ossrhPassword
            }
        }
    }
    publications {
        register<MavenPublication>("maven") {
            from(components["java"])
            artifact(javadocJar.get())
            artifact(sourcesJar.get())
            pom {
                description.set("Wantedly Maven Repository")
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
                            name.set("wantedly-dev")
                            email.set("dev@wantedly.com")
                            organization.set("Wantedly, Inc.")
                            organizationUrl.set("https://github.com/wantedly")
                        }
                    }
                    scm {
                        connection.set("scm:git:git://github.com/wantedly/maven-repository.git")
                        developerConnection.set("scm:git:ssh://github.com:wantedly/maven-repository.git")
                        url.set("https://github.com/wantedly/maven-repository.git")
                    }
                }
            }
        }
    }
}

signing {
    // https://docs.gradle.org/current/userguide/signing_plugin.html#using_in_memory_ascii_armored_openpgp_subkeys
    val signingKeyId: String? by project
    val signingKey: String? by project
    val signingPassword: String? by project
    @Suppress("UnstableApiUsage")
    useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
    sign(publishing.publications["maven"])
}
