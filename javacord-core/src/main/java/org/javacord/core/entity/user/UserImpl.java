package org.javacord.core.entity.user;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.Icon;
import org.javacord.api.entity.activity.Activity;
import org.javacord.api.entity.channel.PrivateChannel;
import org.javacord.api.entity.user.User;
import org.javacord.api.entity.user.UserStatus;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.channel.group.GroupChannelChangeNameListener;
import org.javacord.api.listener.channel.group.GroupChannelCreateListener;
import org.javacord.api.listener.channel.group.GroupChannelDeleteListener;
import org.javacord.api.listener.channel.server.ServerChannelChangeOverwrittenPermissionsListener;
import org.javacord.api.listener.channel.server.voice.ServerVoiceChannelMemberJoinListener;
import org.javacord.api.listener.channel.server.voice.ServerVoiceChannelMemberLeaveListener;
import org.javacord.api.listener.channel.user.PrivateChannelCreateListener;
import org.javacord.api.listener.channel.user.PrivateChannelDeleteListener;
import org.javacord.api.listener.message.MessageCreateListener;
import org.javacord.api.listener.message.reaction.ReactionAddListener;
import org.javacord.api.listener.message.reaction.ReactionRemoveListener;
import org.javacord.api.listener.server.member.ServerMemberBanListener;
import org.javacord.api.listener.server.member.ServerMemberJoinListener;
import org.javacord.api.listener.server.member.ServerMemberLeaveListener;
import org.javacord.api.listener.server.member.ServerMemberUnbanListener;
import org.javacord.api.listener.server.role.UserRoleAddListener;
import org.javacord.api.listener.server.role.UserRoleRemoveListener;
import org.javacord.api.listener.user.UserAttachableListener;
import org.javacord.api.listener.user.UserChangeActivityListener;
import org.javacord.api.listener.user.UserChangeAvatarListener;
import org.javacord.api.listener.user.UserChangeDiscriminatorListener;
import org.javacord.api.listener.user.UserChangeMutedListener;
import org.javacord.api.listener.user.UserChangeNameListener;
import org.javacord.api.listener.user.UserChangeNicknameListener;
import org.javacord.api.listener.user.UserChangeSelfDeafenedListener;
import org.javacord.api.listener.user.UserChangeSelfMutedListener;
import org.javacord.api.listener.user.UserChangeStatusListener;
import org.javacord.api.listener.user.UserStartTypingListener;
import org.javacord.api.util.event.ListenerManager;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.IconImpl;
import org.javacord.core.entity.channel.PrivateChannelImpl;
import org.javacord.core.util.ClassHelper;
import org.javacord.core.util.Cleanupable;
import org.javacord.core.util.logging.LoggerUtil;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;
import org.slf4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * The implementation of {@link User}.
 */
public class UserImpl implements User, Cleanupable {

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
        StringBuilder url = new StringBuilder("https://cdn.discordapp.com/");
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
    public ListenerManager<PrivateChannelCreateListener> addPrivateChannelCreateListener(
            PrivateChannelCreateListener listener) {
        return ((DiscordApiImpl) getApi()).addObjectListener(
                User.class, getId(), PrivateChannelCreateListener.class, listener);
    }

    @Override
    public List<PrivateChannelCreateListener> getPrivateChannelCreateListeners() {
        return ((DiscordApiImpl) getApi()).getObjectListeners(User.class, getId(), PrivateChannelCreateListener.class);
    }

    @Override
    public ListenerManager<PrivateChannelDeleteListener> addPrivateChannelDeleteListener(
            PrivateChannelDeleteListener listener) {
        return ((DiscordApiImpl) getApi()).addObjectListener(
                User.class, getId(), PrivateChannelDeleteListener.class, listener);
    }

    @Override
    public List<PrivateChannelDeleteListener> getPrivateChannelDeleteListeners() {
        return ((DiscordApiImpl) getApi()).getObjectListeners(User.class, getId(), PrivateChannelDeleteListener.class);
    }

    @Override
    public ListenerManager<GroupChannelCreateListener> addGroupChannelCreateListener(
            GroupChannelCreateListener listener) {
        return ((DiscordApiImpl) getApi()).addObjectListener(
                User.class, getId(), GroupChannelCreateListener.class, listener);
    }

    @Override
    public List<GroupChannelCreateListener> getGroupChannelCreateListeners() {
        return ((DiscordApiImpl) getApi()).getObjectListeners(User.class, getId(), GroupChannelCreateListener.class);
    }

    @Override
    public ListenerManager<GroupChannelChangeNameListener> addGroupChannelChangeNameListener(
            GroupChannelChangeNameListener listener) {
        return ((DiscordApiImpl) getApi()).addObjectListener(
                User.class, getId(), GroupChannelChangeNameListener.class, listener);
    }

