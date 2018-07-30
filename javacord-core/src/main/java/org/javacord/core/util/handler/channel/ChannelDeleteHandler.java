package org.javacord.core.util.handler.channel;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.event.channel.group.GroupChannelDeleteEvent;
import org.javacord.api.event.channel.server.ServerChannelDeleteEvent;
import org.javacord.api.event.channel.user.PrivateChannelDeleteEvent;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.entity.user.UserImpl;
import org.javacord.core.event.channel.group.GroupChannelDeleteEventImpl;
import org.javacord.core.event.channel.server.ServerChannelDeleteEventImpl;
import org.javacord.core.event.channel.user.PrivateChannelDeleteEventImpl;
import org.javacord.core.util.gateway.PacketHandler;
import org.javacord.core.util.logging.LoggerUtil;

import java.util.Collections;

/**
 * Handles the channel delete packet.
 */
public class ChannelDeleteHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public ChannelDeleteHandler(DiscordApi api) {
        super(api, true, "CHANNEL_DELETE");
    }

    @Override
    public void handle(JsonNode packet) {
        int type = packet.get("type").asInt();
        switch (type) {
            case 0:
                handleServerTextChannel(packet);
                break;
            case 1:
                handlePrivateChannel(packet);
                break;
            case 2:
                handleServerVoiceChannel(packet);
                break;
            case 3:
                handleGroupChannel(packet);
                break;
            case 4:
                handleCategory(packet);
                break;
            default:
                LoggerUtil.getLogger(ChannelDeleteHandler.class).warn("Unexpected packet type. Your Javacord version"
                        + " might be out of date.");
        }
    }

    /**
     * Handles server text channel deletion.
     *
     * @param channelJson The channel data.
     */
    private void handleCategory(JsonNode channelJson) {
        long serverId = channelJson.get("guild_id").asLong();
        long channelId = channelJson.get("id").asLong();
        api.getPossiblyUnreadyServerById(serverId).ifPresent(server -> server.getChannelCategoryById(channelId)
                .ifPresent(channel -> {
                            dispatchServerChannelDeleteEvent(channel);
                            ((ServerImpl) server).removeChannelFromCache(channel.getId());
                        }
                ));
    }

    /**
     * Handles server text channel deletion.
     *
     * @param channelJson The channel data.
     */
    private void handleServerTextChannel(JsonNode channelJson) {
        long serverId = channelJson.get("guild_id").asLong();
        long channelId = channelJson.get("id").asLong();
        api.getPossiblyUnreadyServerById(serverId)
                .ifPresent(server -> server.getTextChannelById(channelId).ifPresent(channel -> {
                    dispatchServerChannelDeleteEvent(channel);
                    ((ServerImpl) server).removeChannelFromCache(channel.getId());
                }));
    }

    /**
     * Handles server voice channel deletion.
     *
     * @param channelJson The channel data.
     */
    private void handleServerVoiceChannel(JsonNode channelJson) {
        long serverId = channelJson.get("guild_id").asLong();
        long channelId = channelJson.get("id").asLong();
        api.getPossiblyUnreadyServerById(serverId)
                .ifPresent(server -> server.getVoiceChannelById(channelId).ifPresent(channel -> {
                    dispatchServerChannelDeleteEvent(channel);
                    ((ServerImpl) server).removeChannelFromCache(channel.getId());
                }));
    }

    /**
     * Handles a private channel deletion.
     *
     * @param channel The channel data.
     */
    private void handlePrivateChannel(JsonNode channel) {
        UserImpl recipient = (UserImpl) api.getOrCreateUser(channel.get("recipients").get(0));
        recipient.getPrivateChannel().ifPresent(privateChannel -> {
            PrivateChannelDeleteEvent event = new PrivateChannelDeleteEventImpl(privateChannel);

            api.getEventDispatcher().dispatchPrivateChannelDeleteEvent(api, privateChannel, recipient, event);

            recipient.setChannel(null);
        });
    }

    /**
     * Handles a group channel deletion.
     *
     * @param channel The channel data.
     */
    private void handleGroupChannel(JsonNode channel) {
        long channelId = channel.get("id").asLong();

        api.getGroupChannelById(channelId).ifPresent(groupChannel -> {
            GroupChannelDeleteEvent event = new GroupChannelDeleteEventImpl(groupChannel);

            api.getEventDispatcher().dispatchGroupChannelDeleteEvent(
                    api, Collections.singleton(groupChannel), groupChannel.getMembers(), event);

            api.removeGroupChannelFromCache(channelId);
        });
    }

    /**
     * Dispatches a server channel delete event.
     *
     * @param channel The channel of the event.
     */
    private void dispatchServerChannelDeleteEvent(ServerChannel channel) {
        ServerChannelDeleteEvent event = new ServerChannelDeleteEventImpl(channel);

        api.getEventDispatcher().dispatchServerChannelDeleteEvent(
                channel.getServer(), channel.getServer(), channel, event);
    }

}
