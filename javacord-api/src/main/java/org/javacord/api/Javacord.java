package org.javacord.api;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.Properties;

/**
 * This class contains some static information about Javacord.
 */
public class Javacord {

    /**
     * The current Javacord version.
     *
     * @see #DISPLAY_VERSION
     */
    public static final String VERSION;

    /**
     * The commit ID from which the current Javacord version was built.
     *
     * @see #DISPLAY_VERSION
     */
    public static final String COMMIT_ID;

    /**
     * The build timestamp at which the current Javacord version was built.
     *
     * @see #DISPLAY_VERSION
     */
    public static final Instant BUILD_TIMESTAMP;

    /**
     * The display version of the current Javacord version.
     * If the current Javacord version is a release version, it is equal to {@link #VERSION}.
     * If the current Javacord version is a snapshot version, it consists of the {@link #VERSION}, the
     * {@link #COMMIT_ID}, and the {@link #BUILD_TIMESTAMP}.
     * For displaying the version somewhere it is best to use this constant.
     *
     * @see #VERSION
     * @see #COMMIT_ID
     * @see #BUILD_TIMESTAMP
     */
    public static final String DISPLAY_VERSION;

    static {
        Properties versionProperties = new Properties();
        try (InputStream versionPropertiesStream = Javacord.class.getResourceAsStream("/version.properties")) {
            versionProperties.load(versionPropertiesStream);
        } catch (IOException ignored) { }

        String version = versionProperties.getProperty("version", "$version");
        switch (version) {
            case "$version":
                VERSION = "<unknown>";
                break;

            default:
                VERSION = version;
        }

        String commitId = versionProperties.getProperty("commitId", "$commitId");
        switch (commitId) {
            case "$commitId":
                COMMIT_ID = "<unknown>";
                break;

            default:
                COMMIT_ID = commitId;
        }

        String buildTimestamp = versionProperties.getProperty("buildTimestamp", "$buildTimestamp");
        switch (buildTimestamp) {
            case "$buildTimestamp":
                BUILD_TIMESTAMP = null;
                break;

            default:
                BUILD_TIMESTAMP = Instant.parse(buildTimestamp);
        }

        DISPLAY_VERSION = version.endsWith("-SNAPSHOT")
                ? String.format("%s [%s | %s]", VERSION, COMMIT_ID, BUILD_TIMESTAMP)
                : VERSION;
    }

    /**
     * The github url of javacord.
     */
    public static final String GITHUB_URL = "https://github.com/Javacord/Javacord";

    /**
     * The user agent used for requests.
     */
    public static final String USER_AGENT = "DiscordBot (" + GITHUB_URL + ", v" + DISPLAY_VERSION + ")";

    /**
     * The gateway version from Discord which we are using.
     * A list with all gateway versions can be found
     * <a href="https://discordapp.com/developers/docs/topics/gateway#gateways-gateway-versions">here</a>.
     */
    public static final String DISCORD_GATEWAY_VERSION = "6";

    /**
     * The voice gateway version from Discord which we are using.
     * A list with all voice gateway versions can be found
     * <a href="https://discordapp.com/developers/docs/topics/voice-connections#voice-gateway-versioning">here</a>.
     */
    public static final String DISCORD_VOICE_GATEWAY_VERSION = "4";

    /**
     * The API version from Discord which we are using.
     * A list with all API versions can be found
     * <a href="https://discordapp.com/developers/docs/reference#api-versioning-api-versions">here</a>.
     */
    public static final String DISCORD_API_VERSION = "6";

    private Javacord() {
        throw new UnsupportedOperationException();
    }
}
