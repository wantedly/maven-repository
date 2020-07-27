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

private const val AUTHORITY = "wantedly-maven.s3.ap-northeast-1.amazonaws.com"

fun RepositoryHandler.wantedly(useSnapshots: Boolean = false): MavenArtifactRepository {
    return maven {
        val wtdMavenAccessKey = getProp("WTD_MAVEN_ACCESS_KEY")
        val wtdMavenSecretKey = getProp("WTD_MAVEN_SECRET_KEY")
        val authorityAndPath = AUTHORITY + if (useSnapshots) "/snapshots" else ""
        val isWtdMavenCredentialsExists = wtdMavenAccessKey != null && wtdMavenSecretKey != null
        if (isWtdMavenCredentialsExists) {
            url = URI("s3://$authorityAndPath")
            credentials(AwsCredentials::class) {
                accessKey = wtdMavenAccessKey
                secretKey = wtdMavenSecretKey
            }
        } else {
            url = URI("https://$authorityAndPath")
        }
        content {
            includeGroupByRegex("""com\.wantedly.*""")
        }
    }
}
