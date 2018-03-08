package org.javacord.util.handler.guild;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.DiscordApi;
import org.javacord.entity.permission.Role;
import org.javacord.entity.permission.impl.ImplRole;
import org.javacord.entity.server.impl.ImplServer;
import org.javacord.entity.user.User;
import org.javacord.event.server.role.UserRoleAddEvent;
import org.javacord.event.server.role.UserRoleRemoveEvent;
import org.javacord.event.server.role.impl.ImplUserRoleAddEvent;
import org.javacord.event.server.role.impl.ImplUserRoleRemoveEvent;
import org.javacord.event.user.UserChangeNicknameEvent;
import org.javacord.event.user.impl.ImplUserChangeNicknameEvent;
import org.javacord.listener.server.role.UserRoleAddListener;
import org.javacord.listener.server.role.UserRoleRemoveListener;
import org.javacord.listener.user.UserChangeNicknameListener;
import org.javacord.util.gateway.PacketHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

/**
 * Handles the guild member update packet.
 */
public class GuildMemberUpdateHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public GuildMemberUpdateHandler(DiscordApi api) {
        super(api, true, "GUILD_MEMBER_UPDATE");
    }

    @Override
    public void handle(JsonNode packet) {
        api.getServerById(packet.get("guild_id").asText()).map(server -> (ImplServer) server).ifPresent(server -> {
            User user = api.getOrCreateUser(packet.get("user"));
            if (packet.has("nick")) {
                String newNickname = packet.get("nick").asText(null);
                String oldNickname = server.getNickname(user).orElse(null);
                if (!Objects.deepEquals(newNickname, oldNickname)) {
                    server.setNickname(user, newNickname);

                    UserChangeNicknameEvent event =
                            new ImplUserChangeNicknameEvent(user, server, newNickname, oldNickname);

                    List<UserChangeNicknameListener> listeners = new ArrayList<>();
                    listeners.addAll(user.getUserChangeNicknameListeners());
                    listeners.addAll(server.getUserChangeNicknameListeners());
                    listeners.addAll(api.getUserChangeNicknameListeners());

                    api.getEventDispatcher().dispatchEvent(server,
                            listeners, listener -> listener.onUserChangeNickname(event));
                }
            }

            if (packet.has("roles")) {
                JsonNode jsonRoles = packet.get("roles");
                Collection<Role> newRoles = new HashSet<>();
                Collection<Role> oldRoles = server.getRolesOf(user);
                Collection<Role> intersection = new HashSet<>();
                for (JsonNode roleIdJson : jsonRoles) {
                    api.getRoleById(roleIdJson.asText())
                            .map(role -> {
                                newRoles.add(role);
                                return role;
                            })
                            .filter(oldRoles::contains)
                            .ifPresent(intersection::add);
                }

                // Added roles
                Collection<Role> addedRoles = new ArrayList<>(newRoles);
                addedRoles.removeAll(intersection);
                for (Role role : addedRoles) {
                    if (role.isEveryoneRole()) {
                        continue;
                    }
                    ((ImplRole) role).addUserToCache(user);
                    UserRoleAddEvent event = new ImplUserRoleAddEvent(role, user);

                    List<UserRoleAddListener> listeners = new ArrayList<>();
                    listeners.addAll(user.getUserRoleAddListeners());
                    listeners.addAll(role.getUserRoleAddListeners());
                    listeners.addAll(role.getServer().getUserRoleAddListeners());
                    listeners.addAll(api.getUserRoleAddListeners());

                    api.getEventDispatcher().dispatchEvent(server,
                            listeners, listener -> listener.onUserRoleAdd(event));
                }

                // Removed roles
                Collection<Role> removedRoles = new ArrayList<>(oldRoles);
                removedRoles.removeAll(intersection);
                for (Role role : removedRoles) {
                    if (role.isEveryoneRole()) {
                        continue;
                    }
                    ((ImplRole) role).removeUserFromCache(user);
                    UserRoleRemoveEvent event = new ImplUserRoleRemoveEvent(role, user);

                    List<UserRoleRemoveListener> listeners = new ArrayList<>();
                    listeners.addAll(user.getUserRoleRemoveListeners());
                    listeners.addAll(role.getUserRoleRemoveListeners());
                    listeners.addAll(role.getServer().getUserRoleRemoveListeners());
                    listeners.addAll(api.getUserRoleRemoveListeners());

                    api.getEventDispatcher().dispatchEvent(server,
                            listeners, listener -> listener.onUserRoleRemove(event));
                }
            }
        });
    }

}