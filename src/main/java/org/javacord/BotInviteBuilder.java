package org.javacord;

import org.javacord.entity.permission.Permissions;

/**
 * A tool to create bot invites.
 */
public interface BotInviteBuilder {

    /**
     * Sets the permissions the bot should have.
     *
     * @param permissions The permissions to set.
     * @return The current instance in order to chain call methods.
     */
    BotInviteBuilder setPermissions(Permissions permissions);

    /**
     * Creates the invite link.
     *
     * @return The invite link for the bot.
     */
    String build();

}
