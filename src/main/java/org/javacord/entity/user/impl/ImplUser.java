package org.javacord.entity.user.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.javacord.DiscordApi;
import org.javacord.ImplDiscordApi;
import org.javacord.entity.DiscordEntity;
import org.javacord.entity.Icon;
import org.javacord.entity.activity.Activity;
import org.javacord.entity.channel.PrivateChannel;
import org.javacord.entity.channel.ServerVoiceChannel;
import org.javacord.entity.channel.impl.ImplPrivateChannel;
import org.javacord.entity.impl.ImplIcon;
import org.javacord.entity.user.User;
import org.javacord.entity.user.UserStatus;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.channel.group.GroupChannelChangeNameListener;
import org.javacord.listener.channel.group.GroupChannelCreateListener;
import org.javacord.listener.channel.group.GroupChannelDeleteListener;
import org.javacord.listener.channel.server.ServerChannelChangeOverwrittenPermissionsListener;
import org.javacord.listener.channel.server.voice.ServerVoiceChannelMemberJoinListener;
import org.javacord.listener.channel.server.voice.ServerVoiceChannelMemberLeaveListener;
import org.javacord.listener.channel.user.PrivateChannelCreateListener;
import org.javacord.listener.channel.user.PrivateChannelDeleteListener;
import org.javacord.listener.message.MessageCreateListener;
import org.javacord.listener.message.reaction.ReactionAddListener;
import org.javacord.listener.message.reaction.ReactionRemoveListener;
import org.javacord.listener.server.member.ServerMemberBanListener;
import org.javacord.listener.server.member.ServerMemberJoinListener;
import org.javacord.listener.server.member.ServerMemberLeaveListener;
import org.javacord.listener.server.member.ServerMemberUnbanListener;
import org.javacord.listener.server.role.UserRoleAddListener;
import org.javacord.listener.server.role.UserRoleRemoveListener;
import org.javacord.listener.user.UserAttachableListener;
import org.javacord.listener.user.UserChangeActivityListener;
import org.javacord.listener.user.UserChangeAvatarListener;
import org.javacord.listener.user.UserChangeNameListener;
import org.javacord.listener.user.UserChangeNicknameListener;
import org.javacord.listener.user.UserChangeStatusListener;
import org.javacord.listener.user.UserStartTypingListener;
import org.javacord.util.ClassHelper;
import org.javacord.util.Cleanupable;
import org.javacord.util.event.ListenerManager;
import org.javacord.util.logging.LoggerUtil;
import org.javacord.util.rest.RestEndpoint;
import org.javacord.util.rest.RestMethod;
import org.javacord.util.rest.RestRequest;
import org.slf4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * The implementation of {@link User}.
 */
public class ImplUser implements User, Cleanupable {

    /**
     * The logger of this class.
     */
    Logger logger = LoggerUtil.getLogger(ImplUser.class);

    /**
     * The discord api instance.
     */
    private final ImplDiscordApi api;

    /**
     * The id of the user.
     */
    private final long id;

    /**
     * The name of the user.
     */
    private String name;

    /**
     * The private channel with the given user.
     */
    private PrivateChannel channel = null;

    /**
     * The avatar hash of the user. Might be <code>null</code>!
     */
    private String avatarHash = null;

    /**
     * The discriminator of the user.
     */
    private String discriminator;

    /**
     * Whether the user is a bot account or not.
     */
    private final boolean bot;

    /**
     * The activity of the user.
     */
    private Activity activity = null;

    /**
     * The server voice channels the user is connected to.
     */
    private final Collection<ServerVoiceChannel> connectedVoiceChannels = new HashSet<>();

    /**
     * The status of the user.
     */
    private UserStatus status = UserStatus.OFFLINE;

