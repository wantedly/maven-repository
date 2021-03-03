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

@Suppress("LocalVariableName")
signing {
    // https://docs.gradle.org/current/userguide/signing_plugin.html#using_in_memory_ascii_armored_openpgp_subkeys
    // See GitHub's secrets
    // Named with UPPER_CASE because GitHub's secret names are not case-sensitive.
    val SIGNING_KEY_ID: String? by project
    val SIGNING_KEY: String? by project
    val SIGNING_PASSWORD: String? by project
    @Suppress("UnstableApiUsage")
    useInMemoryPgpKeys(SIGNING_KEY_ID, SIGNING_KEY, SIGNING_PASSWORD)
    sign(publishing.publications["maven"])
}
