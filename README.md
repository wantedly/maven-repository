## wantedly-maven-repository

Make it easier introducing Wantedly local maven repository to your Gradle repository.

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
    wantedly()
}

dependencies {
    // You can use the artifacts that have "com.wantedly" groupId!
    implementation("com.wantedly.foo:bar:baz")
}
```

### How to publish to JCenter

TODO
