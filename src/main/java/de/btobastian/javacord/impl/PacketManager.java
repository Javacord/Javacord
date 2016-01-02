package de.btobastian.javacord.impl;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.btobastian.javacord.api.Channel;
import de.btobastian.javacord.api.Message;
import de.btobastian.javacord.api.Role;
import de.btobastian.javacord.api.Server;
import de.btobastian.javacord.api.User;
import de.btobastian.javacord.api.listener.Listener;
import de.btobastian.javacord.api.listener.MessageCreateListener;
import de.btobastian.javacord.api.listener.MessageDeleteListener;
import de.btobastian.javacord.api.listener.MessageEditListener;
import de.btobastian.javacord.api.listener.RoleChangeNameListener;
import de.btobastian.javacord.api.listener.RoleChangePermissionsListener;
import de.btobastian.javacord.api.listener.RoleCreateListener;
import de.btobastian.javacord.api.listener.ServerMemberAddListener;
import de.btobastian.javacord.api.listener.ServerMemberRemoveListener;
import de.btobastian.javacord.api.listener.TypingStartListener;
import de.btobastian.javacord.api.listener.UserChangeNameListener;
import de.btobastian.javacord.api.listener.UserChangeRoleListener;
import de.btobastian.javacord.api.permissions.Permissions;

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
                break;
            case "GUILD_MEMBER_UPDATE":
                onGuildMemberUpdate(json);
                break;
            case "GUILD_MEMBER_ADD":
                onGuildMemberAdd(json);
                break;
            case "GUILD_MEMBER_REMOVE":
                onGuildMemberRemove(json);
                break;
            default:
                if (api.debug()) {
                    System.out.println("Received unknown packet: " + type);
                }
                break;
        }
    }
    
    private void onGuildMemberRemove(JSONObject packet) {
        JSONObject data = packet.getJSONObject("d");
        
        Server server = api.getServerById(data.getString("guild_id"));
        User user = api.getUser(data.getJSONObject("user"));
        if (server != null) {
            ((ImplServer) server).removeUser(user);
            for (Listener listener : api.getListeners(ServerMemberRemoveListener.class)) {
                ((ServerMemberRemoveListener) listener).onServerMemberRemove(api, server, user);
            }
        }
    }
    
    private void onGuildMemberAdd(JSONObject packet) {
        JSONObject data = packet.getJSONObject("d");
        
        Server server = api.getServerById(data.getString("guild_id"));
        User user = api.getUser(data.getJSONObject("user"));
        if (server != null) {
            ((ImplServer) server).addUser(user);
            for (Listener listener : api.getListeners(ServerMemberAddListener.class)) {
                ((ServerMemberAddListener) listener).onServerMemberAdd(api, server, user);
            }
        }
    }
    
    private void onGuildMemberUpdate(JSONObject packet) {
        JSONObject data = packet.getJSONObject("d");
        
        User user = api.getUserById(data.getJSONObject("user").getString("id"));
        Server server = api.getServerById(data.getString("guild_id"));
        
        JSONArray roles = data.getJSONArray("roles");
        List<Role> rolesBefore = new ArrayList<>();
        List<Role> rolesNow = new ArrayList<>();
        
        for (Role role : server.getRoles()) {
            if (role.getUsers().contains(user)) {
                rolesBefore.add(role);
            }
            for (int i = 0; i < roles.length(); i++) {
                if (role.getId().equals(roles.getString(i))) {
                    rolesNow.add(role);
                }
            }
        }
        
        List<Role> newRoles = new ArrayList<>(rolesNow);
        newRoles.removeAll(rolesBefore);
        List<Role> removedRoles = rolesBefore;
        removedRoles.removeAll(rolesNow);
        if (!newRoles.isEmpty()) {
            ((ImplRole) newRoles.get(0)).addUser(user);
            for (Listener listener : api.getListeners(UserChangeRoleListener.class)) {
                ((UserChangeRoleListener) listener).onUserChangeRole(api, user, newRoles.get(0), true);
            }
        }
        if (!removedRoles.isEmpty()) {
            ((ImplRole) removedRoles.get(0)).removeUser(user);
            for (Listener listener : api.getListeners(UserChangeRoleListener.class)) {
                ((UserChangeRoleListener) listener).onUserChangeRole(api, user, removedRoles.get(0), false);
            }
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
            for (Listener listener : api.getListeners(RoleChangeNameListener.class)) {
                ((RoleChangeNameListener) listener).onRoleChangeName(api, role, oldName);
            }
        }
        
        Permissions permissions = new ImplPermissions(roleJson.getInt("permissions"));
        if (!role.getPermission().equals(permissions)) {
            Permissions oldPermissions = role.getPermission();
            ((ImplRole) role).setPermissions(permissions);
            for (Listener listener : api.getListeners(RoleChangePermissionsListener.class)) {
                ((RoleChangePermissionsListener) listener).onRoleChangePermissions(api, role, oldPermissions);
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
                for (Listener listener : api.getListeners(RoleCreateListener.class)) {
                    ((RoleCreateListener) listener).onRoleCreate(api, role);
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
                for (Listener listener : api.getListeners(UserChangeNameListener.class)) {
                    ((UserChangeNameListener) listener).onUserChangeName(api, user, oldName);
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
        for (Listener listener : api.getListeners(MessageCreateListener.class)) {
            ((MessageCreateListener) listener).onMessageCreate(api, message);
        }
    }
    
    private void onMessageDelete(JSONObject packet) {
        JSONObject data = packet.getJSONObject("d");
        
        String messageId = data.getString("id");
        Message message = api.getMessageById(messageId);
        if (message == null) {
            return; // no cached version available
        }
        for (Listener listener : api.getListeners(MessageDeleteListener.class)) {
            ((MessageDeleteListener) listener).onMessageDelete(api, message);
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
        for (Listener listener : api.getListeners(TypingStartListener.class)) {
            ((TypingStartListener) listener).onTypingStart(api, api.getUserById(userId), channel);
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
        
        for (Listener listener : api.getListeners(MessageEditListener.class)) {
            ((MessageEditListener) listener).onMessageEdit(api, message, oldContent);
        }
    }
    
}
