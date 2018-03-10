package org.javacord.util;

import org.javacord.DiscordApiBuilderDelegate;
import org.javacord.entity.message.MessageFactory;
import org.javacord.entity.message.embed.EmbedBuilderDelegate;
import org.javacord.entity.permission.Permissions;
import org.javacord.entity.permission.PermissionsFactory;

/**
 * This class is used by Javacord internally.
 * You probably won't need it ever.
 */
public interface FactoryBuilderDelegate {

    /**
     * Creates a new discord api builder delegate.
     *
     * @return A new discord api builder delegate.
     */
    DiscordApiBuilderDelegate createDiscordApiBuilderDelegate();

    /**
     * Creates a new embed builder delegate.
     *
     * @return A new embed builder delegate.
     */
    EmbedBuilderDelegate createEmbedBuilderDelegate();

    /**
     * Creates a new message factory.
     *
     * @return A new message factory.
     */
    MessageFactory createMessageFactory();

    /**
     * Creates a new permissions factory.
     *
     * @return A new permissions factory.
     */
    PermissionsFactory createPermissionsFactory();

    /**
     * Creates a new permissions factory initialized with the given permissions.
     *
     * @param permissions The permissions which should be copied.
     * @return A new permissions factory initialized with the given permissions.
     */
    PermissionsFactory createPermissionsFactory(Permissions permissions);

}
