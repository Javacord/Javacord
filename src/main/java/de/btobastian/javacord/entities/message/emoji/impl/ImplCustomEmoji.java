package de.btobastian.javacord.entities.message.emoji.impl;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entities.message.emoji.CustomEmoji;
import org.json.JSONObject;

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
     * Creates a new custom emoji.
     *
     * @param api The discord api instance.
     * @param data The json data of the emoji.
     */
    public ImplCustomEmoji(ImplDiscordApi api, JSONObject data) {
        this.api = api;
        id = Long.parseLong(data.getString("id"));
        name = data.getString("name");
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
}
