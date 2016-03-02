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

import de.btobastian.javacord.ImplDiscordAPI;
import de.btobastian.javacord.entities.VoiceChannel;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

/**
 * The websocket which is used to connect to discord.
 */
public class DiscordVoiceWebsocket extends WebSocketClient {

    private volatile boolean isClosed = false;

    private final ImplDiscordAPI api;
    private final VoiceChannel channel;
    private final String token;
    private final String sessionId;

    /**
     * Creates a new instance of this class.
     *
     * @param serverURI The uri of the gateway the socket should connect to.
     * @param api The api.
     * @param channel The channel.
     * @param token The token.
     * @param sessionId The session id.
     */
    public DiscordVoiceWebsocket(
            URI serverURI, ImplDiscordAPI api, VoiceChannel channel, String token, String sessionId) {
        super(serverURI);
        this.api = api;
        this.channel = channel;
        this.token = token;
        this.sessionId = sessionId;
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {

    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
    }

    @Override
    public void onMessage(String message) {
        JSONObject obj = new JSONObject(message);

        int op = obj.getInt("op");
        if (op == 2) {
            JSONObject data = obj.getJSONObject("d");
            int port = data.getInt("port");
            int ssrc = data.getInt("ssrc");
            int heartbeatInterval = data.getInt("heartbeat_interval");
            JSONArray jsonModes = data.getJSONArray("modes");
            String[] modes = new String[jsonModes.length()];
            for (int i = 0; i < jsonModes.length(); i++) {
                modes[i] = jsonModes.getString(i);
            }
        }
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        JSONObject payloadPacket = new JSONObject()
                .put("op", 0)
                .put("d", new JSONObject()
                    .put("server_id", channel.getServer().getId())
                    .put("user_id", api.getYourself().getId())
                    .put("session_id", sessionId)
                    .put("token", token));
        send(payloadPacket.toString());
    }

    @Override
    public void onMessage(ByteBuffer bytes) {
        byte[] compressedData = bytes.array();
        Inflater decompressor = new Inflater();
        decompressor.setInput(compressedData);
        ByteArrayOutputStream bos = new ByteArrayOutputStream(compressedData.length);
        byte[] buf = new byte[1024];
        while (!decompressor.finished()) {
            int count;
            try {
                count = decompressor.inflate(buf);
            } catch (DataFormatException e) {
                e.printStackTrace();
                System.exit(-1);
                return;
            }
            bos.write(buf, 0, count);

        }
        try {
            bos.close();
        } catch (IOException ignored) { }
        byte[] decompressedData = bos.toByteArray();
        try {
            onMessage(new String(decompressedData, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        isClosed = true;
        super.close();
    }

    @Override
    public void closeBlocking() throws InterruptedException {
        isClosed = true;
        super.closeBlocking();
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
                                .put("op", 3)
                                .put("d", System.currentTimeMillis());
                        send(heartbeat.toString());
                        timer = System.currentTimeMillis();
                    }
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

}
