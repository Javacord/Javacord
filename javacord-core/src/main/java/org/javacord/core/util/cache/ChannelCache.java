package org.javacord.core.util.cache;

import io.vavr.Tuple;
import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.channel.ChannelType;
import org.javacord.api.entity.channel.PrivateChannel;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.channel.VoiceChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.core.util.ImmutableToJavaMapper;

import java.util.Optional;
import java.util.Set;

/**
 * An immutable cache for all channel entities.
 */
public class ChannelCache {

    private static final String ID_INDEX_NAME = "id";
    private static final String TYPE_INDEX_NAME = "type";
    private static final String SERVER_ID_INDEX_NAME = "server-id";
    private static final String SERVER_ID_AND_TYPE_INDEX_NAME = "server-id | type";
    private static final String PRIVATE_CHANNEL_USER_ID_INDEX_NAME = "user-id";

    private static final ChannelCache EMPTY_CACHE = new ChannelCache(Cache.<Channel>empty()
            .addIndex(ID_INDEX_NAME, Channel::getId)
            .addIndex(TYPE_INDEX_NAME, Channel::getType)
            .addIndex(SERVER_ID_INDEX_NAME, channel -> channel
                    .asServerChannel()
                    .map(ServerChannel::getServer)
                    .map(Server::getId)
                    .orElse(null))
            .addIndex(SERVER_ID_AND_TYPE_INDEX_NAME, channel -> channel
                    .asServerChannel()
                    .map(ServerChannel::getServer)
                    .map(Server::getId)
                    .map(serverId -> Tuple.of(serverId, channel.getType()))
                    .orElse(null))
            .addIndex(PRIVATE_CHANNEL_USER_ID_INDEX_NAME, channel -> channel
                    .asPrivateChannel()
                    .map(PrivateChannel::getRecipient)
                    .map(User::getId)
                    .orElse(null))
    );

    private final Cache<Channel> cache;

    private ChannelCache(Cache<Channel> cache) {
        this.cache = cache;
    }

    /**
     * Gets an empty channel cache.
     *
     * @return An empty channel cache.
     */
    public static ChannelCache empty() {
        return EMPTY_CACHE;
    }

    /**
     * Adds a channel to the cache.
     *
     * @param channel The channel to add.
     * @return The new channel cache.
     */
    public ChannelCache addChannel(Channel channel) {
        return new ChannelCache(cache.addElement(channel));
    }

    /**
     * Removes a channel from the cache.
     *
     * @param channel The channel to remove.
     * @return The new channel cache.
     */
    public ChannelCache removeChannel(Channel channel) {
        return new ChannelCache(cache.removeElement(channel));
    }

    /**
     * Gets a set with all channels in the cache.
     *
     * @return A set with all channels.
     */
    public Set<Channel> getChannels() {
        return ImmutableToJavaMapper.mapToJava(cache.getAll());
    }

    /**
     * Gets all channels that have one of the given types.
     *
     * @param types The types of the channels to get.
     * @param <T> A type that at least all channels of the given types share.
     *            E.g., if the provided {@code types} parameter is {@link ChannelType#SERVER_TEXT_CHANNEL} and
     *            {@link ChannelType#SERVER_VOICE_CHANNEL}, {@code T} can be {@link ServerChannel} but must not be
     *            {@link TextChannel}.
     * @return A set with all channels that are of one of the given types.
     */
    public <T extends Channel> Set<T> getChannelsWithTypes(ChannelType... types) {
        io.vavr.collection.HashSet<Channel> channels = io.vavr.collection.HashSet.empty();
        for (ChannelType type : types) {
            channels = channels.addAll(cache.findByIndex(TYPE_INDEX_NAME, type));
        }
        return ImmutableToJavaMapper.mapToJava(channels);
    }

    /**
     * Gets all channels of the server with the given id.
     *
     * @param serverId The id of the server.
     * @return A set with all channels in the server.
     */
    public Set<ServerChannel> getChannelsOfServer(long serverId) {
        return ImmutableToJavaMapper.mapToJava(cache.findByIndex(SERVER_ID_INDEX_NAME, serverId));
    }

    /**
     * Gets all channels with the given type of the server with the given id.
     *
     * @param serverId The id of the server.
     * @param type The type of the channels in the server.
     * @param <T> A type that at least all channels of the given type share.
     *            E.g., if the provided {@code type} parameter is {@link ChannelType#SERVER_TEXT_CHANNEL}
     *            {@code T} can be {@link ServerTextChannel} or {@link ServerChannel} but must not be
     *            {@link ServerVoiceChannel} or {@link VoiceChannel}.
     * @return A set with all channels with the given type of the server with the given id.
     */
    public <T extends Channel> Set<T> getChannelsOfServerAndType(long serverId, ChannelType type) {
        return ImmutableToJavaMapper.mapToJava(
                cache.findByIndex(SERVER_ID_AND_TYPE_INDEX_NAME, Tuple.of(serverId, type)));
    }

    /**
     * Gets a channel by its id.
     *
     * <p>This method has a time-complexity of {@code O(1)}.
     *
     * @param id The id of the channel.
     * @return The channel with the given id.
     */
    public Optional<Channel> getChannelById(long id) {
        return cache.findAnyByIndex(ID_INDEX_NAME, id);
    }

    /**
     * Gets a private channel by the user's id.
     *
     * @param userId The id of the user.
     * @return The private channel.
     */
    public Optional<PrivateChannel> getPrivateChannelByUserId(long userId) {
        return cache.findAnyByIndex(PRIVATE_CHANNEL_USER_ID_INDEX_NAME, userId)
                .flatMap(Channel::asPrivateChannel);
    }
}
