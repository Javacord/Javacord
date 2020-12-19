package org.javacord.api.command;

import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import java.util.Optional;

public interface Interaction extends DiscordEntity {

    /**
     * Gets the id of the application that this interaction is for.
     *
     * @return The id of the application.
     */
    long getApplicationId();

    /**
     * Gets the type of the interaction.
     *
     * @return The type of the interaction.
     */
    InteractionType getType();

    /**
     * Gets the data of the interaction.
     *
     * @return The data of the interaction.
     */
    Optional<ApplicationCommandInteractionData> getData();

    /**
     * Gets the server that this interaction was sent from.
     *
     * @return The server.
     */
    Optional<Server> getServer();

    /**
     * Gets the channel that this interaction was sent from.
     *
     * @return The channel.
     */
    Optional<TextChannel> getChannel();

    /**
     * Gets the invoking user.
     *
     * @return The invoking user.
     */
    User getUser();

    /**
     * Gets the continuation token for responding to the interaction.
     *
     * @return The token.
     */
    String getToken();

    /**
     * Gets the version.
     *
     * @return The version.
     */
    int getVersion();
}
