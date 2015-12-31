package de.btobastian.javacord.impl;

import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONObject;

import de.btobastian.javacord.api.listener.ReadyListener;

/**
 * Class for internal purposes!
 */
class DiscordWebsocket extends WebSocketClient {

    private static final String CONNECT_JSON = "{\"op\":2,\"d\":{\"token\":\"%token%\",\"v\":3,\"properties\":{\"$os\":\"Windows\",\"$browser\":\"Chrome\",\"$device\":\"\",\"$referrer\":\" https://discordapp.com/@me\",\"$referring_domain\":\"discordapp.com\"}}}";
    private static final String UPDATE_STATUS_JSON = "{\"op\":3,\"d\":{\"game\":{\"name\":\"%game%\"},\"idle_since\":null}}";
    
    private ImplDiscordAPI api;
    private ReadyListener listener;
    private boolean ready = false;
    private PacketManager packetManager;
    
    protected DiscordWebsocket(URI serverURI, ImplDiscordAPI api, ReadyListener listener) {
        super(serverURI);
        this.api = api;
        this.listener = listener;
        packetManager = new PacketManager(api);
        this.connect();
    }
    
    public boolean isReady() {
        return ready;
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        ready = false;
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
    }

    @Override
    public void onMessage(String message) {
        JSONObject obj = new JSONObject(message);
        if (!obj.getString("t").equals("READY")) {
            packetManager.onPacketReceive(obj);
            return;
        }
        onReadyReceived(obj);
        ready = true;
        listener.onReady();
    }
    
    private void onReadyReceived(JSONObject packet) {
        JSONObject data = packet.getJSONObject("d");  
        
        final long heartbeatInterval = data.getLong("heartbeat_interval");
        
        JSONArray guilds = data.getJSONArray("guilds"); // guild = server
        for (int i = 0; i < guilds.length(); i++) {
            JSONObject guild = guilds.getJSONObject(i);
            new ImplServer(guild, api);
        }
        
        JSONArray privateChannels = data.getJSONArray("private_channels");
        for (int i = 0; i < privateChannels.length(); i++) {
            JSONObject privateChannel = privateChannels.getJSONObject(i);
            String id = privateChannel.getString("id");
            String userId = privateChannel.getJSONObject("recipient").getString("id");
            ((ImplUser) api.getUserById(userId)).setUserChannelId(id);
        }
        
        new Thread(new Runnable() {
            
            @Override
            public void run() {
                long timer = System.currentTimeMillis();
                for (;;) {
                    if ((System.currentTimeMillis() - timer) >= heartbeatInterval - 10) {
                        send(UPDATE_STATUS_JSON.replace("%game%", api.getGame() == null ? "" : api.getGame()));
                        timer = System.currentTimeMillis();
                    }
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        send(CONNECT_JSON.replace("%token%", api.getToken()));
    }
    
}
