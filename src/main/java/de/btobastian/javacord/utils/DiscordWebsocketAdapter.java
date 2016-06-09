/*
 * Copyright (C) 2016 Bastian Oppermann
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

import com.google.common.util.concurrent.SettableFuture;
import com.neovisionaries.ws.client.*;
import de.btobastian.javacord.ImplDiscordAPI;
import de.btobastian.javacord.utils.handler.ReadyHandler;
import de.btobastian.javacord.utils.handler.ReadyReconnectHandler;
import de.btobastian.javacord.utils.handler.channel.ChannelCreateHandler;
import de.btobastian.javacord.utils.handler.channel.ChannelDeleteHandler;
import de.btobastian.javacord.utils.handler.channel.ChannelUpdateHandler;
import de.btobastian.javacord.utils.handler.message.*;
import de.btobastian.javacord.utils.handler.server.*;
import de.btobastian.javacord.utils.handler.server.role.GuildRoleCreateHandler;
import de.btobastian.javacord.utils.handler.server.role.GuildRoleDeleteHandler;
import de.btobastian.javacord.utils.handler.server.role.GuildRoleUpdateHandler;
import de.btobastian.javacord.utils.handler.user.PresenceUpdateHandler;
import de.btobastian.javacord.utils.handler.user.UserGuildSettingsUpdateHandler;
import org.json.JSONObject;
import org.slf4j.Logger;

import javax.net.ssl.SSLContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

/**
 * The main websocket adapter.
 */
public class DiscordWebsocketAdapter extends WebSocketAdapter {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(DiscordWebsocketAdapter.class);

    private WebSocket socket = null;

    private final SettableFuture<Boolean> ready;
    private final ImplDiscordAPI api;
    private final HashMap<String, PacketHandler> handlers = new HashMap<>();
    private final boolean isReconnect;
    private volatile boolean isClosed = false;

    // received in packet with op = 7
    private String urlForReconnect = null;

    // The last sequence number received
    private int lastSeq = -1;
    private String sessionId = null;

    /**
     * Creates a new instance of this class.
     *
     * @param serverURI The uri of the gateway the socket should connect to.
     * @param api The api.
     * @param reconnect Whether it's a reconnect or not.
     */
    public DiscordWebsocketAdapter(URI serverURI, ImplDiscordAPI api, boolean reconnect) {
        this(serverURI, api, reconnect, null, -1);
    }

    /**
     * Creates a new instance of this class.
     *
     * @param serverURI The uri of the gateway the socket should connect to.
     * @param api The api.
     * @param reconnect Whether it's a reconnect or not.
     */
    public DiscordWebsocketAdapter(URI serverURI, ImplDiscordAPI api, boolean reconnect, String sessionId, int lastSeq) {
        this.api = api;
        this.ready = SettableFuture.create();
        registerHandlers();
        this.isReconnect = reconnect;
        this.sessionId = sessionId;
        this.lastSeq = lastSeq;

        WebSocketFactory factory = new WebSocketFactory();
        try {
            factory.setSSLContext(SSLContext.getDefault());
        } catch (NoSuchAlgorithmException e) {
            logger.warn("An error occurred while setting ssl context", e);
        }
        try {
            socket = factory.createSocket(serverURI);
            socket.addListener(this);
            socket.connect();
        } catch (IOException | WebSocketException e) {
            logger.warn("An error occurred while connecting to websocket", e);
        }
    }

    @Override
    public void onConnected(WebSocket socket, Map<String, List<String>> headers) throws Exception {
        if (isReconnect && sessionId != null && lastSeq >= 0) { // send resume packet
            JSONObject resumePacket = new JSONObject()
                    .put("op", 6)
                    .put("d", new JSONObject()
                        .put("token", api.getToken())
                        .put("session_id", sessionId)
                        .put("seq", lastSeq)
                    );
            logger.debug("Sending resume packet");
            socket.sendText(resumePacket.toString());
        } else { // send "normal" payload
            JSONObject connectPacket = new JSONObject()
                    .put("op", 2)
                    .put("d", new JSONObject()
                            .put("token", api.getToken())
                            .put("v", 3)
                            .put("properties", new JSONObject()
                                    .put("$os", System.getProperty("os.name"))
                                    .put("$browser", "None")
                                    .put("$device", "")
                                    .put("$referrer", "https://discordapp.com/@me")
                                    .put("$referring_domain", "discordapp.com"))
                            .put("large_threshold", 250)
                            .put("compress", true));
            logger.debug("Sending connect packet");
            socket.sendText(connectPacket.toString());
        }
    }

