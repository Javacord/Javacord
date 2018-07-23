package org.javacord.core.util.gateway;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.util.logging.LoggerUtil;

import java.util.concurrent.ExecutorService;

/**
 * This class is extended by all PacketHandlers.
 */
public abstract class PacketHandler {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(PacketHandler.class);

    protected final DiscordApiImpl api;
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
        this.api = (DiscordApiImpl) api;
        this.async = async;
        this.type = type;
        if (async) {
            executorService = api.getThreadPool().getSingleThreadExecutorService("Handlers Processor");
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
                            getType(), packet, e);
                }
            });
        } else {
            try {
                handle(packet);
            } catch (Exception e) {
                logger.warn("Couldn't handle packet of type {}. Please contact the developer! (packet: {})",
                        getType(), packet, e);
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