    /**
     * Creates a new user.
     *
     * @param api The discord api instance.
     * @param data The json data of the user.
     */
    public ImplUser(ImplDiscordApi api, JsonNode data) {
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
            return new ImplPrivateChannel(api, data);
        }
    }

    /**
     * Adds the given server voice channel to the ones this user is connected to.
     *
     * @param channel The server voice channel this user has connected to.
     */
    public void addConnectedVoiceChannel(ServerVoiceChannel channel) {
        connectedVoiceChannels.add(channel);
    }

    /**
     * Removes the given server voice channel from the ones this user is connected to.
     *
     * @param channel The server voice channel this user has left.
     */
    public void removeConnectedVoiceChannel(ServerVoiceChannel channel) {
        connectedVoiceChannels.remove(channel);
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
    public Collection<ServerVoiceChannel> getConnectedVoiceChannels() {
        return Collections.unmodifiableCollection(connectedVoiceChannels);
    }

    @Override
    public UserStatus getStatus() {
        return status;
    }

    @Override
    public Icon getAvatar() {
        String url = "https://cdn.discordapp.com/embed/avatars/" + Integer.parseInt(discriminator) % 5 + ".png";
        if (avatarHash != null) {
            url = "https://cdn.discordapp.com/avatars/" + getIdAsString() + "/" + avatarHash +
                    (avatarHash.startsWith("a_") ? ".gif" : ".png");
        }
        try {
            return new ImplIcon(getApi(), new URL(url));
        } catch (MalformedURLException e) {
            logger.warn("Seems like the url of the avatar is malformed! Please contact the developer!", e);
            return null;
        }
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
        return ((ImplDiscordApi) getApi()).addObjectListener(
                User.class, getId(), PrivateChannelCreateListener.class, listener);
    }

    @Override
    public List<PrivateChannelCreateListener> getPrivateChannelCreateListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(User.class, getId(), PrivateChannelCreateListener.class);
    }

    @Override
    public ListenerManager<PrivateChannelDeleteListener> addPrivateChannelDeleteListener(
            PrivateChannelDeleteListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                User.class, getId(), PrivateChannelDeleteListener.class, listener);
    }

    @Override
    public List<PrivateChannelDeleteListener> getPrivateChannelDeleteListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(User.class, getId(), PrivateChannelDeleteListener.class);
    }

    @Override
    public ListenerManager<GroupChannelCreateListener> addGroupChannelCreateListener(
            GroupChannelCreateListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                User.class, getId(), GroupChannelCreateListener.class, listener);
    }

    @Override
    public List<GroupChannelCreateListener> getGroupChannelCreateListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(User.class, getId(), GroupChannelCreateListener.class);
    }

    @Override
    public ListenerManager<GroupChannelChangeNameListener> addGroupChannelChangeNameListener(
            GroupChannelChangeNameListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                User.class, getId(), GroupChannelChangeNameListener.class, listener);
    }

    @Override
    public List<GroupChannelChangeNameListener> getGroupChannelChangeNameListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(
                User.class, getId(), GroupChannelChangeNameListener.class);
    }

    @Override
    public ListenerManager<GroupChannelDeleteListener> addGroupChannelDeleteListener(
            GroupChannelDeleteListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                User.class, getId(), GroupChannelDeleteListener.class, listener);
    }

    @Override
    public List<GroupChannelDeleteListener> getGroupChannelDeleteListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(User.class, getId(), GroupChannelDeleteListener.class);
    }

    @Override
    public ListenerManager<MessageCreateListener> addMessageCreateListener(MessageCreateListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(User.class, getId(), MessageCreateListener.class, listener);
    }

    @Override
    public List<MessageCreateListener> getMessageCreateListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(User.class, getId(), MessageCreateListener.class);
    }

    @Override
    public ListenerManager<UserStartTypingListener> addUserStartTypingListener(UserStartTypingListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(User.class, getId(), UserStartTypingListener.class, listener);
    }

    @Override
    public List<UserStartTypingListener> getUserStartTypingListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(User.class, getId(), UserStartTypingListener.class);
    }

    @Override
    public ListenerManager<ReactionAddListener> addReactionAddListener(ReactionAddListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(User.class, getId(), ReactionAddListener.class, listener);
    }

    @Override
    public List<ReactionAddListener> getReactionAddListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(User.class, getId(), ReactionAddListener.class);
    }

    @Override
    public ListenerManager<ReactionRemoveListener> addReactionRemoveListener(ReactionRemoveListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(User.class, getId(), ReactionRemoveListener.class, listener);
    }

    @Override
    public List<ReactionRemoveListener> getReactionRemoveListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(User.class, getId(), ReactionRemoveListener.class);
    }

    @Override
    public ListenerManager<ServerMemberJoinListener> addServerMemberJoinListener(ServerMemberJoinListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(User.class, getId(), ServerMemberJoinListener.class, listener);
    }

    @Override
    public List<ServerMemberJoinListener> getServerMemberJoinListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(User.class, getId(), ServerMemberJoinListener.class);
    }

    @Override
    public ListenerManager<ServerMemberLeaveListener> addServerMemberLeaveListener(
            ServerMemberLeaveListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(User.class, getId(), ServerMemberLeaveListener.class, listener);
    }

    @Override
    public List<ServerMemberLeaveListener> getServerMemberLeaveListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(User.class, getId(), ServerMemberLeaveListener.class);
    }

    @Override
    public ListenerManager<ServerMemberBanListener> addServerMemberBanListener(ServerMemberBanListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(User.class, getId(), ServerMemberBanListener.class, listener);
    }

    @Override
    public List<ServerMemberBanListener> getServerMemberBanListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(User.class, getId(), ServerMemberBanListener.class);
    }

    @Override
    public ListenerManager<ServerMemberUnbanListener> addServerMemberUnbanListener(ServerMemberUnbanListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(User.class, getId(), ServerMemberUnbanListener.class, listener);
    }

    @Override
    public List<ServerMemberUnbanListener> getServerMemberUnbanListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(User.class, getId(), ServerMemberUnbanListener.class);
    }

    @Override
    public ListenerManager<UserChangeActivityListener> addUserChangeActivityListener(UserChangeActivityListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(User.class, getId(), UserChangeActivityListener.class, listener);
    }

    @Override
    public List<UserChangeActivityListener> getUserChangeActivityListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(User.class, getId(), UserChangeActivityListener.class);
    }

    @Override
    public ListenerManager<UserChangeStatusListener> addUserChangeStatusListener(UserChangeStatusListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(User.class, getId(), UserChangeStatusListener.class, listener);
    }

    @Override
    public List<UserChangeStatusListener> getUserChangeStatusListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(User.class, getId(), UserChangeStatusListener.class);
    }

    @Override
    public ListenerManager<ServerChannelChangeOverwrittenPermissionsListener>
    addServerChannelChangeOverwrittenPermissionsListener(ServerChannelChangeOverwrittenPermissionsListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                User.class, getId(), ServerChannelChangeOverwrittenPermissionsListener.class, listener);
    }

    @Override
    public List<ServerChannelChangeOverwrittenPermissionsListener>
            getServerChannelChangeOverwrittenPermissionsListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(
                User.class, getId(), ServerChannelChangeOverwrittenPermissionsListener.class);
    }

    @Override
    public ListenerManager<UserChangeNicknameListener> addUserChangeNicknameListener(
            UserChangeNicknameListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(User.class, getId(), UserChangeNicknameListener.class, listener);
    }

    @Override
    public List<UserChangeNicknameListener> getUserChangeNicknameListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(User.class, getId(), UserChangeNicknameListener.class);
    }

    @Override
    public ListenerManager<UserRoleAddListener> addUserRoleAddListener(UserRoleAddListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(User.class, getId(), UserRoleAddListener.class, listener);
    }

    @Override
    public List<UserRoleAddListener> getUserRoleAddListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(User.class, getId(), UserRoleAddListener.class);
    }

    @Override
    public ListenerManager<UserRoleRemoveListener> addUserRoleRemoveListener(UserRoleRemoveListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(User.class, getId(), UserRoleRemoveListener.class, listener);
    }

    @Override
    public List<UserRoleRemoveListener> getUserRoleRemoveListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(User.class, getId(), UserRoleRemoveListener.class);
    }

    @Override
    public ListenerManager<UserChangeNameListener> addUserChangeNameListener(UserChangeNameListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(User.class, getId(), UserChangeNameListener.class, listener);
    }

    @Override
    public List<UserChangeNameListener> getUserChangeNameListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(User.class, getId(), UserChangeNameListener.class);
    }

    @Override
    public ListenerManager<UserChangeAvatarListener> addUserChangeAvatarListener(UserChangeAvatarListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(User.class, getId(), UserChangeAvatarListener.class, listener);
    }

    @Override
    public List<UserChangeAvatarListener> getUserChangeAvatarListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(User.class, getId(), UserChangeAvatarListener.class);
    }

    @Override
    public ListenerManager<ServerVoiceChannelMemberJoinListener> addServerVoiceChannelMemberJoinListener(
            ServerVoiceChannelMemberJoinListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(User.class, getId(), ServerVoiceChannelMemberJoinListener.class, listener);
    }

    @Override
    public List<ServerVoiceChannelMemberJoinListener> getServerVoiceChannelMemberJoinListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(
                User.class, getId(), ServerVoiceChannelMemberJoinListener.class);
    }

    @Override
    public ListenerManager<ServerVoiceChannelMemberLeaveListener> addServerVoiceChannelMemberLeaveListener(
            ServerVoiceChannelMemberLeaveListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(User.class, getId(), ServerVoiceChannelMemberLeaveListener.class, listener);
    }

    @Override
    public List<ServerVoiceChannelMemberLeaveListener> getServerVoiceChannelMemberLeaveListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(
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
                .map(listenerClass -> ((ImplDiscordApi) getApi()).addObjectListener(User.class, getId(),
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
                .forEach(listenerClass -> ((ImplDiscordApi) getApi()).removeObjectListener(User.class, getId(),
                                                                                           listenerClass, listener));
    }

    @Override
    public <T extends UserAttachableListener & ObjectAttachableListener> Map<T, List<Class<T>>>
    getUserAttachableListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(User.class, getId());
    }

    @Override
    public <T extends UserAttachableListener & ObjectAttachableListener> void removeListener(
            Class<T> listenerClass, T listener) {
        ((ImplDiscordApi) getApi()).removeObjectListener(User.class, getId(), listenerClass, listener);
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