    @Override
    public List<GroupChannelChangeNameListener> getGroupChannelChangeNameListeners() {
        return ((DiscordApiImpl) getApi()).getObjectListeners(
                User.class, getId(), GroupChannelChangeNameListener.class);
    }

    @Override
    public ListenerManager<GroupChannelDeleteListener> addGroupChannelDeleteListener(
            GroupChannelDeleteListener listener) {
        return ((DiscordApiImpl) getApi()).addObjectListener(
                User.class, getId(), GroupChannelDeleteListener.class, listener);
    }

    @Override
    public List<GroupChannelDeleteListener> getGroupChannelDeleteListeners() {
        return ((DiscordApiImpl) getApi()).getObjectListeners(User.class, getId(), GroupChannelDeleteListener.class);
    }

    @Override
    public ListenerManager<MessageCreateListener> addMessageCreateListener(MessageCreateListener listener) {
        return ((DiscordApiImpl) getApi())
                .addObjectListener(User.class, getId(), MessageCreateListener.class, listener);
    }

    @Override
    public List<MessageCreateListener> getMessageCreateListeners() {
        return ((DiscordApiImpl) getApi()).getObjectListeners(User.class, getId(), MessageCreateListener.class);
    }

    @Override
    public ListenerManager<UserStartTypingListener> addUserStartTypingListener(UserStartTypingListener listener) {
        return ((DiscordApiImpl) getApi())
                .addObjectListener(User.class, getId(), UserStartTypingListener.class, listener);
    }

    @Override
    public List<UserStartTypingListener> getUserStartTypingListeners() {
        return ((DiscordApiImpl) getApi()).getObjectListeners(User.class, getId(), UserStartTypingListener.class);
    }

    @Override
    public ListenerManager<ReactionAddListener> addReactionAddListener(ReactionAddListener listener) {
        return ((DiscordApiImpl) getApi()).addObjectListener(User.class, getId(), ReactionAddListener.class, listener);
    }

    @Override
    public List<ReactionAddListener> getReactionAddListeners() {
        return ((DiscordApiImpl) getApi()).getObjectListeners(User.class, getId(), ReactionAddListener.class);
    }

    @Override
    public ListenerManager<ReactionRemoveListener> addReactionRemoveListener(ReactionRemoveListener listener) {
        return ((DiscordApiImpl) getApi())
                .addObjectListener(User.class, getId(), ReactionRemoveListener.class, listener);
    }

    @Override
    public List<ReactionRemoveListener> getReactionRemoveListeners() {
        return ((DiscordApiImpl) getApi()).getObjectListeners(User.class, getId(), ReactionRemoveListener.class);
    }

    @Override
    public ListenerManager<ServerMemberJoinListener> addServerMemberJoinListener(ServerMemberJoinListener listener) {
        return ((DiscordApiImpl) getApi())
                .addObjectListener(User.class, getId(), ServerMemberJoinListener.class, listener);
    }

    @Override
    public List<ServerMemberJoinListener> getServerMemberJoinListeners() {
        return ((DiscordApiImpl) getApi()).getObjectListeners(User.class, getId(), ServerMemberJoinListener.class);
    }

    @Override
    public ListenerManager<ServerMemberLeaveListener> addServerMemberLeaveListener(
            ServerMemberLeaveListener listener) {
        return ((DiscordApiImpl) getApi())
                .addObjectListener(User.class, getId(), ServerMemberLeaveListener.class, listener);
    }

    @Override
    public List<ServerMemberLeaveListener> getServerMemberLeaveListeners() {
        return ((DiscordApiImpl) getApi()).getObjectListeners(User.class, getId(), ServerMemberLeaveListener.class);
    }

    @Override
    public ListenerManager<ServerMemberBanListener> addServerMemberBanListener(ServerMemberBanListener listener) {
        return ((DiscordApiImpl) getApi())
                .addObjectListener(User.class, getId(), ServerMemberBanListener.class, listener);
    }

    @Override
    public List<ServerMemberBanListener> getServerMemberBanListeners() {
        return ((DiscordApiImpl) getApi()).getObjectListeners(User.class, getId(), ServerMemberBanListener.class);
    }

    @Override
    public ListenerManager<ServerMemberUnbanListener> addServerMemberUnbanListener(ServerMemberUnbanListener listener) {
        return ((DiscordApiImpl) getApi())
                .addObjectListener(User.class, getId(), ServerMemberUnbanListener.class, listener);
    }

    @Override
    public List<ServerMemberUnbanListener> getServerMemberUnbanListeners() {
        return ((DiscordApiImpl) getApi()).getObjectListeners(User.class, getId(), ServerMemberUnbanListener.class);
    }

