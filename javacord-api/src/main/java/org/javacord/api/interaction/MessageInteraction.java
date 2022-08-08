package org.javacord.api.interaction;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.member.Member;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.user.User;
import java.util.Optional;

public interface MessageInteraction extends DiscordEntity {
    @Override
    default DiscordApi getApi() {
        return getMessage().getApi();
    }

    /**
     * Gets the original message that the interaction came from.
     *
     * @return The original message.
     */
    Message getMessage();

    /**
     * Gets the type of the interaction.
     *
     * @return The type of the interaction.
     */
    InteractionType getType();

    /**
     * Gets the name of the application command of the interaction.
     *
     * @return The application command name.
     */
    String getName();

    /**
     * Gets the user who invoked the interaction.
     *
     * @return The user invokee.
     */
    User getUser();

    /**
     * Gets the member who invoked the interaction, if it's inside a server.
     *
     * @return The user invokee.
     */
    Optional<Member> getMember();
}
