package de.btobastian.javacord.entities.channels.impl;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.channels.PrivateChannel;
import de.btobastian.javacord.entities.impl.ImplUser;
import de.btobastian.javacord.utils.cache.ImplMessageCache;
import de.btobastian.javacord.utils.cache.MessageCache;
import org.json.JSONObject;

/**
 * The implementation of {@link PrivateChannel}.
 */
public class ImplPrivateChannel implements PrivateChannel {

    /**
     * The discord api instance.
     */
    private final ImplDiscordApi api;

    /**
     * The id of the channel.
     */
    private final long id;

    /**
     * The recipient of the private channel.
     */
    private final ImplUser recipient;

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
    public ImplPrivateChannel(ImplDiscordApi api, JSONObject data) {
        this.api = api;
        this.recipient = (ImplUser) api.getOrCreateUser(data.getJSONArray("recipients").getJSONObject(0));
        this.messageCache = new ImplMessageCache(
                api, api.getDefaultMessageCacheCapacity(), api.getDefaultMessageCacheStorageTimeInSeconds());

        id = Long.parseLong(data.getString("id"));
        recipient.setChannel(this);
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
    public User getRecipient() {
        return recipient;
    }

    @Override
    public MessageCache getMessageCache() {
        return messageCache;
    }


    @Override
    public String toString() {
        return String.format("PrivateChannel (id: %s, recipient: %s)", getId(), getRecipient());
    }
}