    @Override
    public ListenerManager<UserChangeActivityListener>
            addUserChangeActivityListener(UserChangeActivityListener listener) {
        return ((DiscordApiImpl) getApi())
                .addObjectListener(User.class, getId(), UserChangeActivityListener.class, listener);
    }

    @Override
    public List<UserChangeActivityListener> getUserChangeActivityListeners() {
        return ((DiscordApiImpl) getApi()).getObjectListeners(User.class, getId(), UserChangeActivityListener.class);
    }

    @Override
    public ListenerManager<UserChangeStatusListener> addUserChangeStatusListener(UserChangeStatusListener listener) {
        return ((DiscordApiImpl) getApi())
                .addObjectListener(User.class, getId(), UserChangeStatusListener.class, listener);
    }

    @Override
    public List<UserChangeStatusListener> getUserChangeStatusListeners() {
        return ((DiscordApiImpl) getApi()).getObjectListeners(User.class, getId(), UserChangeStatusListener.class);
    }

    @Override
    public ListenerManager<ServerChannelChangeOverwrittenPermissionsListener>
            addServerChannelChangeOverwrittenPermissionsListener(
                    ServerChannelChangeOverwrittenPermissionsListener listener) {
        return ((DiscordApiImpl) getApi()).addObjectListener(
                User.class, getId(), ServerChannelChangeOverwrittenPermissionsListener.class, listener);
    }

    @Override
    public List<ServerChannelChangeOverwrittenPermissionsListener>
            getServerChannelChangeOverwrittenPermissionsListeners() {
        return ((DiscordApiImpl) getApi()).getObjectListeners(
                User.class, getId(), ServerChannelChangeOverwrittenPermissionsListener.class);
    }

    @Override
    public ListenerManager<UserChangeNicknameListener> addUserChangeNicknameListener(
            UserChangeNicknameListener listener) {
        return ((DiscordApiImpl) getApi())
                .addObjectListener(User.class, getId(), UserChangeNicknameListener.class, listener);
    }

    @Override
    public List<UserChangeNicknameListener> getUserChangeNicknameListeners() {
        return ((DiscordApiImpl) getApi()).getObjectListeners(User.class, getId(), UserChangeNicknameListener.class);
    }

    @Override
    public ListenerManager<UserChangeSelfMutedListener> addUserChangeSelfMutedListener(
            UserChangeSelfMutedListener listener) {
        return ((DiscordApiImpl) getApi())
                .addObjectListener(User.class, getId(), UserChangeSelfMutedListener.class, listener);
    }

    @Override
    public List<UserChangeSelfMutedListener> getUserChangeSelfMutedListeners() {
        return ((DiscordApiImpl) getApi()).getObjectListeners(User.class, getId(), UserChangeSelfMutedListener.class);
    }

    @Override
    public ListenerManager<UserChangeSelfDeafenedListener> addUserChangeSelfDeafenedListener(
            UserChangeSelfDeafenedListener listener) {
        return ((DiscordApiImpl) getApi())
                .addObjectListener(User.class, getId(), UserChangeSelfDeafenedListener.class, listener);
    }

    @Override
    public List<UserChangeSelfDeafenedListener> getUserChangeSelfDeafenedListeners() {
        return ((DiscordApiImpl) getApi())
                .getObjectListeners(User.class, getId(), UserChangeSelfDeafenedListener.class);
    }

    @Override
    public ListenerManager<UserChangeMutedListener> addUserChangeMutedListener(UserChangeMutedListener listener) {
        return ((DiscordApiImpl) getApi())
                .addObjectListener(User.class, getId(), UserChangeMutedListener.class, listener);
    }

    @Override
    public List<UserChangeMutedListener> getUserChangeMutedListeners() {
        return ((DiscordApiImpl) getApi()).getObjectListeners(User.class, getId(), UserChangeMutedListener.class);
    }

    @Override
    public ListenerManager<UserRoleAddListener> addUserRoleAddListener(UserRoleAddListener listener) {
        return ((DiscordApiImpl) getApi()).addObjectListener(User.class, getId(), UserRoleAddListener.class, listener);
    }

    @Override
    public List<UserRoleAddListener> getUserRoleAddListeners() {
        return ((DiscordApiImpl) getApi()).getObjectListeners(User.class, getId(), UserRoleAddListener.class);
    }

    @Override
    public ListenerManager<UserRoleRemoveListener> addUserRoleRemoveListener(UserRoleRemoveListener listener) {
        return ((DiscordApiImpl) getApi())
                .addObjectListener(User.class, getId(), UserRoleRemoveListener.class, listener);
    }

