package org.javacord.core.entity.channel;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.Javacord;
import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.Icon;
import org.javacord.api.entity.channel.GroupChannel;
import org.javacord.api.entity.user.User;
import org.javacord.api.util.cache.MessageCache;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.IconImpl;
import org.javacord.core.listener.channel.group.InternalGroupChannelAttachableListenerManager;
import org.javacord.core.util.Cleanupable;
import org.javacord.core.util.cache.MessageCacheImpl;
import org.javacord.core.util.logging.LoggerUtil;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * The implementation of {@link GroupChannel}.
 */
public class GroupChannelImpl implements GroupChannel, Cleanupable, InternalTextChannel,
                                         InternalGroupChannelAttachableListenerManager {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(GroupChannelImpl.class);

    /**
     * The discord api instance.
     */
    private final DiscordApiImpl api;

    /**
     * The id of the channel.
     */
    private final long id;

    /**
     * The name of the channel.
     */
    private volatile String name;

    /**
     * The icon id of the channel.
     */
    private volatile String iconId;

    /**
     * The recipients of the group channel.
     */
    private final List<User> recipients = new ArrayList<>();

    /**
     * The message cache of the private channel.
     */
    private final MessageCacheImpl messageCache;

    /**
     * Creates a new private channel.
     *
     * @param api The discord api instance.
     * @param data The json data of the channel.
     */
    public GroupChannelImpl(DiscordApiImpl api, JsonNode data) {
        this.api = api;

        for (JsonNode recipientJson : data.get("recipients")) {
            recipients.add(api.getOrCreateUser(recipientJson));
        }

        messageCache = new MessageCacheImpl(
                api, api.getDefaultMessageCacheCapacity(), api.getDefaultMessageCacheStorageTimeInSeconds(),
                api.isDefaultAutomaticMessageCacheCleanupEnabled());

        id = Long.parseLong(data.get("id").asText());
        name = data.has("name") && !data.get("name").isNull() ? data.get("name").asText() : null;
        iconId = data.has("icon") && !data.get("icon").isNull() ? data.get("icon").asText() : null;

        api.addChannelToCache(this);
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

    /**
     * Sets the name of the channel.
     *
     * @param name The new name of the channel.
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Optional<Icon> getIcon() {
        if (iconId == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(new IconImpl(
                    getApi(),
                    new URL("https://" + Javacord.DISCORD_CDN_DOMAIN
                            + "/channel-icons/" + getIdAsString() + "/" + iconId + ".png")));
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
        return String.format("GroupChannel (id: %s, name: %s)", getIdAsString(), getName());
    }

}
