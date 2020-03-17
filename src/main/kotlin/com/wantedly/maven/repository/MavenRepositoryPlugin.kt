package com.wantedly.maven.repository

import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.credentials.AwsCredentials
import org.gradle.kotlin.dsl.credentials
import java.io.File
import java.net.URI
import java.util.Properties

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
        val authority = "wantedly-maven.s3.ap-northeast-1.amazonaws.com"
        if (isWtdMavenCredentialsExists) {
            url = URI("s3://$authority")
            credentials(AwsCredentials::class) {
                accessKey = wtdMavenAccessKey
                secretKey = wtdMavenSecretKey
            }
        } else {
            url = URI("https://$authority")
        }
        content {
            includeGroupByRegex("""com\.wantedly.*""")
        }
    }
}
