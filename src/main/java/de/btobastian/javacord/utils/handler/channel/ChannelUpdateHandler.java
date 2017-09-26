package de.btobastian.javacord.utils.handler.channel;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.channels.impl.ImplChannelCategory;
import de.btobastian.javacord.entities.channels.impl.ImplServerTextChannel;
import de.btobastian.javacord.entities.channels.impl.ImplServerVoiceChannel;
import de.btobastian.javacord.events.server.channel.ServerChannelChangeNameEvent;
import de.btobastian.javacord.events.server.channel.ServerChannelChangePositionEvent;
import de.btobastian.javacord.listeners.server.channel.ServerChannelChangeNameListener;
import de.btobastian.javacord.listeners.server.channel.ServerChannelChangePositionListener;
import de.btobastian.javacord.utils.PacketHandler;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Handles the channel update packet.
 */
public class ChannelUpdateHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public ChannelUpdateHandler(DiscordApi api) {
        super(api, true, "CHANNEL_UPDATE");
    }

    @Override
    public void handle(JSONObject packet) {
        int type = packet.getInt("type");
        switch (type) {
            case 0:
                handleServerChannel(packet);
                handleServerTextChannel(packet);
                break;
            case 1:
                handlePrivateChannel(packet);
                break;
            case 2:
                handleServerChannel(packet);
                handleServerVoiceChannel(packet);
                break;
            case 4:
                handleServerChannel(packet);
                handleChannelCategory(packet);
                break;
        }
    }

    /**
     * Handles a server channel update.
     *
     * @param channel The channel data.
     */
    private void handleServerChannel(JSONObject channel) {
        long channelId = Long.parseLong(channel.getString("id"));
        api.getServerChannelById(channelId).ifPresent(c -> {
            String oldName = c.getName();
            String newName = channel.getString("name");
            if (!Objects.deepEquals(oldName, newName)) {
                c.asChannelCategory().ifPresent(cc -> ((ImplChannelCategory) cc).setName(newName));
                c.asServerTextChannel().ifPresent(stc -> ((ImplServerTextChannel) stc).setName(newName));
                c.asServerVoiceChannel().ifPresent(svc -> ((ImplServerVoiceChannel) svc).setName(newName));
                ServerChannelChangeNameEvent event =
                        new ServerChannelChangeNameEvent(api, c.getServer(), c, newName, oldName);

                List<ServerChannelChangeNameListener> listeners = new ArrayList<>();
                listeners.addAll(c.getServerChannelChangeNameListeners());
                listeners.addAll(c.getServer().getServerChannelChangeNameListeners());
                listeners.addAll(api.getServerChannelChangeNameListeners());

                dispatchEvent(listeners, listener -> listener.onServerChannelChangeName(event));
            }

            int oldPosition = c.getPosition();
            int newPosition = channel.getInt("position");
            if (oldPosition != newPosition) {
                c.asChannelCategory().ifPresent(cc -> ((ImplChannelCategory) cc).setPosition(newPosition));
                c.asServerTextChannel().ifPresent(stc -> ((ImplServerTextChannel) stc).setPosition(newPosition));
                c.asServerVoiceChannel().ifPresent(svc -> ((ImplServerVoiceChannel) svc).setPosition(newPosition));

                ServerChannelChangePositionEvent event =
                        new ServerChannelChangePositionEvent(api, c.getServer(), c, newPosition, oldPosition);

                List<ServerChannelChangePositionListener> listeners = new ArrayList<>();
                listeners.addAll(c.getServerChannelChangePositionListeners());
                listeners.addAll(c.getServer().getServerChannelChangePositionListeners());
                listeners.addAll(api.getServerChannelChangePositionListeners());

                dispatchEvent(listeners, listener -> listener.onServerChannelChangePosition(event));
            }
        });
    }

    /**
     * Handles a channel category update.
     *
     * @param channel The channel data.
     */
    private void handleChannelCategory(JSONObject channel) {
        long channelId = Long.parseLong(channel.getString("id"));
        api.getChannelCategoryById(channelId).map(c -> ((ImplChannelCategory) c)).ifPresent(c -> {
            String oldName = c.getName();
            String newName = channel.getString("name");
            if (!Objects.deepEquals(oldName, newName)) {
                c.setName(newName);
                ServerChannelChangeNameEvent event =
                        new ServerChannelChangeNameEvent(api, c.getServer(), c, newName, oldName);

                List<ServerChannelChangeNameListener> listeners = new ArrayList<>();
                listeners.addAll(c.getServerChannelChangeNameListeners());
                listeners.addAll(c.getServer().getServerChannelChangeNameListeners());
                listeners.addAll(api.getServerChannelChangeNameListeners());

                dispatchEvent(listeners, listener -> listener.onServerChannelChangeName(event));
            }
        });
    }

    /**
     * Handles a server text channel update.
     *
     * @param channel The channel data.
     */
    private void handleServerTextChannel(JSONObject channel) {
        long serverId = Long.parseLong(channel.getString("guild_id"));

    }

    /**
     * Handles a server voice channel update.
     *
     * @param channel The channel data.
     */
    private void handleServerVoiceChannel(JSONObject channel) {
        long serverId = Long.parseLong(channel.getString("guild_id"));

    }

    /**
     * Handles a private channel update.
     *
     * @param channel The channel data.
     */
    private void handlePrivateChannel(JSONObject channel) {

    }

}