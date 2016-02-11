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
package de.btobastian.javacord.utils.handler.channel;

import de.btobastian.javacord.ImplDiscordAPI;
import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.impl.ImplChannel;
import de.btobastian.javacord.entities.permissions.Permissions;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.javacord.entities.permissions.impl.ImplPermissions;
import de.btobastian.javacord.entities.permissions.impl.ImplRole;
import de.btobastian.javacord.listener.Listener;
import de.btobastian.javacord.listener.channel.ChannelChangeNameListener;
import de.btobastian.javacord.listener.channel.ChannelChangePositionListener;
import de.btobastian.javacord.listener.channel.ChannelChangeTopicListener;
import de.btobastian.javacord.listener.role.RoleChangeOverwrittenPermissionsListener;
import de.btobastian.javacord.listener.user.UserChangeOverwrittenPermissionsListener;
import de.btobastian.javacord.utils.PacketHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Handles the channel update packet.
 */
public class ChannelUpdateHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public ChannelUpdateHandler(ImplDiscordAPI api) {
        super(api, true, "CHANNEL_UPDATE");
    }

    @Override
    public void handle(JSONObject packet) {
        boolean isPrivate = packet.getBoolean("is_private");
        if (isPrivate) {
            return; // TODO ignored atm
        }
        Server server = api.getServerById(packet.getString("guild_id"));
        if (packet.getString("type").equals("text")) {
            handleServerTextChannel(packet, server);
        } else {
            handleServerVoiceChannel(packet, server);
        }
    }

    /**
     * Handles the server text channels.
     *
     * @param packet The packet (the "d"-object).
     * @param server The server of the channel.
     */
    private void handleServerTextChannel(JSONObject packet, Server server) {
        ImplChannel channel = null;
        for (Channel c : server.getChannels()) {
            if (c.getId().equals(packet.getString("id"))) {
                channel = (ImplChannel) c;
                break;
            }
        }
        if (channel == null) {
            return; // no channel with the given id was found
        }

        String name = packet.getString("name");
        if (!channel.getName().equals(name)) {
            String oldName = channel.getName();
            channel.setName(name);
            List<Listener> listeners =  api.getListeners(ChannelChangeNameListener.class);
            synchronized (listeners) {
                for (Listener listener : listeners) {
                    ((ChannelChangeNameListener) listener).onChannelChangeName(api, channel, oldName);
                }
            }
        }

        String topic = packet.getString("topic");
        if ((channel.getTopic() != null && topic == null)
                || (channel.getTopic() == null && topic != null)
                || (channel.getTopic() != null && !channel.getTopic().equals(topic))) {
            String oldTopic = channel.getTopic();
            channel.setTopic(topic);
            List<Listener> listeners =  api.getListeners(ChannelChangeTopicListener.class);
            synchronized (listeners) {
                for (Listener listener : listeners) {
                    ((ChannelChangeTopicListener) listener).onChannelChangeTopic(api, channel, oldTopic);
                }
            }
        }

        int position = packet.getInt("position");
        if (channel.getPosition() != position) {
            int oldPosition = channel.getPosition();
            channel.setPosition(position);
            List<Listener> listeners =  api.getListeners(ChannelChangePositionListener.class);
            synchronized (listeners) {
                for (Listener listener : listeners) {
                    ((ChannelChangePositionListener) listener).onChannelChangePosition(api, channel, oldPosition);
                }
            }
        }

        JSONArray permissionOverwrites = packet.getJSONArray("permission_overwrites");
        for (int i = 0; i < permissionOverwrites.length(); i++) {
            JSONObject permissionOverwrite = permissionOverwrites.getJSONObject(i);
            int allow = permissionOverwrite.getInt("allow");
            int deny = permissionOverwrite.getInt("deny");
            String id = permissionOverwrite.getString("id");
            String type = permissionOverwrite.getString("type");

            // permissions overwritten by users
            if (type.equals("member")) {
                User user = api.getUserById(id);
                ImplPermissions permissions = new ImplPermissions(allow, deny);
                Permissions oldPermissions = channel.getOverwrittenPermissions(user);
                if (!oldPermissions.equals(permissions)) {
                    channel.setOverwrittenPermissions(user, permissions);
                    List<Listener> listeners =  api.getListeners(UserChangeOverwrittenPermissionsListener.class);
                    synchronized (listeners) {
                        for (Listener listener : listeners) {
                            ((UserChangeOverwrittenPermissionsListener) listener).onUserChangeOverwrittenPermissions(
                                    api, user, channel, oldPermissions);
                        }
                    }
                }

            }

            // permissions overwritten by roles
            if (type.equals("role")) {
                Role role = channel.getServer().getRoleById(id);
                ImplPermissions permissions = new ImplPermissions(allow, deny);
                Permissions oldPermissions = role.getOverwrittenPermissions(channel);
                if (!permissions.equals(oldPermissions)) {
                    ((ImplRole) role).setOverwrittenPermissions(channel, permissions);
                    List<Listener> listeners =  api.getListeners(RoleChangeOverwrittenPermissionsListener.class);
                    synchronized (listeners) {
                        for (Listener listener : listeners) {
                            ((RoleChangeOverwrittenPermissionsListener) listener).onRoleChangeOverwrittenPermissions(
                                    api, role, channel, oldPermissions);
                        }
                    }
                }
            }
        }
    }

    /**
     * Handles the server voice channels.
     *
     * @param packet The packet (the "d"-object).
     * @param server The server of the channel.
     */
    private void handleServerVoiceChannel(JSONObject packet, Server server) {

    }

}
