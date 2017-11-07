package de.btobastian.javacord.utils.rest;

import de.btobastian.javacord.Javacord;

import java.util.Optional;

/**
 * This enum contains all endpoints which we may use.
 */
public enum RestEndpoint {

    GATEWAY("/gateway"),
    GATEWAY_BOT("/gateway/bot"),
    MESSAGE("/channels/%s/messages", 0),
    /**
     * This is the same endpoint as {@link RestEndpoint#MESSAGE}, but it has an different ratelimit bucket!
     */
    MESSAGE_DELETE("/channels/%s/messages", 0),
    MESSAGES_BULK_DELETE("/channels/%s/messages/bulk-delete", 0),
    CHANNEL_TYPING("/channels/%s/typing", 0),
    USER_CHANNEL("/users/@me/channels"),
    CHANNEL("/channels/%s", 0),
    ROLE("/guilds/%s/roles/%s", 0),
    SERVER("/guilds/%s", 0),
    SERVER_CHANNEL("/guilds/%s/channels", 0),
    REACTION("/channels/%s/messages/%s/reactions/%s", 0),
    PINS("/channels/%s/pins", 0),
    SERVER_MEMBER("/guilds/%s/members/%s", 0),
    OWN_NICKNAME("/guilds/%s/members/@me/nick", 0),
    SELF_INFO("/oauth2/applications/@me"),
    CHANNEL_WEBHOOK("/channels/%s/webhooks", 0),
    SERVER_WEBHOOK("/guilds/%s/webhooks", 0),
    WEBHOOK("/webhooks/%s");

    /**
     * The endpoint url (only including the base, not the https://discordapp.com/api/vXYZ/ "prefix".
     */
    private final String endpointUrl;

    /**
     * The position of the major parameter starting with <code>0</code> or <code>-1</code> if no major parameter exists.
     */
    private final int majorParameterPosition;

    /**
     * Whether the endpoint is global or not.
     */
    private boolean global;

    RestEndpoint(String endpointUrl) {
        this(endpointUrl, -1, false);
    }

    RestEndpoint(String endpointUrl, boolean global) {
        this(endpointUrl, -1, global);
    }

    RestEndpoint(String endpointUrl, int majorParameterPosition) {
        this(endpointUrl, majorParameterPosition, false);
    }

    RestEndpoint(String endpointUrl, int majorParameterPosition, boolean global) {
        this.endpointUrl = endpointUrl;
        this.majorParameterPosition = majorParameterPosition;
        this.global = global;
    }

    /**
     * Gets the major parameter position of the endpoint.
     * If an endpoint has a major parameter, it means that the ratelimits for this endpoint are based on this parameter.
     * The position starts counting at <code>0</code>!
     *
     * @return An optional which is present, if the endpoint has a major parameter.
     */
    public Optional<Integer> getMajorParameterPosition() {
        if (majorParameterPosition >= 0) {
            return Optional.of(majorParameterPosition);
        }
        return Optional.empty();
    }

    /**
     * Gets the endpoint url (only including the base, not the https://discordapp.com/api/vXYZ/ "prefix".
     *
     * @return The gateway url.
     */
    public String getEndpointUrl() {
        return endpointUrl;
    }

    /**
     * Checks if the endpoint is global.
     *
     * @return Whether the endpoint is global or not.
     */
    public boolean isGlobal() {
        return global;
    }

    /**
     * Sets whether this endpoint is global or not.
     *
     * @param global If the endpoint is global.
     */
    public void setGlobal(boolean global) {
        this.global = global;
    }

    /**
     * Gets the full url of the endpoint.
     * Parameters which are "too much" are added to the end.
     *
     * @param parameters The parameters of the url. E.g. for channel ids.
     * @return The full url of the endpoint.
     */
    public String getFullUrl(String... parameters) {
        String url = "https://discordapp.com/api/v" + Javacord.DISCORD_GATEWAY_PROTOCOL_VERSION + getEndpointUrl();
        url = String.format(url, (Object[]) parameters);
        int parameterAmount = getEndpointUrl().split("%s").length - (getEndpointUrl().endsWith("%s") ? 0 : 1);
        if (parameters.length > parameterAmount) {
            for (int i = parameterAmount; i < parameters.length; i++) {
                url += "/" + parameters[i];
            }
        }
        return url;
    }

}
