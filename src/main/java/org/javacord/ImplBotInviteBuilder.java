package org.javacord;

import org.javacord.entity.permission.Permissions;
import org.javacord.entity.permission.impl.ImplPermissions;

/**
 * A tool to create bot invites.
 */
public class ImplBotInviteBuilder implements BotInviteBuilder {

    /**
     * The base link of a bot invite.
     */
    public static final String BASE_LINK =
            "https://discordapp.com/oauth2/authorize?client_id=%s&scope=bot&permissions=%s";

    /**
     * The client id of the bot's application.
     */
    private long clientId;

    /**
     * The permissions for the bot.
     */
    private Permissions permissions = ImplPermissions.EMPTY_PERMISSIONS;

    /**
     * Creates a new bot invite builder.
     *
     * @param clientId The client id of the bot's application.
     */
    public ImplBotInviteBuilder(long clientId) {
        this.clientId = clientId;
    }

    /**
     * Creates a new bot invite builder.
     *
     * @param clientId The client id of the bot's application.
     */
    public ImplBotInviteBuilder(String clientId) {
        try {
            this.clientId = Long.parseLong(clientId);
        } catch (NumberFormatException e) {
            this.clientId = 0;
        }
    }

    @Override
    public BotInviteBuilder setPermissions(Permissions permissions) {
        this.permissions = permissions;
        return this;
    }

    @Override
    public String build() {
        return String.format(BASE_LINK, clientId, ((ImplPermissions) permissions).getAllowed());
    }

}
