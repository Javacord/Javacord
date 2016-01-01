package de.btobastian.javacord.impl;

import org.json.JSONException;
import org.json.JSONObject;

import de.btobastian.javacord.api.Channel;
import de.btobastian.javacord.api.Message;
import de.btobastian.javacord.api.Role;
import de.btobastian.javacord.api.Server;
import de.btobastian.javacord.api.User;
import de.btobastian.javacord.api.listener.MessageCreateListener;
import de.btobastian.javacord.api.listener.MessageDeleteListener;
import de.btobastian.javacord.api.listener.MessageEditListener;
import de.btobastian.javacord.api.listener.RoleChangeNameListener;
import de.btobastian.javacord.api.listener.RoleCreateListener;
import de.btobastian.javacord.api.listener.TypingStartListener;
import de.btobastian.javacord.api.listener.UserChangeNameListener;

class PacketManager {

    private ImplDiscordAPI api;
    
    /**
     * Class constructor.
     * 
     * @param api The api.
     */
    public PacketManager(ImplDiscordAPI api) {
        this.api = api;
    }
    
    /**
     * Called when a packet was received.
     * 
     * @param json The packet.
     */
    public void onPacketReceive(JSONObject json) {
        String type = json.getString("t");
        switch (type) {
            case "MESSAGE_CREATE":
                onMessageCreate(json);
                break;
            case "TYPING_START":
                onTypingStart(json);
                break;
            case "MESSAGE_DELETE":
                onMessageDelete(json);
                break;
            case "MESSAGE_UPDATE":
                onMessageUpdate(json);
                break;
            case "PRESENCE_UPDATE":
                onPresenceUpdate(json);
                break;
            case "GUILD_ROLE_CREATE":
                onGuildRoleCreate(json);
                break;
            case "GUILD_ROLE_UPDATE":
                onGuildRoleUpdate(json);
            default:
                if (api.debug()) {
                    System.out.println("Received unknown packet: " + type);
                }
                break;
        }
    }
    
    private void onGuildRoleUpdate(JSONObject packet) {
        JSONObject data = packet.getJSONObject("d");
        
        String guildId = data.getString("guild_id");
        JSONObject roleJson = data.getJSONObject("role");
        
        String roleId = roleJson.getString("id");
        Role role = null;
        for (Server server : api.getServers()) {
            if (server.getId().equals(guildId)) {
                for (Role r : server.getRoles()) {
                    if (r.getId().equals(roleId)) {
                        role = r;
                    }
                }
            }
        }
        if (role == null) {
            return;
        }
        
        String roleName = roleJson.getString("name");
        if (!roleName.equals(role.getName())) {
            String oldName = role.getName();
            ((ImplRole) role).setName(roleName);
            for (RoleChangeNameListener listener : api.getRoleChangeNameListeners()) {
                listener.onRoleChangeName(api, role, oldName);
            }
        }
    }
    
    private void onGuildRoleCreate(JSONObject packet) {
        JSONObject data = packet.getJSONObject("d");
        
        String guildId = data.getString("guild_id");
        JSONObject roleJson = data.getJSONObject("role");
        
        for (Server server : api.getServers()) {
            if (server.getId().equals(guildId)) {
                Role role = new ImplRole(roleJson, (ImplServer) server);
                for (RoleCreateListener listener : api.getRoleCreateListeners()) {
                    listener.onRoleCreate(api, role);
                }
            }
        }
    }
    
    private void onPresenceUpdate(JSONObject packet) {
        JSONObject data = packet.getJSONObject("d");
        
        String userId = data.getJSONObject("user").getString("id");
        try {
            String userName = data.getJSONObject("user").getString("username");
            User user = api.getUserById(userId);
            String oldName = user.getName();
            if (!user.getName().equals(userName)) {
                ((ImplUser) user).setName(userName);
                for (UserChangeNameListener listener : api.getUserChangeNameListeners()) {
                    listener.onUserChangeName(api, user, oldName);
                }
            }
        } catch (JSONException e) {
        }
    }
    
    private void onMessageCreate(JSONObject packet) {
        JSONObject data = packet.getJSONObject("d");
        
        String messageId = data.getString("id");        
        Message message = api.getMessageById(messageId);
        if (message == null) {
            message = new ImplMessage(data, api, null);
        }
        for (MessageCreateListener listener : api.getMessageCreateListeners()) {
            listener.onMessageCreate(api, message);
        }
    }
    
    private void onMessageDelete(JSONObject packet) {
        JSONObject data = packet.getJSONObject("d");
        
        String messageId = data.getString("id");
        Message message = api.getMessageById(messageId);
        if (message == null) {
            return; // no cached version available
        }
        for (MessageDeleteListener listener : api.getMessageDeleteListeners()) {
            listener.onMessageDelete(api, message);
        }
    }
    
    private void onTypingStart(JSONObject packet) {
        JSONObject data = packet.getJSONObject("d");
        
        String channelId = data.getString("channel_id");
        Channel channel = null;
        outer: for (Server server : api.getServers()) {
            for (Channel c : server.getChannels()) {
                if (c.getId().equals(channelId)) {
                    channel = c;
                    break outer;
                }
            }
        }

        String userId = data.getString("user_id");
        for (TypingStartListener listener : api.getTypingStartListeners()) {
            listener.onTypingStart(api, api.getUserById(userId), channel);
        }
    }
    
    private void onMessageUpdate(JSONObject packet) {
        JSONObject data = packet.getJSONObject("d");
        
        String messageId = data.getString("id");
        Message message = api.getMessageById(messageId);
        if (message == null) {
            return;
        }
        String oldContent = message.getContent();
        ((ImplMessage) message).update(data);
        
        for (MessageEditListener listener : api.getMessageEditListeners()) {
            listener.onMessageEdit(api, message, oldContent);
        }
    }
    
}
