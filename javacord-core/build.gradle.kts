import aQute.bnd.version.MavenVersion.parseMavenString

plugins {
    `java-library`
    id("biz.aQute.bnd.builder") version "6.3.1"
}

repositories {
    mavenCentral()
}

dependencies {
    api(project(":javacord-api")) {
        isTransitive = true
    }

    // OkHttp for REST-calls
    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.9.3"))

    // define any required OkHttp artifacts without version
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:logging-interceptor")

    // the JSON-lib because Discord returns in JSON format
    // Update when upgrading to Java 11+
    // https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind
    implementation("com.fasterxml.jackson.core:jackson-databind:2.12.7.1")

    // the web socket
    implementation("com.neovisionaries:nv-websocket-client:2.14")

    // voice encryption
    implementation("com.codahale:xsalsa20poly1305:0.11.0")

    // logging
    implementation("org.apache.logging.log4j:log4j-api:2.17.2")

    // Vavr, mainly for immutable collections
    // We are using 0.10.1, because of an issue in 0.10.2: https://github.com/vavr-io/vavr/issues/2573
    implementation("io.vavr:vavr:0.10.4")

    // For old @Generated annotation in Java 9
    // can be replaced by javax.annotation.processing.Generated if Java 9 is minimum requirement
    compileOnly("javax.annotation:javax.annotation-api:1.3.2")
}

java {
    withJavadocJar()
    withSourcesJar()
}

tasks.jar {
    bundle {
        val version by archiveVersion
        val osgiVersion = "${parseMavenString(version).osGiVersion}"
        bnd(
            mapOf(
                "Fragment-Host" to "org.javacord.api;bundle-version=\"[$osgiVersion,$osgiVersion]\"",
                "Import-Package" to listOf(
                    "!org.javacord.api.*",
                    "*"
                ).joinToString(),
                // work-around for https://github.com/bndtools/bnd/issues/2227
                "-fixupmessages" to listOf(
                    """^Classes found in the wrong directory:""",
                    """\\{META-INF/versions/9/org/javacord/core/entity/message/MessageSetImpl$1.class=org.javacord.core.entity.message.MessageSetImpl$1,""",
                    """META-INF/versions/9/module-info.class=module-info,""",
                    """META-INF/versions/9/org/javacord/core/entity/message/MessageSetImpl.class=org.javacord.core.entity.message.MessageSetImpl,""",
                    """META-INF/versions/9/org/javacord/core/entity/message/MessageSetImpl$2.class=org.javacord.core.entity.message.MessageSetImpl$2}$"""
                ).joinToString(" ")
            )
        )
    }
}

tasks.javadoc {
    dependsOn(project(":javacord-api").tasks.javadoc)
    options {
        this as StandardJavadocDocletOptions
        val releaseVersion: Boolean by rootProject.extra
        val apiJavadocs = "${project(":javacord-api").tasks.javadoc.get().destinationDir!!.toURI()}"
        val releasedApiJavadocs = "https://docs.javacord.org/api/v/$version"
        linksOffline(if (releaseVersion) releasedApiJavadocs else apiJavadocs, apiJavadocs)
    }
}
