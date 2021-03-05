plugins {
    `kotlin-dsl`
    kotlin("jvm") version embeddedKotlinVersion
    `maven-publish`
    signing
    id("org.jetbrains.dokka") version "1.4.20"
}

group = "com.wantedly"
version = "1.0.0"

repositories {
    mavenCentral()
    // Workaround: https://github.com/Kotlin/dokka/issues/41
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
            // TODO: Needs migrate to s01.oss.sonatype.org after release completed.
            // > Note: As of February 2021, all new projects began being provisioned on https://s01.oss.sonatype.org/.
            // > If your project is not provisioned on https://s01.oss.sonatype.org/,
            // > please login to the legacy host https://oss.sonatype.org/.
            val releasesRepoUrl = "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
            val snapshotsRepoUrl = "https://oss.sonatype.org/content/repositories/snapshots/"
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
        create<MavenPublication>("maven") {
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

tasks.all {
    // The `kotlin-dsl` plugin automatically generates a `pluginMaven` publishing task to publish Gradle plugins,
    // but since there are no Gradle plugins to publish in this project, the `pluginMaven` publishing task is disabled.
    // https://github.com/gradle/gradle/issues/14993#issuecomment-717883699
    if ("PluginMavenPublication" in name) {
        enabled = false
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
