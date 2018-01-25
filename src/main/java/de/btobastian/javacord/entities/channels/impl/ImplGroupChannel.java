package de.btobastian.javacord.entities.channels.impl;

import com.fasterxml.jackson.databind.JsonNode;
import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entities.Icon;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.channels.GroupChannel;
import de.btobastian.javacord.entities.impl.ImplIcon;
import de.btobastian.javacord.utils.Cleanupable;
import de.btobastian.javacord.utils.cache.ImplMessageCache;
import de.btobastian.javacord.utils.cache.MessageCache;
import de.btobastian.javacord.utils.logging.LoggerUtil;
import org.slf4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * The implementation of {@link GroupChannel}.
 */
public class ImplGroupChannel implements GroupChannel, Cleanupable {

    /**
     * The logger of this class.
     */
    Logger logger = LoggerUtil.getLogger(ImplGroupChannel.class);

    /**
     * The discord api instance.
     */
    private final ImplDiscordApi api;

    /**
     * The id of the channel.
     */
    private final long id;

    /**
     * The name of the channel.
     */
    private String name;

    /**
     * The icon id of the channel.
     */
    private String iconId;

    /**
     * The recipients of the group channel.
     */
    private final List<User> recipients = new ArrayList<>();

    /**
     * The message cache of the private channel.
     */
    private final ImplMessageCache messageCache;

    /**
     * Creates a new private channel.
     *
     * @param api The discord api instance.
     * @param data The json data of the channel.
     */
    public ImplGroupChannel(ImplDiscordApi api, JsonNode data) {
        this.api = api;

        for (JsonNode recipientJson : data.get("recipients")) {
            recipients.add(api.getOrCreateUser(recipientJson));
        }

        this.messageCache = new ImplMessageCache(
                api, api.getDefaultMessageCacheCapacity(), api.getDefaultMessageCacheStorageTimeInSeconds());

        id = Long.parseLong(data.get("id").asText());
        name = data.has("name") && !data.get("name").isNull() ? data.get("name").asText() : null;
        iconId = data.has("icon") && !data.get("icon").isNull() ? data.get("icon").asText() : null;

        api.addGroupChannelToCache(this);
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
    public MessageCache getMessageCache() {
        return messageCache;
    }

    @Override
    public Collection<User> getMembers() {
        return Collections.unmodifiableCollection(recipients);
    }

    @Override
    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    @Override
    public Optional<Icon> getIcon() {
        if (iconId == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(new ImplIcon(
                    getApi(), new URL("https://cdn.discordapp.com/channel-icons/" + getId() + "/" + iconId + ".png")));
        } catch (MalformedURLException e) {
            logger.warn("Seems like the url of the icon is malformed! Please contact the developer!", e);
            return Optional.empty();
        }
    }

    @Override
    public void cleanup() {
        messageCache.cleanup();
    }

    @Override
    public String toString() {
        return String.format("GroupChannel (id: %s, name: %s)", getId(), getName());
    }

}
