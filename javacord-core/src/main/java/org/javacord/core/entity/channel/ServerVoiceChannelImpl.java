package org.javacord.core.entity.channel;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.audio.AudioConnection;
import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.channel.ChannelCategory;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.user.User;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.audio.AudioConnectionImpl;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.listener.channel.server.voice.InternalServerVoiceChannelAttachableListenerManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * The implementation of {@link ServerVoiceChannel}.
 */
public class ServerVoiceChannelImpl extends RegularServerChannelImpl
        implements ServerVoiceChannel, InternalServerVoiceChannelAttachableListenerManager {

    /**
     * The bitrate of the channel.
     */
    private volatile int bitrate;

    /**
     * The userLimit of the channel.
     */
    private volatile int userLimit;

    /**
     * The parent id of the channel.
     */
    private volatile long parentId;

    /**
     * The ids of the connected users of this server voice channel.
     */
    private final Collection<Long> connectedUsers = new ArrayList<>();

    /**
     * Creates a new server voice channel object.
     *
     * @param api The discord api instance.
     * @param server The server of the channel.
     * @param data The json data of the channel.
     */
    public ServerVoiceChannelImpl(DiscordApiImpl api, ServerImpl server, JsonNode data) {
        super(api, server, data);
        bitrate = data.get("bitrate").asInt();
        userLimit = data.get("user_limit").asInt();
        parentId = Long.parseLong(data.has("parent_id") ? data.get("parent_id").asText("-1") : "-1");
    }

    /**
     * Sets the bitrate of the channel.
     *
     * @param bitrate The new bitrate of the channel.
     */
    public void setBitrate(int bitrate) {
        this.bitrate = bitrate;
    }

    /**
     * Sets the user limit of the channel.
     *
     * @param userLimit The user limit to set.
     */
    public void setUserLimit(int userLimit) {
        this.userLimit = userLimit;
    }

    /**
     * Sets the parent id of the channel.
     *
     * @param parentId The parent id to set.
     */
    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    /**
     * Adds the user with the given id to the list of connected users.
     *
     * @param userId The id of the user to add.
     */
    public void addConnectedUser(long userId) {
        connectedUsers.add(userId);
    }

    /**
     * Removes the user with the given id from the list of connected users.
     *
     * @param userId The id of the user to remove.
     */
    public void removeConnectedUser(long userId) {
        connectedUsers.remove(userId);
    }

    @Override
    public Optional<ChannelCategory> getCategory() {
        return getServer().getChannelCategoryById(parentId);
    }

    @Override
    public CompletableFuture<AudioConnection> connect(boolean muted, boolean deafened) {
        return getServer()
                .getAudioConnection()
                .map(AudioConnection::close)
                .orElseGet(() -> CompletableFuture.completedFuture(null))
                .thenCompose(closedAudioConnection -> {
                    CompletableFuture<AudioConnection> future = new CompletableFuture<>();
                    AudioConnectionImpl connection = new AudioConnectionImpl(this, future, muted, deafened);
                    ((ServerImpl) getServer()).setPendingAudioConnection(connection);
                    return future;
                })
                .thenApply(conn -> {
                    ((ServerImpl) getServer()).setAudioConnection((AudioConnectionImpl) conn);
                    return conn;
                });
    }

    @Override
    public int getBitrate() {
        return bitrate;
    }

    @Override
    public Optional<Integer> getUserLimit() {
        return userLimit == 0 ? Optional.empty() : Optional.of(userLimit);
    }

    @Override
    public Collection<Long> getConnectedUserIds() {
        return Collections.unmodifiableCollection(connectedUsers);
    }

    @Override
    public Collection<User> getConnectedUsers() {
        return Collections.unmodifiableCollection(
                connectedUsers.stream()
                        .map(getApi()::getCachedUserById)
                        .map(optionalUser -> optionalUser.orElseThrow(AssertionError::new))
                        .collect(Collectors.toList()));
    }

    @Override
    public boolean isConnected(long userId) {
        return connectedUsers.contains(userId);
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
        return String.format("ServerVoiceChannel (id: %s, name: %s)", getIdAsString(), getName());
    }

}
