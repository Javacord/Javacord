plugins {
    `java-library`
    `maven-publish`
    signing
    id("com.github.johnrengelman.shadow") version "2.0.4" apply false
    id("net.researchgate.release") version "2.7.0" apply false
    id("biz.aQute.bnd.builder") version "5.3.0"
}

repositories {
    mavenCentral()
}

dependencies {
    api(project(":javacord-api"))
    implementation(project(":javacord-core"))
}

allprojects {
    group = "org.javacord"

    apply(plugin = "java-library")
    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

task("generate") // We need to create the task so that the generation files can extend it

val releaseVersion by extra(!version.toString().endsWith("-SNAPSHOT"))

subprojects {
    val shortName by extra(name.replace("javacord-", ""))
    description = "An easy to use multithreaded library for creating Discord bots in Java ($shortName)"
}

apply(from = "gradle/readme.gradle.kts")
apply(from = "gradle/jars.gradle")
apply(from = "gradle/java9.gradle")
apply(from = "gradle/tests.gradle")
apply(from = "gradle/javadoc.gradle")
apply(from = "gradle/listener-manager-generation.gradle")
apply(from = "gradle/event-dispatcher-generation.gradle")
apply(from = "gradle/checkstyle.gradle.kts")

configure<PublishingExtension> {
    allprojects {
        val isReleaseVersion = !version.toString().endsWith("SNAPSHOT")

        apply(plugin = "java-library")
        apply(plugin = "maven-publish")
        apply(plugin = "signing")

        publishing {
            publications {
                create<MavenPublication>("javacord") {
                    pom {
                        name.set(rootProject.name.capitalize() + (if (project.parent != null) " (${project.ext.get("shortName")})" else ""))
                        description.set(project.description)
                        url.set("https://javacord.org")
                        issueManagement {
                            system.set("GitHub")
                            url.set("https://github.com/Javacord/Javacord/issues")
                        }
                        ciManagement {
                            system.set("GitHub Actions")
                        }
                        inceptionYear.set("2015")
                        developers {
                            developer {
                                id.set("Bastian")
                                name.set("Bastian Oppermann")
                                email.set("bastianoppermann1997@gmail.com")
                                url.set("https://github.com/Bastian")
                                timezone.set("Europe/Berlin")
                            }
                        }
                        contributors {
                            contributor {
                                name.set("Björn Kautler")
                                email.set("Bjoern@Kautler.net")
                                url.set("https://github.com/Vampire")
                                timezone.set("Europe/Berlin")
                            }
                        }
                        licenses {
                            license {
                                name.set("Apache License, Version 2.0")
                                url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                                distribution.set("repo")
                                comments.set("A business-friendly OSS license")
                            }
                        }
                        scm {
                            connection.set("scm:git:https://github.com/Javacord/Javacord.git")
                            developerConnection.set("scm:git:git@github.com:Javacord/Javacord.git")
                            url.set("https://github.com/Javacord/Javacord")
                        }
                        distributionManagement {
                            downloadUrl.set("https://github.com/Javacord/Javacord/releases")
                        }
                    }
                }
            }

            repositories {
                maven {
                    name = "OSSRH"
                    url = if (isReleaseVersion) {
                        uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
                    } else {
                        uri("https://oss.sonatype.org/content/repositories/snapshots/")
                    }
                    credentials {
                        username = System.getenv("MAVEN_USERNAME")
                        password = System.getenv("MAVEN_PASSWORD")
                    }
                }
            }
        }

        signing {
            val signingKey = System.getenv("SIGNING_KEY")
            val signingPassword = System.getenv("SIGNING_PASSWORD")
            useInMemoryPgpKeys(signingKey, signingPassword)
            sign(publishing.publications["javacord"])
        }
    }
}

subprojects {
    publishing {
        publications {
            named<MavenPublication>("javacord") {
                from(components["java"])
            }
        }
    }
}

publishing {
    publications {
        named<MavenPublication>("javacord") {
            // add the dependencies to the POM
            from(components["java"])
            // but do not try to publish the JAR
            artifacts.removeAll { it.classifier == null && it.extension == "jar" }
        }
    }
}