package de.btobastian.javacord.utils;

import com.fasterxml.jackson.databind.JsonNode;
import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.utils.logging.LoggerUtil;
import org.slf4j.Logger;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

/**
 * This class is extended by all PacketHandlers.
 */
public abstract class PacketHandler {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(PacketHandler.class);

    protected final ImplDiscordApi api;
    private final String type;
    private final boolean async;
    private ExecutorService executorService;

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     * @param async Whether the packet should be handled in a new thread or in the websocket thread.
     * @param type The type of packet the class handles.
     */
    public PacketHandler(DiscordApi api, boolean async, String type) {
        this.api = (ImplDiscordApi) api;
        this.async = async;
        this.type = type;
        if (async) {
            executorService = api.getThreadPool().getSingleThreadExecutorService("handlers");
        }
    }

    /**
     * Handles the packet.
     *
     * @param packet The packet (the "d"-object).
     */
    public void handlePacket(final JsonNode packet) {
        if (async) {
            executorService.submit(() -> {
                try {
                    handle(packet);
                } catch (Exception e) {
                    logger.warn("Couldn't handle packet of type {}. Please contact the developer! (packet: {})",
                            getType(), packet.toString(), e);
                }
            });
        } else {
            try {
                handle(packet);
            } catch (Exception e) {
                logger.warn("Couldn't handle packet of type {}. Please contact the developer! (packet: {})",
                        getType(), packet.toString(), e);
            }
        }
    }

    /**
     * This method is called by the super class to handle the packet.
     *
     * @param packet The packet (the "d"-object).
     */
    protected abstract void handle(JsonNode packet);

    /**
     * Dispatches an event in a the listener thread.
     *
     * @param listeners The listeners for the event.
     * @param consumer The consumer which consumes the listeners and calls the event.
     * @param <T> The listener class.
     * @see DiscordWebSocketAdapter#dispatchEvent(List, Consumer)
     */
    protected <T> void dispatchEvent(List<T> listeners, Consumer<T> consumer) {
        api.getWebSocketAdapter().dispatchEvent(listeners, consumer);
    }

    /**
     * Gets the type of packet the handler handles.
     *
     * @return The type of the packet.
     */
    public String getType() {
        return type;
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof PacketHandler && ((PacketHandler) obj).getType().equals(getType());
    }

}