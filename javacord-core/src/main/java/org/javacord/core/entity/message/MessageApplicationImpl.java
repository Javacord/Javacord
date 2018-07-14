package org.javacord.core.entity.message;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageApplication;

public class MessageApplicationImpl implements MessageApplication {
    private final long id;
    private final String coverImageId;
    private final String description;
    private final String iconId;
    private final String name;
    private final Message message;

    /**
     * Creates a new application object.
     *
     * @param message The message that the application belongs to.
     * @param data The json data of the application.
     */
    public MessageApplicationImpl(Message message, JsonNode data) {
        this.message = message;
        this.id = data.get("id").asLong();
        this.name = data.get("name").asText();
        this.description = data.get("description").asText();
        this.iconId = data.get("icon").asText();
        this.coverImageId = data.get("cover_image").asText();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getIcon() {
        return iconId;
    }

    @Override
    public String getCoverImage() {
        return coverImageId;
    }

    @Override
    public Message getMessage() {
        return message;
    }

    @Override
    public long getApplicationId() {
        return id;
    }
}
