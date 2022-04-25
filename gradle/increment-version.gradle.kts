/*
 * If the current version is not a snapshot version, this task will update the version by one
 * patch level, append "-SNAPSHOT", and replace the old version in the gradle.properties file.
 */
tasks.register("incrementVersion") {
    doLast {
        if (version.toString().endsWith("-SNAPSHOT")) {
            return@doLast
        }

        val splitVersion = version.toString().split(".")
        val newVersion = "${splitVersion[0]}.${splitVersion[1]}.${splitVersion[2].toInt() + 1}-SNAPSHOT"

        file("gradle.properties").writeText(
            file("gradle.properties").readText().replace(version.toString(), newVersion)
        )
    }
}