    @Override
    public void onDisconnected(WebSocket socket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame,
                               boolean closedByServer) throws Exception {
        logger.info("Websocket closed with reason {} and code {}{}",
                serverCloseFrame != null ? serverCloseFrame.getCloseReason() : "unknown",
                serverCloseFrame != null ? serverCloseFrame.getCloseCode() : "unknown",
                closedByServer ? " by server" : "");
        isClosed = true;
        if (closedByServer && urlForReconnect != null) {
            logger.info("Trying to reconnect (we received op 7 before)...");
            api.reconnectBlocking(urlForReconnect, sessionId, lastSeq);
            logger.info("Reconnected!");
        } else if (closedByServer && api.isAutoReconnectEnabled()) {
            logger.info("Trying to auto-reconnect...");
            api.reconnectBlocking(api.requestGatewayBlocking(), sessionId, lastSeq);
            logger.info("Reconnected!");
        } else if (!closedByServer && (clientCloseFrame == null || clientCloseFrame.getCloseCode() != 1000)) {
            logger.info("Trying to auto-reconnect...");
            api.reconnectBlocking(api.requestGatewayBlocking(), sessionId, lastSeq);
            logger.info("Reconnected!");
        }
        if (!ready.isDone()) {
            ready.set(false);
        }
    }

    @Override
    public void onConnectError(WebSocket socket, WebSocketException exception) throws Exception {
        logger.warn("Could not connect to websocket!", exception);
    }

    @Override
    public void onError(WebSocket socket, WebSocketException cause) throws Exception {
        logger.warn("Websocket error!", cause);
    }

    @Override
    public void onMessageError(WebSocket socket, WebSocketException cause, List<WebSocketFrame> frames)
            throws Exception {
        logger.warn("Websocket message error!", cause);
    }

    @Override
    public void onSendError(WebSocket socket, WebSocketException cause, WebSocketFrame frame) throws Exception {
        logger.warn("Websocket error!", cause);
    }

    @Override
    public void onSendingHandshake(WebSocket socket, String requestLine, List<String[]> headers) throws Exception {
        Thread.currentThread().setName("Websocket");
    }

