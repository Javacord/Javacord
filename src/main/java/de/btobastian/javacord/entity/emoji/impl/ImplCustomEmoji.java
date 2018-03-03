package de.btobastian.javacord.entity.emoji.impl;

import com.fasterxml.jackson.databind.JsonNode;
import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entity.DiscordEntity;
import de.btobastian.javacord.entity.Icon;
import de.btobastian.javacord.entity.emoji.CustomEmoji;
import de.btobastian.javacord.entity.impl.ImplIcon;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

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
    protected String name;

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
        this.api = api;
        id = data.get("id").asLong();
        name = data.get("name").asText();
        animated = data.get("animated").asBoolean();
    }

    /**
     * Creates a new custom emoji.
     *
     * @param api The discord api instance.
     * @param id The id of the emoji.
     * @param name The name of the emoji.
     * @param animated Whether the emoji is animated or not.
     */
    public ImplCustomEmoji(ImplDiscordApi api, long id, String name, boolean animated) {
        this.api = api;
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
    public Icon getImage() {
        String urlString = "https://cdn.discordapp.com/emojis/" + getIdAsString() + (isAnimated() ? ".gif" : ".png");
        try {
            return new ImplIcon(getApi(), new URL(urlString));
        } catch (MalformedURLException e) {
            logger.warn("Seems like the url of the avatar is malformed! Please contact the developer!", e);
            return null;
        }
    }

    @Override
    public boolean isAnimated() {
        return animated;
    }

    @Override
    public boolean equals(Object o) {
        return (this == o)
               || !((o == null)
                    || (getClass() != o.getClass())
                    || (getId() != ((DiscordEntity) o).getId()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return String.format("CustomEmoji (id: %s, name: %s, animated: %b)", getIdAsString(), getName(), isAnimated());
    }

}
