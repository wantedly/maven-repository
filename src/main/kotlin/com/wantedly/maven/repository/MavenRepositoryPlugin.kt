package com.wantedly.maven.repository

import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.jetbrains.kotlin.konan.properties.loadProperties
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.net.URI

private val logger: Logger = LoggerFactory.getLogger("WantedlyMavenRepository")

/**
 * Adds a GitHub Packages Maven repository of Wantedly organization.
 * Either `GITHUB_TOKEN` environment variable or `GITHUB_TOKEN` property in project's `local.properties` must be set.
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
    return System.getenv(name)
        ?: File("local.properties").takeIf { it.exists() }?.let { loadProperties(it.path).getProperty(name) }
}
