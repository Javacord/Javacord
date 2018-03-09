package org.javacord.util;

import org.javacord.DiscordApiFactory;
import org.javacord.entity.message.MessageFactory;
import org.javacord.entity.message.embed.EmbedFactory;
import org.javacord.entity.permission.Permissions;
import org.javacord.entity.permission.PermissionsFactory;
import org.javacord.util.exception.DiscordExceptionValidator;

/**
 * This class is used by Javacord internally.
 * You probably won't need it ever.
 */
public interface FactoryBuilderDelegate {

    /**
     * Creates a new discord api factory.
     *
     * @return A new discord api factory.
     */
    DiscordApiFactory createDiscordApiFactory();

    /**
     * Creates a new embed factory.
     *
     * @return A new embed factory.
     */
    EmbedFactory createEmbedFactory();

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

    /**
     * Creates a new discord exception validator.
     *
     * @return A new discord exception validator.
     */
    DiscordExceptionValidator createDiscordExceptionValidator();

}
