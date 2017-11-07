package de.btobastian.javacord.utils.handler.message;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.channels.ServerTextChannel;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.events.message.MessageCreateEvent;
import de.btobastian.javacord.listeners.message.MessageCreateListener;
import de.btobastian.javacord.utils.PacketHandler;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the message create packet.
 */
public class MessageCreateHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public MessageCreateHandler(DiscordApi api) {
        super(api, true, "MESSAGE_CREATE");
    }

    @Override
    public void handle(JSONObject packet) {
        api.getTextChannelById(packet.getString("channel_id")).ifPresent(channel -> {
            Message message = api.getOrCreateMessage(channel, packet);
            MessageCreateEvent event = new MessageCreateEvent(api, message);

            List<MessageCreateListener> listeners = new ArrayList<>();
            listeners.addAll(channel.getMessageCreateListeners());
            if (channel instanceof ServerTextChannel) {
                listeners.addAll(((ServerTextChannel) channel).getServer().getMessageCreateListeners());
            }
            message.getUserAuthor().ifPresent(user -> listeners.addAll(user.getMessageCreateListeners()));
            listeners.addAll(api.getMessageCreateListeners());

            dispatchEvent(listeners, listener -> listener.onMessageCreate(event));
        });
    }

}