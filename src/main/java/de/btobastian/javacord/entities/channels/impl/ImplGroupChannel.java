package de.btobastian.javacord.entities.channels.impl;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.channels.GroupChannel;
import de.btobastian.javacord.utils.cache.ImplMessageCache;
import de.btobastian.javacord.utils.cache.MessageCache;
import de.btobastian.javacord.utils.logging.LoggerUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * The implementation of {@link GroupChannel}.
 */
public class ImplGroupChannel implements GroupChannel {

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
    public ImplGroupChannel(ImplDiscordApi api, JSONObject data) {
        this.api = api;
        JSONArray jsonRecipients = data.getJSONArray("recipients");
        for (int i = 0; i < jsonRecipients.length(); i++) {
            recipients.add(api.getOrCreateUser(jsonRecipients.getJSONObject(i)));
        }
        this.messageCache = new ImplMessageCache(
                api, api.getDefaultMessageCacheCapacity(), api.getDefaultMessageCacheStorageTimeInSeconds());

        id = Long.parseLong(data.getString("id"));
        name = data.has("name") && !data.isNull("name") ? data.getString("name") : null;
        iconId = data.has("icon") && !data.isNull("icon") ? data.getString("icon") : null;

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
    public Optional<URL> getIconUrl() {
        if (iconId == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(new URL("https://cdn.discordapp.com/channel-icons/" + getId() + "/" + iconId + ".png"));
        } catch (MalformedURLException e) {
            logger.warn("Seems like the url of the icon is malformed! Please contact the developer!", e);
            return Optional.empty();
        }
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
    public String toString() {
        return String.format("GroupChannel (id: %s, name: %s)", getId(), getName());
    }

}
