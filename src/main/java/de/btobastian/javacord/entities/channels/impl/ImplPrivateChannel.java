package de.btobastian.javacord.entities.channels.impl;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.channels.PrivateChannel;
import de.btobastian.javacord.entities.impl.ImplUser;
import de.btobastian.javacord.utils.cache.ImplMessageCache;
import de.btobastian.javacord.utils.cache.MessageCache;

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
	 * List of users connected to this voice-channel.
	 */
	private final List<User> connectedUsers = new ArrayList<>();

    /**
     * Creates a new private channel.
     *
     * @param api The discord api instance.
     * @param data The json data of the channel.
     */
    public ImplPrivateChannel(ImplDiscordApi api, JsonNode data) {
        this.api = api;
        this.recipient = (ImplUser) api.getOrCreateUser(data.get("recipients").get(0));
        this.messageCache = new ImplMessageCache(
                api, api.getDefaultMessageCacheCapacity(), api.getDefaultMessageCacheStorageTimeInSeconds());

        id = Long.parseLong(data.get("id").asText());
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
	public List<User> getConnectedUsers() {
		return this.connectedUsers;
	}

    @Override
    public String toString() {
        return String.format("PrivateChannel (id: %s, recipient: %s)", getId(), getRecipient());
    }
}
