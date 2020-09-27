package org.javacord.core.util.handler.guild;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.event.server.role.UserRoleAddEvent;
import org.javacord.api.event.server.role.UserRoleRemoveEvent;
import org.javacord.api.event.user.UserChangeNicknameEvent;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.entity.user.Member;
import org.javacord.core.entity.user.MemberImpl;
import org.javacord.core.event.server.role.UserRoleAddEventImpl;
import org.javacord.core.event.server.role.UserRoleRemoveEventImpl;
import org.javacord.core.event.user.UserChangeNicknameEventImpl;
import org.javacord.core.util.event.DispatchQueueSelector;
import org.javacord.core.util.gateway.PacketHandler;
import org.javacord.core.util.logging.LoggerUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Handles the guild member update packet.
 */
public class GuildMemberUpdateHandler extends PacketHandler {

    private static final Logger logger = LoggerUtil.getLogger(GuildMemberUpdateHandler.class);

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
        api.getPossiblyUnreadyServerById(packet.get("guild_id").asLong()).map(server -> (ServerImpl) server)
                .ifPresent(server -> {
                    MemberImpl newMember = new MemberImpl(api, server, packet, null);
                    Member oldMember = server.getRealMemberById(newMember.getId()).orElse(null);

                    api.addMemberToCacheOrReplaceExisting(newMember);

                    if (oldMember == null) {
                        // Should only happen shortly after startup and is unproblematic
                        return;
                    }

                    if (!newMember.getNickname().equals(oldMember.getNickname())) {
                        UserChangeNicknameEvent event =
                                new UserChangeNicknameEventImpl(newMember, oldMember);

                        api.getEventDispatcher().dispatchUserChangeNicknameEvent(
                                server, server, newMember.getUser(), event);
                    }

                    if (packet.has("roles")) {
                        JsonNode jsonRoles = packet.get("roles");
                        Collection<Role> newRoles = new HashSet<>();
                        Collection<Role> oldRoles = oldMember.getRoles();
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
                            UserRoleAddEvent event = new UserRoleAddEventImpl(role, newMember);

                            api.getEventDispatcher().dispatchUserRoleAddEvent((DispatchQueueSelector) role.getServer(),
                                    role, role.getServer(), newMember.getId(), event);
                        }

                        // Removed roles
                        Collection<Role> removedRoles = new ArrayList<>(oldRoles);
                        removedRoles.removeAll(intersection);
                        for (Role role : removedRoles) {
                            if (role.isEveryoneRole()) {
                                continue;
                            }
                            UserRoleRemoveEvent event = new UserRoleRemoveEventImpl(role, newMember);

                            api.getEventDispatcher().dispatchUserRoleRemoveEvent(
                                    (DispatchQueueSelector) role.getServer(), role, role.getServer(), newMember.getId(),
                                    event);
                        }
                    }

                    if (newMember.getUser().isYourself()) {
                        Set<Long> unreadableChannels = server.getTextChannels().stream()
                                .filter(((Predicate<ServerTextChannel>)ServerTextChannel::canYouSee).negate())
                                .map(ServerTextChannel::getId)
                                .collect(Collectors.toSet());
                        api.forEachCachedMessageWhere(
                                msg -> unreadableChannels.contains(msg.getChannel().getId()),
                                msg -> api.removeMessageFromCache(msg.getId())
                        );
                    }

                });
    }

}
