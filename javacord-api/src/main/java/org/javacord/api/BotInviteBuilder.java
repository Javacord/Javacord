package org.javacord.api;

import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.entity.permission.PermissionsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * A tool to create bot invites.
 */
public class BotInviteBuilder {

    /**
     * The application client identification.
     */
    private long clientId;

    /**
     * The base link for a bot application invite link.
     */
    public static final String BASE_LINK =
            "https://" + Javacord.DISCORD_DOMAIN + "/oauth2/authorize?client_id=";

    /**
     * The URI to redirect after inviting.
     */
    private String redirectUri = "";

    /**
     * The scopes to append to the link.
     */
    private Collection<BotInviteScope> inviteScopes = new ArrayList<>();

    /**
     * The permissions for the bot.
     */
    private Permissions permissions = new PermissionsBuilder().build();

    /**
     * To append the consent field into the invite builder or not.
     */
    private boolean consent = true;

    /**
     * Creates a new bot invite builder.
     *
     * @param clientId The client id of the bot's application.
     */
    public BotInviteBuilder(long clientId) {
        this.clientId = clientId;
    }

    /**
     * Creates a new bot invite builder.
     *
     * @param clientId The client id of the bot's application.
     */
    public BotInviteBuilder(String clientId) {
        try {
            this.clientId = Long.parseLong(clientId);
        } catch (NumberFormatException e) {
            this.clientId = 0L;
        }
    }

    /**
     * Creates a new bot invite builder.
     *
     * @param api The Discord API to use.
     */
    public BotInviteBuilder(DiscordApi api) {
        this.clientId = api.getClientId();
    }

    /**
     * Sets the permissions the bot should have.
     *
     * @param permissions The permissions to set.
     * @return The current instance in order to chain call methods.
     */
    public BotInviteBuilder setPermissions(Permissions permissions) {
        this.permissions = permissions;
        return this;
    }

    /**
     * Sets the permissions the bot should have.
     *
     * @param permissions The permissions to set.
     * @return The current instance in order to chain call methods.
     */
    public BotInviteBuilder setPermissions(PermissionType... permissions) {
        this.permissions = new PermissionsBuilder()
                .setAllowed(permissions)
                .build();
        return this;
    }

    /**
     * Sets the redirect uri for this invite builder.
     *
     * @param redirectUri The redirect uri to set.
     * @return The current instance in order to chain call methods.
     */
    public BotInviteBuilder setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
        return this;
    }

    /**
     * Sets the scopes for this invite builder.
     *
     * @param scopes The scopes to set.
     * @return The current instance in order to chain call methods.
     */
    public BotInviteBuilder setScopes(Collection<BotInviteScope> scopes) {
        this.inviteScopes = scopes;
        return this;
    }

    /**
     * Sets the scopes for this invite builder.
     *
     * @param scopes The scopes to set.
     * @return The current instance in order to chain call methods.
     */
    public BotInviteBuilder setScopes(BotInviteScope... scopes) {
        this.inviteScopes = Arrays.asList(scopes);
        return this;
    }

    /**
     * Adds the scopes for this invite builder.
     *
     * @param scopes The scopes to add.
     * @return The current instance in order to chain call methods.
     */
    public BotInviteBuilder addScopes(Collection<BotInviteScope> scopes) {
        this.inviteScopes.addAll(scopes);
        return this;
    }

    /**
     * Adds the scopes for this invite builder.
     *
     * @param scopes The scopes to add.
     * @return The current instance in order to chain call methods.
     */
    public BotInviteBuilder addScopes(BotInviteScope... scopes) {
        this.inviteScopes.addAll(Arrays.asList(scopes));
        return this;
    }

    /**
     * Sets whether to append prompt consent to the invite link.
     *
     * @param promptConsent Whether to append prompt consent or not.
     * @return The current instance in order to chain call methods.
     */
    public BotInviteBuilder setPromptConsent(boolean promptConsent) {
        this.consent = promptConsent;
        return this;
    }

    /**
     * Creates the invite link.
     *
     * @return The invite link for the bot.
     */
    public String build() {
        if (inviteScopes.isEmpty()) {
            addScopes(BotInviteScope.BOT, BotInviteScope.APPLICATIONS_COMMANDS);
        }

        StringBuilder builder = new StringBuilder(BASE_LINK);
        builder.append(clientId)
                .append("&scope=")
                .append(
                        inviteScopes.stream()
                                .map(BotInviteScope::getScope)
                                .collect(Collectors.joining("%20"))
                )
                .append("&permissions=")
                .append(permissions.getAllowedBitmask());

        if (redirectUri != null && !redirectUri.isEmpty()) {
            try {
                builder.append("&redirect_uri=")
                        .append(URLEncoder.encode(redirectUri, "UTF-8"))
                        .append("&response_type=code");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        if (consent) {
            builder.append("&prompt=consent");
        }

        return builder.toString();
    }

}
