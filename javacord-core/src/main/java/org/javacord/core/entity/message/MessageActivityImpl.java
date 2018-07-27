package org.javacord.core.entity.message;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageActivity;
import org.javacord.api.entity.message.MessageActivityType;

import java.util.Optional;

/**
 * The implementation of {@link MessageActivity}.
 */
public class MessageActivityImpl implements MessageActivity {

    private final MessageActivityType type;
    private final String partyId;
    private final Message message;

    /**
     * Creates a new message activity object.
     *
     * @param message The message of the activity.
     * @param data The json data of the activity.
     */
    public MessageActivityImpl(Message message, JsonNode data) {
        type = MessageActivityType.getMessageActivityTypeById(data.get("type").asInt());
        partyId = data.has("party_id") ? data.get("party_id").asText() : null;
        this.message = message;
    }

    @Override
    public MessageActivityType getType() {
        return type;
    }

    @Override
    public Optional<String> getPartyId() {
        return Optional.ofNullable(partyId);
    }

    @Override
    public Message getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return String.format("MessageActivity (type: %s, partyId: %s, message: %s)",
                getType(),
                getPartyId().orElse("none"),
                getMessage());
    }
}
