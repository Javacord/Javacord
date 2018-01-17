package de.btobastian.javacord.entities.message.emoji.impl;

import com.fasterxml.jackson.databind.JsonNode;
import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.message.emoji.CustomEmoji;

import java.util.Objects;
import java.util.Optional;

/**
 * The implementation of {@link CustomEmoji}.
 */
public class ImplCustomEmoji implements CustomEmoji {

    /**
     * The discord api instance.
     */
    private final ImplDiscordApi api;

    /**
     * The id of the emoji.
     */
    private final long id;

    /**
     * The name of the emoji.
     */
    private String name;

    /**
     * The server of the emoji. Might be <code>null</code>!
     */
    private Server server;

    /**
     * Whether the emoji is animated or not.
     */
    private boolean animated;

    /**
     * Creates a new custom emoji.
     *
     * @param api The discord api instance.
     * @param data The json data of the emoji.
     */
    public ImplCustomEmoji(ImplDiscordApi api, JsonNode data) {
        this(api, null, data);
    }

    /**
     * Creates a new custom emoji.
     *
     * @param api The discord api instance.
     * @param server The server of the emoji.
     * @param data The json data of the emoji.
     */
    public ImplCustomEmoji(ImplDiscordApi api, Server server, JsonNode data) {
        this.api = api;
        this.server = server;
        id = data.get("id").asLong();
        updateFromJson(data);
    }

    /**
     * Update the custom emoji with updated information.
     *
     * @param data The json data of the emoji
     * @return whether anything was obsolete and got updated
     * @throws IllegalArgumentException if the id does not match
     */
    public boolean updateFromJson(JsonNode data) {
        if (id != data.get("id").asLong()) {
            throw new IllegalArgumentException("id does not match");
        }

        boolean result = false;

        String name = data.get("name").asText();
        if (!Objects.equals(this.name, name)) {
            this.name = name;
            result = true;
        }

        boolean animated = data.has("animated") && data.get("animated").asBoolean(false);
        if (!this.animated == animated) {
            this.animated = animated;
            result = true;
        }

        return result;
    }

    /**
     * Creates a new custom emoji.
     *
     * @param api The discord api instance.
     * @param server The server of the emoji.
     * @param id The id of the emoji.
     * @param name The name of the emoji.
     * @param animated Whether the emoji is animated or not.
     */
    public ImplCustomEmoji(ImplDiscordApi api, Server server, long id, String name, boolean animated) {
        this.api = api;
        this.server = server;
        this.id = id;
        this.name = name;
        this.animated = animated;
    }

    @Override
    public DiscordApi getApi() {
        return api;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Optional<Server> getServer() {
        return Optional.ofNullable(server);
    }

    @Override
    public boolean isAnimated() {
        return animated;
    }

    @Override
    public String toString() {
        return String.format("CustomEmoji (id: %s, name: %s, animated: %b)", getId(), getName(), isAnimated());
    }
}
