package de.btobastian.javacord.utils.handler.message;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.channels.ServerTextChannel;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.impl.ImplMessage;
import de.btobastian.javacord.events.message.MessageCreateEvent;
import de.btobastian.javacord.listeners.message.MessageCreateListener;
import de.btobastian.javacord.utils.PacketHandler;
import de.btobastian.javacord.utils.logging.LoggerUtil;
import org.json.JSONObject;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the message create packet.
 */
public class MessageCreateHandler extends PacketHandler {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(MessageCreateHandler.class);

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
            Message message = new ImplMessage(api, channel, packet);
            MessageCreateEvent event = new MessageCreateEvent(api, message);
            listenerExecutorService.submit(() -> {
                List<MessageCreateListener> listeners = new ArrayList<>();
                listeners.addAll(channel.getMessageCreateListeners());
                if (channel instanceof ServerTextChannel) {
                    listeners.addAll(((ServerTextChannel) channel).getServer().getMessageCreateListeners());
                }
                listeners.addAll(api.getMessageCreateListeners());
                listeners.forEach(listener -> listener.onMessageCreate(event));
            });
        });
    }

}