    @Override
    public List<UserRoleRemoveListener> getUserRoleRemoveListeners() {
        return ((DiscordApiImpl) getApi()).getObjectListeners(User.class, getId(), UserRoleRemoveListener.class);
    }

    @Override
    public ListenerManager<UserChangeNameListener> addUserChangeNameListener(UserChangeNameListener listener) {
        return ((DiscordApiImpl) getApi())
                .addObjectListener(User.class, getId(), UserChangeNameListener.class, listener);
    }

    @Override
    public List<UserChangeNameListener> getUserChangeNameListeners() {
        return ((DiscordApiImpl) getApi()).getObjectListeners(User.class, getId(), UserChangeNameListener.class);
    }

    @Override
    public ListenerManager<UserChangeDiscriminatorListener> addUserChangeDiscriminatorListener(
            UserChangeDiscriminatorListener listener) {
        return ((DiscordApiImpl) getApi())
                .addObjectListener(User.class, getId(), UserChangeDiscriminatorListener.class, listener);
    }

    @Override
    public List<UserChangeDiscriminatorListener> getUserChangeDiscriminatorListeners() {
        return ((DiscordApiImpl) getApi()).getObjectListeners(
                User.class, getId(), UserChangeDiscriminatorListener.class);
    }

    @Override
    public ListenerManager<UserChangeAvatarListener> addUserChangeAvatarListener(UserChangeAvatarListener listener) {
        return ((DiscordApiImpl) getApi())
                .addObjectListener(User.class, getId(), UserChangeAvatarListener.class, listener);
    }

    @Override
    public List<UserChangeAvatarListener> getUserChangeAvatarListeners() {
        return ((DiscordApiImpl) getApi()).getObjectListeners(User.class, getId(), UserChangeAvatarListener.class);
    }

    @Override
    public ListenerManager<ServerVoiceChannelMemberJoinListener> addServerVoiceChannelMemberJoinListener(
            ServerVoiceChannelMemberJoinListener listener) {
        return ((DiscordApiImpl) getApi())
                .addObjectListener(User.class, getId(), ServerVoiceChannelMemberJoinListener.class, listener);
    }

    @Override
    public List<ServerVoiceChannelMemberJoinListener> getServerVoiceChannelMemberJoinListeners() {
        return ((DiscordApiImpl) getApi()).getObjectListeners(
                User.class, getId(), ServerVoiceChannelMemberJoinListener.class);
    }

    @Override
    public ListenerManager<ServerVoiceChannelMemberLeaveListener> addServerVoiceChannelMemberLeaveListener(
            ServerVoiceChannelMemberLeaveListener listener) {
        return ((DiscordApiImpl) getApi())
                .addObjectListener(User.class, getId(), ServerVoiceChannelMemberLeaveListener.class, listener);
    }

    @Override
    public List<ServerVoiceChannelMemberLeaveListener> getServerVoiceChannelMemberLeaveListeners() {
        return ((DiscordApiImpl) getApi()).getObjectListeners(
                User.class, getId(), ServerVoiceChannelMemberLeaveListener.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends UserAttachableListener & ObjectAttachableListener> Collection<ListenerManager<T>>
            addUserAttachableListener(T listener) {
        return ClassHelper.getInterfacesAsStream(listener.getClass())
                .filter(UserAttachableListener.class::isAssignableFrom)
                .filter(ObjectAttachableListener.class::isAssignableFrom)
                .map(listenerClass -> (Class<T>) listenerClass)
                .map(listenerClass -> ((DiscordApiImpl) getApi()).addObjectListener(User.class, getId(),
                                                                                    listenerClass, listener))
                .collect(Collectors.toList());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends UserAttachableListener & ObjectAttachableListener> void removeUserAttachableListener(T listener) {
        ClassHelper.getInterfacesAsStream(listener.getClass())
                .filter(UserAttachableListener.class::isAssignableFrom)
                .filter(ObjectAttachableListener.class::isAssignableFrom)
                .map(listenerClass -> (Class<T>) listenerClass)
                .forEach(listenerClass -> ((DiscordApiImpl) getApi()).removeObjectListener(User.class, getId(),
                                                                                           listenerClass, listener));
    }

    @Override
    public <T extends UserAttachableListener & ObjectAttachableListener> Map<T, List<Class<T>>>
            getUserAttachableListeners() {
        return ((DiscordApiImpl) getApi()).getObjectListeners(User.class, getId());
    }

    @Override
    public <T extends UserAttachableListener & ObjectAttachableListener> void removeListener(
            Class<T> listenerClass, T listener) {
        ((DiscordApiImpl) getApi()).removeObjectListener(User.class, getId(), listenerClass, listener);
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
