package org.javacord.core.entity.user;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.javacord.api.DiscordApi;
import org.javacord.api.Javacord;
import org.javacord.api.entity.DiscordClient;
import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.Icon;
import org.javacord.api.entity.activity.Activity;
import org.javacord.api.entity.channel.PrivateChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.entity.user.UserFlag;
import org.javacord.api.entity.user.UserStatus;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.IconImpl;
import org.javacord.core.entity.channel.PrivateChannelImpl;
import org.javacord.core.listener.user.InternalUserAttachableListenerManager;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * Maps a user object.
 *
 * @see <a href="https://discord.com/developers/docs/resources/user#user-object">Discord Docs</a>
 */
public class UserImpl implements User, InternalUserAttachableListenerManager {

    private static final int DEFAULT_AVATAR_SIZE = 1024;

    private final DiscordApiImpl api;
    private final Long id;
    private final String name;
    private final String discriminator;
    private final String avatarHash;
    private final EnumSet<UserFlag> userFlags = EnumSet.noneOf(UserFlag.class);
    private final boolean bot;

    /**
     * Creates a new user instance.
     *
     * @param api  A discord api instance.
     * @param data The json data.
     */
    public UserImpl(DiscordApiImpl api, JsonNode data) {
        this.api = api;
        id = data.get("id").asLong();
        name = data.get("username").asText();
        discriminator = data.get("discriminator").asText();
        if (data.hasNonNull("avatar")) {
            avatarHash = data.get("avatar").asText();
        } else {
            avatarHash = null;
        }
        if (data.has("public_flags")) {
            int flags = data.get("public_flags").asInt();
            for (UserFlag flag : UserFlag.values()) {
                if ((flag.asInt() & flags) == flag.asInt()) {
                    userFlags.add(flag);
                }
            }
        }

        bot = data.hasNonNull("bot") && data.get("bot").asBoolean();
    }

    private UserImpl(DiscordApiImpl api, Long id, String name, String discriminator, String avatarHash, boolean bot) {
        this.api = api;
        this.id = id;
        this.name = name;
        this.discriminator = discriminator;
        this.avatarHash = avatarHash;
        this.bot = bot;
    }

    /**
     * Creates a new user instance with the updates data from the given partial json data.
     *
     * @param partialUserJson The user json data.
     * @return The new user.
     */
    public UserImpl replacePartialUserData(JsonNode partialUserJson) {
        if (partialUserJson.get("id").asLong() != id) {
            throw new IllegalArgumentException("Ids of user do not match");
        }
        String name = this.name;
        if (partialUserJson.hasNonNull("username")) {
            name = partialUserJson.get("username").asText();
        }

        String discriminator = this.discriminator;
        if (partialUserJson.hasNonNull("discriminator")) {
            discriminator = partialUserJson.get("discriminator").asText();
        }

        String avatarHash = this.avatarHash;
        if (partialUserJson.hasNonNull("avatar")) {
            avatarHash = partialUserJson.get("avatar").asText();
        }

        return new UserImpl(api, id, name, discriminator, avatarHash, bot);
    }

    @Override
    public Optional<String> getAvatarHash() {
        return Optional.ofNullable(avatarHash);
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
    public String getDiscriminator() {
        return discriminator;
    }

    @Override
    public EnumSet<UserFlag> getUserFlags() {
        return userFlags;
    }

    /**
     * Gets the avatar for the given details.
     *
     * @param api           The discord api instance.
     * @param avatarHash    The avatar hash or {@code null} for default avatar.
     * @param discriminator The discriminator if default avatar is wanted.
     * @param userId        The user id.
     * @return The avatar for the given details.
     */
    public static Icon getAvatar(DiscordApi api, String avatarHash, String discriminator, long userId) {
        return getAvatar(api, avatarHash, discriminator, userId, DEFAULT_AVATAR_SIZE);
    }

    /**
     * Gets the avatar for the given details.
     *
     * @param api           The discord api instance.
     * @param avatarHash    The avatar hash or {@code null} for default avatar.
     * @param discriminator The discriminator if default avatar is wanted.
     * @param userId        The user id.
     * @param size          The size of the image. Must be any power of 2 between 16 and 4096.
     * @return The avatar for the given details.
     */
    public static Icon getAvatar(DiscordApi api, String avatarHash, String discriminator, long userId, int size) {
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
        url.append("?size=").append(size);
        try {
            return new IconImpl(api, new URL(url.toString()));
        } catch (MalformedURLException e) {
            throw new AssertionError("Found a malformed avatar url. Please update to the latest Javacord "
                    + "version or create an issue on GitHub if you are already using the latest one.");
        }
    }

    @Override
    public Icon getAvatar() {
        return getAvatar(api, avatarHash, discriminator, getId());
    }

    @Override
    public Icon getAvatar(int size) {
        return getAvatar(api, avatarHash, discriminator, getId(), size);
    }

    @Override
    public boolean hasDefaultAvatar() {
        return avatarHash == null;
    }

    @Override
    public Set<Server> getMutualServers() {
        return api.isUserCacheEnabled()
                ? api.getEntityCache().get().getMemberCache().getServers(getId())
                : Collections.emptySet();
    }

    @Override
    public boolean isBot() {
        return bot;
    }

    @Override
    public Set<Activity> getActivities() {
        return api.getEntityCache().get().getUserPresenceCache().getPresenceByUserId(getId())
                .map(UserPresence::getActivities).orElse(Collections.emptySet());
    }

    @Override
    public UserStatus getStatus() {
        return api.getEntityCache().get().getUserPresenceCache().getPresenceByUserId(getId())
                .map(UserPresence::getStatus)
                .orElse(UserStatus.OFFLINE);
    }

    @Override
    public UserStatus getStatusOnClient(DiscordClient client) {
        return api.getEntityCache().get().getUserPresenceCache().getPresenceByUserId(getId())
                .map(UserPresence::getClientStatus)
                .map(clientStatusMap -> clientStatusMap.getOrElse(client, UserStatus.OFFLINE))
                .orElse(UserStatus.OFFLINE);
    }

    @Override
    public Optional<PrivateChannel> getPrivateChannel() {
        return api.getEntityCache().get().getChannelCache().getPrivateChannelByUserId(getId());
    }

    @Override
    public CompletableFuture<PrivateChannel> openPrivateChannel() {
        return getPrivateChannel()
                .map(CompletableFuture::completedFuture)
                .orElseGet(() ->
                        new RestRequest<PrivateChannel>(api, RestMethod.POST, RestEndpoint.USER_CHANNEL)
                                .setBody(JsonNodeFactory.instance.objectNode().put("recipient_id", getIdAsString()))
                                .execute(result -> new PrivateChannelImpl(api, result.getJsonBody()))
                );
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
