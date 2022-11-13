import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        "classpath"(group = "com.fasterxml.jackson.core", name = "jackson-databind", version = "2.12.6")
        "classpath"(group = "com.squareup.okhttp3", name = "okhttp", version = "4.9.3")
    }
}

tasks.register("generateChangelog") {
    doLast {
        val client = OkHttpClient()
        val milestone = "Next Version"
        val repo = "Javacord/Javacord"

        val httpBuilderPrs: HttpUrl.Builder = "https://api.github.com/search/issues".toHttpUrl().newBuilder().apply {
            addQueryParameter("sort", "created")
            addQueryParameter("order", "asc")
            addQueryParameter("per_page", "100")
            addEncodedQueryParameter("q", """type:pr+repo:$repo+milestone:"$milestone"+is:merged""")
        }

        val allPrsWithNextVersionMilestoneRequestBuilder: Request.Builder =
            Request.Builder().addHeader("Accept", "application/vnd.github+json")

        val mapper = ObjectMapper()
        val itemsArray: ArrayNode = JsonNodeFactory.instance.arrayNode()
        var page = 1
        while (true) {
            httpBuilderPrs.addQueryParameter("page", "$page")
            allPrsWithNextVersionMilestoneRequestBuilder.url(httpBuilderPrs.build())

            val jsonNode: JsonNode =
                mapper.readTree(
                    client.newCall(allPrsWithNextVersionMilestoneRequestBuilder.build()).execute().body!!.string()
                )

            jsonNode["items"].forEach { itemsArray.add(it) }
            if (jsonNode["total_count"].asInt() / (page * 100) == 0) break else page++
        }

        @OptIn(kotlin.ExperimentalStdlibApi::class)
        val prs = buildList {
            for (item in itemsArray) {
                val lines = item["body"].asText().trimIndent().lines()

                val changes = getBulletPoints(lines, "## Changelog")
                val breakingChanges = getBulletPoints(lines, "### Breaking Changes")

                PullRequest(item["html_url"].asText(), item["user"]["login"].asText(), changes, breakingChanges)
                    .takeIf { it.authorName != "allcontributors[bot]" }
                    ?.let { add(it) }
            }
        }

        val generatedChangelog = buildChangelog(prs)
        val myfile = File("$projectDir/CHANGELOG.md")

        print(generatedChangelog)
        myfile.writeText(generatedChangelog)
    }
}

data class PullRequest(
    val prLink: String,
    val authorName: String,
    val changes: List<String>,
    val breakingChanges: List<String>
)

fun buildChangelog(prs: List<PullRequest>): String {
    val docBuilder = StringBuilder("# \uD83D\uDCCB Changelog").appendLine()

    val impBuilder = StringBuilder("### Improvements").appendLine()
    val fixBuilder = StringBuilder("### Bugfixes").appendLine()
    var addFixBuilder = false;
    var addImpBuilder = false;
    prs.forEach {
        it.changes.forEach { change ->
            if (change.startsWith("Fixed", true)) {
                addFixBuilder = true
                fixBuilder
            } else {
                addImpBuilder = true
                impBuilder
            }.append("- $change (${it.prLink})").append(" by @").append(it.authorName).appendLine()
        }
    }

    val bcBuilder = StringBuilder("### Breaking Changes").appendLine()
    var addBcBuilder = false;
    prs.forEach {
        it.breakingChanges.forEach { change ->
            addBcBuilder = true
            bcBuilder.append("- $change (${it.prLink})").append(" by @").append(it.authorName).appendLine()
        }
    }

    if (addImpBuilder) {
        docBuilder.appendLine().append(impBuilder)
    }
    if (addFixBuilder) {
        docBuilder.appendLine().append(fixBuilder)
    }
    if (addBcBuilder) {
        docBuilder.appendLine().append(bcBuilder)
    }

    val noChangelogPrs = prs.filter { it.changes.isEmpty() && it.breakingChanges.isEmpty() }
    if (noChangelogPrs.isNotEmpty()) {
        docBuilder.appendLine().append("UNKNOWN PR INFORMATION:").appendLine()
        noChangelogPrs.forEach { docBuilder.append("- ${it.prLink}").appendLine() }
    }

    return docBuilder.toString()
}

fun getBulletPoints(lines: List<String>, header: String) = @OptIn(kotlin.ExperimentalStdlibApi::class) buildList {
    for ((index, line) in lines.withIndex()) {
        if (line == header) {
            var secondIndex = 1
            while (index + secondIndex < lines.size && lines[index + secondIndex].trim().startsWith("-")) {
                add(lines[index + secondIndex].replaceFirst("-", "").trim())
                secondIndex++
            }
            break
        }
    }
}