package org.javacord.util;

import org.javacord.DiscordApiFactory;
import org.javacord.entity.message.MessageFactory;
import org.javacord.entity.message.embed.EmbedFactory;
import org.javacord.entity.permission.Permissions;
import org.javacord.entity.permission.PermissionsFactory;
import org.javacord.util.exception.DiscordExceptionValidator;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * This class is used by Javacord internally.
 * You probably won't need it ever.
 */
public class FactoryBuilder {

    /**
     * The factory builder delegate. It's used to create new factories.
     */
    private static final FactoryBuilderDelegate factoryBuilderDelegate;

    /**
     * The discord exception validator.
     */
    private static final DiscordExceptionValidator discordExceptionValidator;

    // Load it static, because it has a better performance to load it only once
    static {
        ServiceLoader<FactoryBuilderDelegate> delegateServiceLoader = ServiceLoader.load(FactoryBuilderDelegate.class);
        Iterator<FactoryBuilderDelegate> delegateIterator = delegateServiceLoader.iterator();
        if (delegateIterator.hasNext()) {
            factoryBuilderDelegate = delegateIterator.next();
            if (delegateIterator.hasNext()) {
                throw new IllegalStateException("Found more than one FactoryBuilderDelegate implementation!");
            }
        } else {
            throw new IllegalStateException("No FactoryBuilderDelegate implementation was found!");
        }
        discordExceptionValidator = factoryBuilderDelegate.createDiscordExceptionValidator();
    }

    private FactoryBuilder() {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates a new discord api factory.
     *
     * @return A new discord api factory.
     */
    public static DiscordApiFactory createDiscordApiFactory() {
        return factoryBuilderDelegate.createDiscordApiFactory();
    }

    /**
     * Creates a new embed factory.
     *
     * @return A new embed factory.
     */
    public static EmbedFactory createEmbedFactory() {
        return factoryBuilderDelegate.createEmbedFactory();
    }

    /**
     * Creates a new message factory.
     *
     * @return A new message factory.
     */
    public static MessageFactory createMessageFactory() {
        return factoryBuilderDelegate.createMessageFactory();
    }

    /**
     * Creates a new permissions factory.
     *
     * @return A new permissions factory.
     */
    public static PermissionsFactory createPermissionsFactory() {
        return factoryBuilderDelegate.createPermissionsFactory();
    }

    /**
     * Creates a new permissions factory initialized with the given permissions.
     *
     * @param permissions The permissions which should be copied.
     * @return A new permissions factory initialized with the given permissions.
     */
    public static PermissionsFactory createPermissionsFactory(Permissions permissions) {
        return factoryBuilderDelegate.createPermissionsFactory(permissions);
    }

    /**
     * Gets the discord exception validator.
     *
     * @return The discord exception validator.
     */
    public static DiscordExceptionValidator getDiscordExceptionValidator() {
        return discordExceptionValidator;
    }

}
