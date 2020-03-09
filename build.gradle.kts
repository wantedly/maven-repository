plugins {
    kotlin("dsl")
    kotlin("jvm")
}

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

gradlePlugin {
    val mavenRepository by plugins.creating {
        id = "com.wantedly.maven.repository"
        implementationClass = "com.wantedly.maven.repository.MavenRepositoryPlugin"
    }
}
