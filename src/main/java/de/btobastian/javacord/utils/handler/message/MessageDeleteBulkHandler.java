package de.btobastian.javacord.utils.handler.message;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.channels.ServerTextChannel;
import de.btobastian.javacord.entities.message.impl.ImplMessage;
import de.btobastian.javacord.events.message.MessageDeleteEvent;
import de.btobastian.javacord.listeners.message.MessageDeleteListener;
import de.btobastian.javacord.utils.PacketHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the message delete bulk packet.
 */
public class MessageDeleteBulkHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public MessageDeleteBulkHandler(DiscordApi api) {
        super(api, true, "MESSAGE_DELETE_BULK");
    }

    @Override
    public void handle(JSONObject packet) {
        long channelId = Long.parseLong(packet.getString("channel_id"));

        api.getTextChannelById(channelId).ifPresent(channel -> {
            JSONArray messageIds = packet.getJSONArray("ids");
            for (int i = 0; i < messageIds.length(); i++) {
                long messageId = Long.parseLong(messageIds.getString(i));
                MessageDeleteEvent event = new MessageDeleteEvent(api, messageId, channel);

                List<MessageDeleteListener> listeners = new ArrayList<>();
                api.getCachedMessageById(messageId)
                        .ifPresent(message -> ((ImplMessage) message).setDeleted(true));
                listeners.addAll(api.getMessageDeleteListeners(messageId));
                listeners.addAll(channel.getMessageDeleteListeners());
                if (channel instanceof ServerTextChannel) {
                    listeners.addAll(((ServerTextChannel) channel).getServer().getMessageDeleteListeners());
                }
                listeners.addAll(api.getMessageDeleteListeners());

                dispatchEvent(listeners, listener -> listener.onMessageDelete(event));
            }
        });
    }
}