plugins {
    `kotlin-dsl`
    kotlin("jvm") version embeddedKotlinVersion
    `maven-publish`
    signing
    id("org.jetbrains.dokka") version "1.9.10"
    id("io.github.gradle-nexus.publish-plugin") version "2.0.0"
}

group = "com.wantedly"
version = "1.2.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().all {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

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

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://ossrh-staging-api.central.sonatype.com/service/local/"))
            snapshotRepositoryUrl.set(uri("https://central.sonatype.com/repository/maven-snapshots/"))
        }
    }
}
