package org.javacord.core.entity.user;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.Javacord;
import org.javacord.api.entity.DiscordClient;
import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.Icon;
import org.javacord.api.entity.activity.Activity;
import org.javacord.api.entity.channel.PrivateChannel;
import org.javacord.api.entity.user.User;
import org.javacord.api.entity.user.UserStatus;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.IconImpl;
import org.javacord.core.entity.channel.PrivateChannelImpl;
import org.javacord.core.listener.user.InternalUserAttachableListenerManager;
import org.javacord.core.util.Cleanupable;
import org.javacord.core.util.logging.LoggerUtil;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The implementation of {@link User}.
 */
public class UserImpl implements User, Cleanupable, InternalUserAttachableListenerManager {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(UserImpl.class);

    /**
     * The discord api instance.
     */
    private final DiscordApiImpl api;

    /**
     * The id of the user.
     */
    private final long id;

    /**
     * The name of the user.
     */
    private volatile String name;

    /**
     * The private channel with the given user.
     */
    private volatile PrivateChannel channel = null;

    /**
     * The avatar hash of the user. Might be <code>null</code>!
     */
    private volatile String avatarHash = null;

    /**
     * The discriminator of the user.
     */
    private volatile String discriminator;

    /**
     * Whether the user is a bot account or not.
     */
    private final boolean bot;

    /**
     * The activity of the user.
     */
    private volatile Activity activity = null;

    /**
     * The status of the user.
     */
    private volatile UserStatus status = UserStatus.OFFLINE;

    /**
     * The status of the user on a given client.
     */
    private final Map<DiscordClient, UserStatus> clientStatus = new ConcurrentHashMap<>();

    /**
     * Creates a new user.
     *
     * @param api The discord api instance.
     * @param data The json data of the user.
     */
    public UserImpl(DiscordApiImpl api, JsonNode data) {
        this.api = api;

        id = Long.parseLong(data.get("id").asText());
        name = data.get("username").asText();
        discriminator = data.get("discriminator").asText();
        if (data.has("avatar") && !data.get("avatar").isNull()) {
            avatarHash = data.get("avatar").asText();
        }
        bot = data.has("bot") && data.get("bot").asBoolean();

        api.addUserToCache(this);
    }

    /**
     * Sets the private channel with the user.
     *
     * @param channel The channel to set.
     */
    public void setChannel(PrivateChannel channel) {
        if (this.channel != channel) {
            if (this.channel != null) {
                ((Cleanupable) this.channel).cleanup();
            }
            this.channel = channel;
        }
    }

    /**
     * Sets the activity of the user.
     *
     * @param activity The activity to set.
     */
    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    /**
     * Sets the status of the user.
     *
     * @param status The status to set.
     */
    public void setStatus(UserStatus status) {
        this.status = status;
    }

    /**
     * Sets the client status of the user.
     *
     * @param client The client.
     * @param status The status to set.
     */
    public void setClientStatus(DiscordClient client, UserStatus status) {
        clientStatus.put(client, status);
    }

    /**
     * Sets the name of the user.
     *
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the discriminator of the user.
     *
     * @param discriminator The discriminator to set.
     */
    public void setDiscriminator(String discriminator) {
        this.discriminator = discriminator;
    }

    /**
     * Gets the avatar hash of the user.
     * Might be <code>null</code>.
     *
     * @return The avatar hash of the user.
     */
    public String getAvatarHash() {
        return avatarHash;
    }

    /**
     * Sets the avatar hash of the user.
     *
     * @param avatarHash The avatar hash to set.
     */
    public void setAvatarHash(String avatarHash) {
        this.avatarHash = avatarHash;
    }

    /**
     * Gets or creates a new private channel.
     *
     * @param data The data of the private channel.
     * @return The private channel for the given data.
     */
    public PrivateChannel getOrCreateChannel(JsonNode data) {
        synchronized (this) {
            if (channel != null) {
                return channel;
            }
            return new PrivateChannelImpl(api, data);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDiscriminator() {
        return discriminator;
    }

    @Override
    public boolean isBot() {
        return bot;
    }

    @Override
    public Optional<Activity> getActivity() {
        return Optional.ofNullable(activity);
    }

    @Override
    public UserStatus getStatus() {
        return status;
    }

    @Override
    public UserStatus getStatusOnClient(DiscordClient client) {
        return clientStatus.getOrDefault(client, UserStatus.OFFLINE);
    }

    /**
     * Gets the avatar for the given details.
     *
     * @param api The discord api instance.
     * @param avatarHash The avatar hash or {@code null} for default avatar.
     * @param discriminator The discriminator if default avatar is wanted.
     * @param userId The user id.
     * @return The avatar for the given details.
     */
    public static Icon getAvatar(DiscordApi api, String avatarHash, String discriminator, long userId) {
        StringBuilder url = new StringBuilder("https://" + Javacord.DISCORD_CDN_DOMAIN + "/");
        if (avatarHash == null) {
            url.append("embed/avatars/")
                    .append(Integer.parseInt(discriminator) % 5)
                    .append(".png");
        } else {
            url.append("avatars/")
                    .append(userId).append('/').append(avatarHash)
                    .append(avatarHash.startsWith("a_") ? ".gif" : ".png");
        }
        try {
            return new IconImpl(api, new URL(url.toString()));
        } catch (MalformedURLException e) {
            logger.warn("Seems like the url of the avatar is malformed! Please contact the developer!", e);
            return null;
        }
    }

    @Override
    public Icon getAvatar() {
        return getAvatar(api, avatarHash, discriminator, id);
    }

    @Override
    public boolean hasDefaultAvatar() {
        return avatarHash == null;
    }

    @Override
    public Optional<PrivateChannel> getPrivateChannel() {
        return Optional.ofNullable(channel);
    }

    @Override
    public CompletableFuture<PrivateChannel> openPrivateChannel() {
        if (channel != null) {
            return CompletableFuture.completedFuture(channel);
        }
        return new RestRequest<PrivateChannel>(api, RestMethod.POST, RestEndpoint.USER_CHANNEL)
                .setBody(JsonNodeFactory.instance.objectNode().put("recipient_id", getIdAsString()))
                .execute(result -> getOrCreateChannel(result.getJsonBody()));
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
    public void cleanup() {
        if (channel != null) {
            ((Cleanupable) channel).cleanup();
        }
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
        return String.format("User (id: %s, name: %s)", getIdAsString(), getName());
    }

}
