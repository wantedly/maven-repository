package com.wantedly.maven.repository

import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.credentials.AwsCredentials
import org.gradle.kotlin.dsl.credentials
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.net.URI
import java.util.Properties

private val logger: Logger = LoggerFactory.getLogger("WantedlyMavenRepository")

/**
 * Adds a GitHub Packages Maven repository of Wantedly organization.
 * `GITHUB_TOKEN` environment variable must be set.
 *
 * @param repo The name of the Wantedly organization's repository.
 * @param group The name of the Maven artifact group. `com.wantedly.*` is set by default if not specified.
 */
fun RepositoryHandler.wantedly(repo: String, group: String? = null): MavenArtifactRepository {
    return maven {
        url = URI("https://maven.pkg.github.com/wantedly/$repo")
        credentials {
            username = "not used but required"

            val ghToken = getProp("GITHUB_TOKEN")
            if (ghToken.isNullOrEmpty()) {
                logger.warn("You will get an authorization error if you don't set `GITHUB_TOKEN` as an environment variable or as a property in `local.properties`.")
            }
            password = ghToken
        }
        content {
            if (group != null) {
                includeGroup(group)
            } else {
                includeGroupByRegex("""com\.wantedly.*""")
            }
        }
    }
}

private fun getProp(name: String): String? {
    return File("local.properties")
        .takeIf { it.exists() }
        ?.let { Properties().apply { load(it.reader()) }.getProperty(name) }
        ?: System.getenv(name)
}

private const val AUTHORITY = "wantedly-maven.s3.ap-northeast-1.amazonaws.com"

@Deprecated(
    message = "S3 Maven has been deprecated. You should migrate to GitHub Packages.",
    replaceWith = ReplaceWith("""wantedly("repo-name")""")
)
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
