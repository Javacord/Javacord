package org.javacord.util;

import org.javacord.DiscordApiFactory;
import org.javacord.entity.message.MessageFactory;
import org.javacord.entity.message.embed.EmbedFactory;

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

}
