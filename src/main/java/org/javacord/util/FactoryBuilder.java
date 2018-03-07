package org.javacord.util;

import org.javacord.DiscordApiFactory;
import org.javacord.entity.message.MessageFactory;
import org.javacord.entity.message.embed.EmbedFactory;

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

}
