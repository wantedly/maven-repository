## wantedly-maven-repository

![Maven Central](https://img.shields.io/maven-central/v/com.wantedly/wantedly-maven-repository)

Make it easier adding a Wantedly's private Maven repository to your Gradle script.

### Setup

```kotlin
import com.wantedly.maven.repository.wantedly

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("com.wantedly:wantedly-maven-repository:${LATEST_VERSION}")
    }
}

repositories {
    // Adds a GitHub Packages Maven repository.
    wantedly(repo = "repo-name")
}

dependencies {
    // You can use the artifacts that have "com.wantedly" groupId!
    implementation("com.wantedly.foo:bar:baz")
}
```

### How to publish to Maven Central

#### GitHub

You can publish this library using `publish` workflow from GitHub Actions.

You must change the artifact version before running `publish` workflow to publish the new version.
Otherwise, `publish` workflow will fail.

#### Local

This repository is public, so we can't write about secrets values, but you can find it in the internal documentation.

```sh
$ export ORG_GRADLE_PROJECT_signingKeyId=XXXXXXXXXXXX
$ export ORG_GRADLE_PROJECT_signingKey=XXXXXXXXXXXX
$ export ORG_GRADLE_PROJECT_signingPassword=XXXXXXXXXXXX
$ export ORG_GRADLE_PROJECT_sonatypeUsername=XXXXXXXXXXXX
$ export ORG_GRADLE_PROJECT_sonatypePassword=XXXXXXXXXXXX
$ ./gradlew clean publishToSonatype closeAndReleaseSonatypeStagingRepository
```
