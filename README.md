## wantedly-maven-repository

[ ![Download](https://api.bintray.com/packages/wantedly-dev/maven/com.wantedly%3Awantedly-maven-repository/images/download.svg) ](https://bintray.com/wantedly-dev/maven/com.wantedly%3Awantedly-maven-repository/_latestVersion)

Make it easier adding a Wantedly's private Maven repository to your Gradle script.

### Setup

```kotlin
import com.wantedly.maven.repository.wantedly

buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath("com.wantedly:wantedly-maven-repository:${LATEST_VERSION}")
    }
}

repositories {
    // Adds a GitHub Packages Maven repository.
    wantedly(repo = "repo-name")
    // S3 Maven repository is depreacted. Use Github packages instead.
    wantedly()
    // Deprecated: Add this if you want to use the snapshot versions.
    wantedly(useSnapshots = true)
}

dependencies {
    // You can use the artifacts that have "com.wantedly" groupId!
    implementation("com.wantedly.foo:bar:baz")
}
```

### How to publish to JCenter

1. Upload from your local.

    ```sh
    $ export BINTRAY_USER=wantedly-dev
    $ export BINTRAY_KEY=1234567890
    $ ./gradlew clean bintrayUpload
    ```

1. Go to https://bintray.com/wantedly-dev/maven/com.wantedly%3Awantedly-maven-repository
1. Click Publish button
