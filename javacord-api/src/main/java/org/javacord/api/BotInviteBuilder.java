package org.javacord.api;

import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.entity.permission.PermissionsBuilder;

/**
 * A tool to create bot invites.
 */
public class BotInviteBuilder {

    /**
     * The base link of a bot invite.
     */
    public static final String BASE_LINK =
            "https://" + Javacord.DISCORD_DOMAIN + "/oauth2/authorize?client_id=%s&scope=bot&permissions=%s";

    /**
     * The client id of the bot's application.
     */
    private long clientId;

    /**
     * The permissions for the bot.
     */
    private Permissions permissions = new PermissionsBuilder().build();

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
            this.clientId = 0;
        }
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
     * Creates the invite link.
     *
     * @return The invite link for the bot.
     */
    public String build() {
        return String.format(BASE_LINK, clientId, permissions.getAllowedBitmask());
    }

}
