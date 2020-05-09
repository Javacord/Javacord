package org.javacord.core.util.handler.guild.role;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.server.role.RoleChangeColorEvent;
import org.javacord.api.event.server.role.RoleChangeHoistEvent;
import org.javacord.api.event.server.role.RoleChangeMentionableEvent;
import org.javacord.api.event.server.role.RoleChangeNameEvent;
import org.javacord.api.event.server.role.RoleChangePermissionsEvent;
import org.javacord.api.event.server.role.RoleChangePositionEvent;
import org.javacord.core.entity.permission.PermissionsImpl;
import org.javacord.core.entity.permission.RoleImpl;
import org.javacord.core.event.server.role.RoleChangeColorEventImpl;
import org.javacord.core.event.server.role.RoleChangeHoistEventImpl;
import org.javacord.core.event.server.role.RoleChangeMentionableEventImpl;
import org.javacord.core.event.server.role.RoleChangeNameEventImpl;
import org.javacord.core.event.server.role.RoleChangePermissionsEventImpl;
import org.javacord.core.event.server.role.RoleChangePositionEventImpl;
import org.javacord.core.util.event.DispatchQueueSelector;
import org.javacord.core.util.gateway.PacketHandler;

import java.awt.Color;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Handles the guild role create packet.
 */
public class GuildRoleUpdateHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public GuildRoleUpdateHandler(DiscordApi api) {
        super(api, true, "GUILD_ROLE_UPDATE");
    }

    @Override
    public void handle(JsonNode packet) {
        JsonNode roleJson = packet.get("role");
        long roleId = roleJson.get("id").asLong();
        api.getRoleById(roleId).map(role -> (RoleImpl) role).ifPresent(role -> {
            Color oldColorObject = role.getColor().orElse(null);
            int oldColor = role.getColorAsInt();
            int newColor = roleJson.get("color").asInt(0);
            if (oldColor != newColor) {
                role.setColor(newColor);

                RoleChangeColorEvent event = new RoleChangeColorEventImpl(
                        role, role.getColor().orElse(null), oldColorObject);

                api.getEventDispatcher().dispatchRoleChangeColorEvent(
                        (DispatchQueueSelector) role.getServer(), role, role.getServer(), event);
            }

            boolean oldHoist = role.isDisplayedSeparately();
            boolean newHoist = roleJson.get("hoist").asBoolean(false);
            if (oldHoist != newHoist) {
                role.setHoist(newHoist);

                RoleChangeHoistEvent event = new RoleChangeHoistEventImpl(role, oldHoist);

                api.getEventDispatcher().dispatchRoleChangeHoistEvent(
                        (DispatchQueueSelector) role.getServer(), role, role.getServer(), event);
            }

            boolean oldMentionable = role.isMentionable();
            boolean newMentionable = roleJson.get("mentionable").asBoolean(false);
            if (oldMentionable != newMentionable) {
                role.setMentionable(newMentionable);

                RoleChangeMentionableEvent event = new RoleChangeMentionableEventImpl(role, oldMentionable);

                api.getEventDispatcher().dispatchRoleChangeMentionableEvent(
                        (DispatchQueueSelector) role.getServer(), role, role.getServer(), event);
            }

            String oldName = role.getName();
            String newName = roleJson.get("name").asText();
            if (!oldName.equals(newName)) {
                role.setName(newName);

                RoleChangeNameEvent event = new RoleChangeNameEventImpl(role, newName, oldName);

                api.getEventDispatcher().dispatchRoleChangeNameEvent(
                        (DispatchQueueSelector) role.getServer(), role, role.getServer(), event);
            }

            Permissions oldPermissions = role.getPermissions();
            PermissionsImpl newPermissions = new PermissionsImpl(roleJson.get("permissions").asInt(), 0);
            if (!oldPermissions.equals(newPermissions)) {
                role.setPermissions(newPermissions);

                RoleChangePermissionsEvent event =
                        new RoleChangePermissionsEventImpl(role, newPermissions, oldPermissions);

                api.getEventDispatcher().dispatchRoleChangePermissionsEvent(
                        (DispatchQueueSelector) role.getServer(), role, role.getServer(), event);
                // If bot is affected remove messages from cache that are no longer visible
                if (role.getUsers().stream().anyMatch(User::isYourself)) {
                    Set<Long> unreadableChannels = role.getServer().getTextChannels().stream()
                            .filter(((Predicate<ServerTextChannel>)ServerTextChannel::canYouSee).negate())
                            .map(ServerTextChannel::getId)
                            .collect(Collectors.toSet());
                    api.forEachCachedMessageWhere(
                            msg -> unreadableChannels.contains(msg.getChannel().getId()),
                            msg -> api.removeMessageFromCache(msg.getId())
                    );
                }
            }

            int oldPosition = role.getPosition();
            int oldRawPosition = role.getRawPosition();
            int newRawPosition = roleJson.get("position").asInt();
            if (oldRawPosition != newRawPosition) {
                role.setRawPosition(newRawPosition);
                int newPosition = role.getPosition();
                RoleChangePositionEvent event = new RoleChangePositionEventImpl(role, newRawPosition, oldRawPosition,
                                                                                            newPosition, oldPosition);

                api.getEventDispatcher().dispatchRoleChangePositionEvent(
                        (DispatchQueueSelector) role.getServer(), role, role.getServer(), event);
            }
        });
    }

}