    @Override
    public void onTextMessage(WebSocket socket, String text) throws Exception {
        JSONObject obj = new JSONObject(text);

        int op = obj.getInt("op");
        if (op == 7) {
            urlForReconnect = obj.getJSONObject("d").getString("url");
            return;
        }

        lastSeq = obj.getInt("s");

        JSONObject packet = obj.getJSONObject("d");
        String type = obj.getString("t");

        if (type.equals("READY") && isReconnect) {
            // we would get some errors if we do not handle the missed data
            handlers.get("READY_RECONNECT").handlePacket(packet);
            ready.set(true);
            updateStatus();
            return; // do not handle the ready packet twice
        }

        // We don't receive a ready packet if we are resuming
        if (isReconnect && !ready.isDone() && sessionId != null && lastSeq >= 0) {
            ready.set(true);
            updateStatus();
            return; // do not handle the ready packet twice
        }

        PacketHandler handler = handlers.get(type);
        if (handler != null) {
            handler.handlePacket(packet);
        } else {
            logger.debug("Received unknown packet of type {} (packet: {})", type, obj.toString());
        }
        if (type.equals("READY")) {
            if (api.isWaitingForServersOnStartup()) {
                api.getThreadPool().getExecutorService().submit(new Runnable() {
                    @Override
                    public void run() {
                        int amount = api.getServers().size();
                        for (;;) {
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException ignored) { }
                            if (api.getServers().size() <= amount) {
                                break; // two seconds without new servers becoming available
                            }
                            amount = api.getServers().size();
                        }
                        ready.set(true);
                    }
                });
            } else {
                ready.set(true);
            }
            logger.debug("Received READY-packet!");
            updateStatus();
        }
    }

    @Override
    public void onBinaryMessage(WebSocket socket, byte[] binary) throws Exception {
        Inflater decompressor = new Inflater();
        decompressor.setInput(binary);
        ByteArrayOutputStream bos = new ByteArrayOutputStream(binary.length);
        byte[] buf = new byte[1024];
        while (!decompressor.finished()) {
            int count;
            try {
                count = decompressor.inflate(buf);
            } catch (DataFormatException e) {
                logger.warn("An error occurred while decompressing data", e);
                return;
            }
            bos.write(buf, 0, count);

        }
        try {
            bos.close();
        } catch (IOException ignored) { }
        byte[] decompressedData = bos.toByteArray();
        try {
            onTextMessage(socket, new String(decompressedData, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            logger.warn("An error occurred while decompressing data", e);
        }
    }

    /**
     * Gets the websocket of the adapter.
     *
     * @return The websocket of the adapter.
     */
    public WebSocket getWebSocket() {
        return socket;
    }

    /**
     * Gets the Future which tells whether the connection is ready or failed.
     *
     * @return The Future.
     */
    public Future<Boolean> isReady() {
        return ready;
    }

    /**
     * Starts to send the heartbeat.
     *
     * @param heartbeatInterval The heartbeat interval received in the ready packet.
     */
    public void startHeartbeat(final long heartbeatInterval) {
        api.getThreadPool().getExecutorService().submit(new Runnable() {
            @Override
            public void run() {
                long timer = System.currentTimeMillis();
                while (!isClosed) {
                    if ((System.currentTimeMillis() - timer) >= heartbeatInterval - 10) {
                        JSONObject heartbeat = new JSONObject()
                                .put("op", 1)
                                .put("d", System.currentTimeMillis());
                        socket.sendText(heartbeat.toString());
                        timer = System.currentTimeMillis();
                        logger.debug("Sending heartbeat (interval: {})", heartbeatInterval);
                        if (Math.random() < 0.1) {
                            // some random status updates to ensure the game and idle status is updated correctly
                            // (discord only accept 5 of these packets per minute and ignores more
                            //  so some might get lost).
                            updateStatus();
                        }
                    }
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        logger.warn("An error occurred while sending heartbeat", e);
                    }
                }
            }
        });
    }

    /**
     * Sends the update status packet
     */
    public void updateStatus() {
        logger.debug(
                "Updating status (game: {}, idle: {})", api.getGame() == null ? "none" : api.getGame(), api.isIdle());
        JSONObject game = new JSONObject();
        game.put("name", api.getGame() == null ? JSONObject.NULL : api.getGame());
        if (api.getStreamingUrl() != null) {
            game.put("url", api.getStreamingUrl()).put("type", 1);
        }
        JSONObject updateStatus = new JSONObject()
                .put("op", 3)
                .put("d", new JSONObject()
                        .put("game", game)
                        .put("idle_since", api.isIdle() ? 1 : JSONObject.NULL));
        socket.sendText(updateStatus.toString());
    }

    /**
     * Sets the session id received in the ready packet.
     *
     * @param sessionId The session id to set.
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * Registers all handlers.
     */
    private void registerHandlers() {
        // general
        addHandler(new ReadyHandler(api));
        addHandler(new ReadyReconnectHandler(api));

        // channel
        addHandler(new ChannelCreateHandler(api));
        addHandler(new ChannelDeleteHandler(api));
        addHandler(new ChannelUpdateHandler(api));

        // message
        addHandler(new MessageAckHandler(api));
        addHandler(new MessageCreateHandler(api));
        addHandler(new MessageDeleteHandler(api));
        addHandler(new MessageUpdateHandler(api));
        addHandler(new TypingStartHandler(api));

        // server
        addHandler(new GuildBanAddHandler(api));
        addHandler(new GuildBanRemoveHandler(api));
        addHandler(new GuildCreateHandler(api));
        addHandler(new GuildDeleteHandler(api));
        addHandler(new GuildMemberAddHandler(api));
        addHandler(new GuildMemberRemoveHandler(api));
        addHandler(new GuildMemberUpdateHandler(api));
        addHandler(new GuildUpdateHandler(api));

        // role
        addHandler(new GuildRoleCreateHandler(api));
        addHandler(new GuildRoleDeleteHandler(api));
        addHandler(new GuildRoleUpdateHandler(api));

        // user
        addHandler(new PresenceUpdateHandler(api));
        addHandler(new UserGuildSettingsUpdateHandler(api));
    }

    /**
     * Adds a packet handler.
     *
     * @param handler The handler to add.
     */
    private void addHandler(PacketHandler handler) {
        handlers.put(handler.getType(), handler);
    }
}
