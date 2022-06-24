import java.time.Instant

plugins {
    `java-library`
    id("com.gorylenko.gradle-git-properties") version "2.4.0"
    id("biz.aQute.bnd.builder") version "6.2.0"
}

repositories {
    mavenCentral()
}

dependencies {
    // For old @Generated annotation in Java 9
    // can be replaced by javax.annotation.processing.Generated if Java 9 is minimum requirement
    compileOnly("javax.annotation:javax.annotation-api:1.3.2")
}

java {
    withJavadocJar()
    withSourcesJar()
}

/*
 * Generate a "git.properties" file with additional information about the current
 * version and the build timestamp. Used by the Javacord class to provide static
 * methods like "Javacord.VERSION" and "Javacord.COMMIT_ID".
 */
gitProperties {
    customProperty("version", version)
    customProperty("buildTimestamp", Instant.now())
}

tasks.jar {
    bundle {
        val version by archiveVersion
        bnd(
            mapOf(
                "Export-Package" to listOf(
                    "!org.javacord.*.internal.*",
                    "*",
                    "version=$version",
                    "-noimport:=true"
                ).joinToString(";"),
                // work-around for https://github.com/bndtools/bnd/issues/2227
                "-fixupmessages" to "^Classes found in the wrong directory: \\\\{META-INF/versions/9/module-info\\\\.class=module-info}$"
            )
        )
    }
}
