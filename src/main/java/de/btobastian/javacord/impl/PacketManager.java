package de.btobastian.javacord.impl;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.btobastian.javacord.Channel;
import de.btobastian.javacord.Server;
import de.btobastian.javacord.User;
import de.btobastian.javacord.VoiceChannel;
import de.btobastian.javacord.listener.Listener;
import de.btobastian.javacord.listener.channel.ChannelChangeNameListener;
import de.btobastian.javacord.listener.channel.ChannelChangePositionListener;
import de.btobastian.javacord.listener.channel.ChannelChangeTopicListener;
import de.btobastian.javacord.listener.message.MessageCreateListener;
import de.btobastian.javacord.listener.message.MessageDeleteListener;
import de.btobastian.javacord.listener.message.MessageEditListener;
import de.btobastian.javacord.listener.message.TypingStartListener;
import de.btobastian.javacord.listener.role.RoleChangeNameListener;
import de.btobastian.javacord.listener.role.RoleChangeOverriddenPermissionsListener;
import de.btobastian.javacord.listener.role.RoleChangePermissionsListener;
import de.btobastian.javacord.listener.role.RoleChangePositionListener;
import de.btobastian.javacord.listener.role.RoleCreateListener;
import de.btobastian.javacord.listener.role.RoleDeleteListener;
import de.btobastian.javacord.listener.server.ServerJoinListener;
import de.btobastian.javacord.listener.server.ServerMemberAddListener;
import de.btobastian.javacord.listener.server.ServerMemberRemoveListener;
import de.btobastian.javacord.listener.user.UserChangeNameListener;
import de.btobastian.javacord.listener.user.UserChangeOverriddenPermissionsListener;
import de.btobastian.javacord.listener.user.UserChangeRoleListener;
import de.btobastian.javacord.listener.voice.VoiceChannelChangeNameListener;
import de.btobastian.javacord.listener.voice.VoiceChannelChangePositionListener;
import de.btobastian.javacord.message.Message;
import de.btobastian.javacord.permissions.Permissions;
import de.btobastian.javacord.permissions.Role;

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
            case "GUILD_ROLE_DELETE":
                onGuildRoleDelete(json);
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
            case "GUILD_CREATE":
                onGuildCreate(json);
                break;
            case "CHANNEL_UPDATE":
                onChannelUpdate(json);
                onVoiceChannelUpdate(json);
                break;
            default:
                if (api.debug()) {
                    System.out.println("Received unknown packet: " + type);
                }
                break;
        }
    }
    
    private void onVoiceChannelUpdate(JSONObject packet) {
        JSONObject data = packet.getJSONObject("d");
        if (!data.getString("type").equals("voice")) {
            return;
        }
        Server server = api.getServerById(data.getString("guild_id"));
        ImplVoiceChannel channel = null;
        for (VoiceChannel c : server.getVoiceChannels()) {
            if (c.getId().equals(data.getString("id"))) {
                channel = (ImplVoiceChannel) c;
                break;
            }
        }
        if (channel == null) {
            return;
        }
        // check if name was changed
        String name = data.getString("name");
        if (!channel.getName().equals(name)) {
            String oldName = channel.getName();
            channel.setName(name);
            for (Listener listener : api.getListeners(VoiceChannelChangeNameListener.class)) {
                ((VoiceChannelChangeNameListener) listener).onVoiceChannelChangeName(api, channel, oldName);
            }
        }
        
        // check if position was changed
        int position = data.getInt("position");
        if (channel.getPosition() != position) {
            int oldPosition = channel.getPosition();
            channel.setPosition(position);
            for (Listener listener : api.getListeners(VoiceChannelChangePositionListener.class)) {
                ((VoiceChannelChangePositionListener) listener).onVoiceChannelChangePosition(api, channel, oldPosition);
            }
        }
    }
    
    private void onChannelUpdate(JSONObject packet) {
        JSONObject data = packet.getJSONObject("d");
        if (!data.getString("type").equals("text")) {
            return;
        }
        Server server = api.getServerById(data.getString("guild_id"));
        ImplChannel channel = null;
        for (Channel c : server.getChannels()) {
            if (c.getId().equals(data.getString("id"))) {
                channel = (ImplChannel) c;
                break;
            }
        }
        if (channel == null) {
            return;
        }
        // check if name was changed
        String name = data.getString("name");
        if (!channel.getName().equals(name)) {
            String oldName = channel.getName();
            channel.setName(name);
            for (Listener listener : api.getListeners(ChannelChangeNameListener.class)) {
                ((ChannelChangeNameListener) listener).onChannelChangeName(api, channel, oldName);
            }
        }
        
        // check if position was changed
        int position = data.getInt("position");
        if (channel.getPosition() != position) {
            int oldPosition = channel.getPosition();
            channel.setPosition(position);
            for (Listener listener : api.getListeners(ChannelChangePositionListener.class)) {
                ((ChannelChangePositionListener) listener).onChannelChangePosition(api, channel, oldPosition);
            }
        }
        
        // check if topic was changed
        String topic = data.getString("topic");
        topic = topic == null ? "" : topic;
        if (!channel.getTopic().equals(topic)) {
            String oldTopic = channel.getTopic();
            channel.setTopic(topic);
            for (Listener listener : api.getListeners(ChannelChangeTopicListener.class)) {
                ((ChannelChangeTopicListener) listener).onChannelChangeTopic(api, channel, oldTopic);
            }
        }
        
        // check if overridden permissions were changed
        JSONArray permissionOverrites = data.getJSONArray("permission_overwrites");
        for (int i = 0; i < permissionOverrites.length(); i++) { // iterate throw all overridden permissions
            JSONObject permissionOverrite = permissionOverrites.getJSONObject(i);
            int allow = permissionOverrite.getInt("allow");
            int deny = permissionOverrite.getInt("deny");
            String id = permissionOverrite.getString("id");
            String type = permissionOverrite.getString("type");
            if (type.equals("member")) {
                User user = api.getUserById(id);
                ImplPermissions oldPermissions = (ImplPermissions) channel.getOverriddenPermissions(user);
                if (oldPermissions.getAllow() != allow || oldPermissions.getDeny() != deny) { // if some permissions changed
                    channel.setOverriddenPermissions(user, new ImplPermissions(allow, deny));
                    for (Listener listener : api.getListeners(UserChangeOverriddenPermissionsListener.class)) {
                        ((UserChangeOverriddenPermissionsListener) listener).onUserChangeOverriddenPermissions(api, user, channel, oldPermissions);
                    }
                }
            }
            if (type.equals("role")) {
                ImplRole role = null;
                for (Role r : channel.getServer().getRoles()) {
                    if (r.getId().equals(id)) {
                        role = (ImplRole) r;
                    }
                }
                ImplPermissions oldPermissions = (ImplPermissions) role.getOverriddenPermissions(channel);
                if (oldPermissions.getAllow() != allow || oldPermissions.getDeny() != deny) { // if some permissions changed
                    role.setOverriddenPermissions(channel, new ImplPermissions(allow, deny));
                    for (Listener listener : api.getListeners(RoleChangeOverriddenPermissionsListener.class)) {
                        ((RoleChangeOverriddenPermissionsListener) listener).onRoleChangeOverriddenPermissions(api, role, channel, oldPermissions);
                    }
                }
            }
        }
    }
    
    private void onGuildCreate(JSONObject packet) {
        JSONObject data = packet.getJSONObject("d");
        Server server = new ImplServer(data, api);
        for (Listener listener : api.getListeners(ServerJoinListener.class)) {
            ((ServerJoinListener) listener).onServerJoin(api, server);
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
            ((ImplRole) newRoles.get(0)).addUserWithoutUpdate(user);
            for (Listener listener : api.getListeners(UserChangeRoleListener.class)) {
                ((UserChangeRoleListener) listener).onUserChangeRole(api, user, newRoles.get(0), true);
            }
        }
        if (!removedRoles.isEmpty()) {
            ((ImplRole) removedRoles.get(0)).removeUserWithoutUpdate(user);
            for (Listener listener : api.getListeners(UserChangeRoleListener.class)) {
                ((UserChangeRoleListener) listener).onUserChangeRole(api, user, removedRoles.get(0), false);
            }
        }
    }
    
    private void onGuildRoleDelete(JSONObject packet) {
        JSONObject data = packet.getJSONObject("d");
        
        String guildId = data.getString("guild_id");
        String roleId = data.getString("role_id");
        
        Role role = null;
        Server server = api.getServerById(guildId);
        for (Role r : server.getRoles()) {
            if (r.getId().equals(roleId)) {
                role = r;
            }
        }
        if (role == null) {
            return;
        }
        
        ((ImplRole) role).setPosition(-100);
        ((ImplServer) server).removeRole((ImplRole) role);
        
        for (Listener listener : api.getListeners(RoleDeleteListener.class)) {
            ((RoleDeleteListener) listener).onRoleDelete(api, role);
        }
    }
    
    private void onGuildRoleUpdate(JSONObject packet) {
        JSONObject data = packet.getJSONObject("d");
        
        String guildId = data.getString("guild_id");
        JSONObject roleJson = data.getJSONObject("role");
        
        String roleId = roleJson.getString("id");
        Role role = null;
        for (Role r : api.getServerById(guildId).getRoles()) {
            if (r.getId().equals(roleId)) {
                role = r;
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
        
        synchronized (Role.class) {
            int position = roleJson.getInt("position");
            if (role.getPosition() != position) {
                int oldPosition = role.getPosition();
                ((ImplRole) role).setPosition(position);
                for (Listener listener : api.getListeners(RoleChangePositionListener.class)) {
                    ((RoleChangePositionListener) listener).onRoleChangePosition(api, role, oldPosition);
                }
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
