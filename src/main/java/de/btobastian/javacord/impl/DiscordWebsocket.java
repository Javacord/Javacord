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
        JSONObject connectJSON = new JSONObject();
        connectJSON.put("op", 2);
        JSONObject dataJSON = new JSONObject();
        dataJSON.put("token", api.getToken());
        dataJSON.put("v", 3);
        JSONObject propertiesJSON = new JSONObject();
        propertiesJSON.put("$os", "Linux");
        propertiesJSON.put("$browser", "Chrome");
        propertiesJSON.put("$device", "");
        propertiesJSON.put("$referrer", "https://discordapp.com/@me");
        propertiesJSON.put("$referring_domain", "discordapp.com");
        dataJSON.put("properties", propertiesJSON);
        connectJSON.put("d", dataJSON);
        send(connectJSON.toString());
    }
    
}
