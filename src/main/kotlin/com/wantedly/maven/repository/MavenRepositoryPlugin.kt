package com.wantedly.maven.repository

import org.gradle.api.Project
import org.gradle.api.Plugin
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.credentials.AwsCredentials
import org.gradle.kotlin.dsl.credentials
import java.io.File
import java.net.URI
import java.util.*

class MavenRepositoryPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        TODO("This plugin does nothing so you must not apply this plugin.")
    }
}

private fun getProp(name: String): String? {
    return File("local.properties")
        .takeIf { it.exists() }
        ?.let { Properties().apply { load(it.reader()) }.getProperty(name) }
        ?: System.getenv(name)
}

private val wtdMavenAccessKey = getProp("WTD_MAVEN_ACCESS_KEY")
private val wtdMavenSecretKey = getProp("WTD_MAVEN_SECRET_KEY")
private val isWtdMavenCredentialsExists = wtdMavenAccessKey != null && wtdMavenSecretKey != null

fun RepositoryHandler.wantedly(): MavenArtifactRepository {
    return maven {
        if (isWtdMavenCredentialsExists) {
            url = URI("s3://wantedly-maven")
            credentials(AwsCredentials::class) {
                accessKey = wtdMavenAccessKey
                secretKey = wtdMavenSecretKey
            }
        } else {
            url = URI("https://wantedly-maven.s3.ap-northeast-1.amazonaws.com")
        }
        content {
            includeGroupByRegex("""com\.wantedly.*""")
        }
    }
}
