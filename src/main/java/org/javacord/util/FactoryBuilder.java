package org.javacord.util;

import org.javacord.DiscordApiBuilderDelegate;
import org.javacord.entity.message.MessageBuilderDelegate;
import org.javacord.entity.message.embed.EmbedBuilderDelegate;
import org.javacord.entity.permission.Permissions;
import org.javacord.entity.permission.PermissionsBuilderDelegate;

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
    }

    private FactoryBuilder() {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates a new discord api builder delegate.
     *
     * @return A new discord api builder delegate.
     */
    public static DiscordApiBuilderDelegate createDiscordApiBuilderDelegate() {
        return factoryBuilderDelegate.createDiscordApiBuilderDelegate();
    }

    /**
     * Creates a new embed builder delegate.
     *
     * @return A new embed builder delegate.
     */
    public static EmbedBuilderDelegate createEmbedBuilderDelegate() {
        return factoryBuilderDelegate.createEmbedBuilderDelegate();
    }

    /**
     * Creates a new message builder delegate.
     *
     * @return A new message builder delegate.
     */
    public static MessageBuilderDelegate createMessageBuilderDelegate() {
        return factoryBuilderDelegate.createMessageBuilderDelegate();
    }

    /**
     * Creates a new permissions builder delegate.
     *
     * @return A new permissions builder delegate.
     */
    public static PermissionsBuilderDelegate createPermissionsBuilderDelegate() {
        return factoryBuilderDelegate.createPermissionsBuilderDelegate();
    }

    /**
     * Creates a new permissions builder delegate initialized with the given permissions.
     *
     * @param permissions The permissions which should be copied.
     * @return A new permissions builder delegate initialized with the given permissions.
     */
    public static PermissionsBuilderDelegate createPermissionsBuilderDelegate(Permissions permissions) {
        return factoryBuilderDelegate.createPermissionsBuilderDelegate(permissions);
    }

}
