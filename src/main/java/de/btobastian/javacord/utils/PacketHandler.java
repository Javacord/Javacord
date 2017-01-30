/*
 * Copyright (C) 2017 Bastian Oppermann
 * 
 * This file is part of Javacord.
 * 
 * Javacord is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser general Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 * 
 * Javacord is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, see <http://www.gnu.org/licenses/>.
 */
package de.btobastian.javacord.utils;

import de.btobastian.javacord.ImplDiscordAPI;
import org.json.JSONObject;
import org.slf4j.Logger;

import java.util.concurrent.ExecutorService;

/**
 * This class is extended by all PacketHandlers.
 */
public abstract class PacketHandler {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(PacketHandler.class);

    protected final ImplDiscordAPI api;
    private final String type;
    private final boolean async;
    private ExecutorService executorService;
    protected final ExecutorService listenerExecutorService;

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     * @param async Whether the packet should be handled in a new thread or in the websocket thread.
     * @param type The type of packet the class handles.
     */
    public PacketHandler(ImplDiscordAPI api, boolean async, String type) {
        this.api = api;
        this.async = async;
        this.type = type;
        if (async) {
            executorService = api.getThreadPool().getSingleThreadExecutorService("handlers");
        }
        listenerExecutorService = api.getThreadPool().getSingleThreadExecutorService("listeners");
    }

    /**
     * Handles the packet.
     *
     * @param packet The packet (the "d"-object).
     */
    public void handlePacket(final JSONObject packet) {
        if (async) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        handle(packet);
                    } catch (Exception e) {
                        logger.warn("Couldn't handle packet of type {}. Please contact the developer! (packet: {})",
                                getType(), packet.toString(), e);
                    }
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
    protected abstract void handle(JSONObject packet);

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
