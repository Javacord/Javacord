package org.javacord.core.interaction;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.InteractionType;
import org.javacord.api.interaction.MessageInteraction;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.entity.user.MemberImpl;
import org.javacord.core.entity.user.UserImpl;

public class MessageInteractionImpl implements MessageInteraction {
    private final Message message;

    private final long id;

    private final InteractionType type;
    private final String name;

    private final UserImpl user;

    /**
     * Class constructor.
     *
     * @param message  The message that the interaction originated from.
     * @param jsonData The json data of the interaction context.
     */
    public MessageInteractionImpl(Message message, JsonNode jsonData) {
        this.message = message;

        id = jsonData.get("id").asLong();

        type = InteractionType.fromValue(jsonData.get("type").asInt());

        name = jsonData.get("name").asText();

        UserImpl userTemp = new UserImpl(
                (DiscordApiImpl) message.getApi(),
                jsonData.get("user"),
                (MemberImpl) null,
                null
        );
        if (jsonData.hasNonNull("member")) {
            MemberImpl member = new MemberImpl(
                    (DiscordApiImpl) message.getApi(),
                    (ServerImpl) message.getServer().orElseThrow(AssertionError::new),
                    jsonData.get("member"),
                    userTemp
            );
            userTemp = (UserImpl) member.getUser();
        }
        user = userTemp;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public Message getMessage() {
        return message;
    }

    @Override
    public InteractionType getType() {
        return type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public User getUser() {
        return user;
    }
}
