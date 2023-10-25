package org.javacord.core.entity.channel;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.Permissionable;
import org.javacord.api.entity.channel.RegularServerChannel;
import org.javacord.api.entity.member.Member;
import org.javacord.api.entity.permission.PermissionState;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.entity.permission.PermissionsBuilder;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.permission.PermissionsImpl;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.util.logging.LoggerUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class RegularServerChannelImpl extends ServerChannelImpl implements RegularServerChannel {

    /**
     * Compares channels according to their "position" field and, if those are the same, their id.
     */
    public static final Comparator<RegularServerChannel> COMPARE_BY_RAW_POSITION = Comparator
            .comparingInt(RegularServerChannel::getRawPosition)
            .thenComparingLong(RegularServerChannel::getId);

    /**
     * A map with all overwritten user permissions.
     */
    private final ConcurrentHashMap<Long, Permissions> overwrittenUserPermissions = new ConcurrentHashMap<>();

    /**
     * A map with all overwritten role permissions.
     */
    private final ConcurrentHashMap<Long, Permissions> overwrittenRolePermissions = new ConcurrentHashMap<>();

    /**
     * The rawPosition of the channel.
     */
    private volatile int rawPosition;

    /**
     * Creates a new server channel object.
     *
     * @param api    The discord api instance.
     * @param server The server of the channel.
     * @param data   The json data of the channel.
     */
    public RegularServerChannelImpl(final DiscordApiImpl api, final ServerImpl server, final JsonNode data) {
        super(api, server, data);
        rawPosition = data.get("position").asInt();

        if (data.has("permission_overwrites")) {
            for (JsonNode permissionOverwrite : data.get("permission_overwrites")) {
                long id = Long.parseLong(permissionOverwrite.has("id") ? permissionOverwrite.get("id").asText() : "-1");
                long allow = permissionOverwrite.has("allow") ? permissionOverwrite.get("allow").asLong() : 0;
                long deny = permissionOverwrite.has("deny") ? permissionOverwrite.get("deny").asLong() : 0;
                Permissions permissions = new PermissionsImpl(allow, deny);
                switch (permissionOverwrite.get("type").asInt()) {
                    case 0:
                        overwrittenRolePermissions.put(id, permissions);
                        break;
                    case 1:
                        overwrittenUserPermissions.put(id, permissions);
                        break;
                    default:
                        LoggerUtil.getLogger(ServerChannelImpl.class).warn("Unknown type for permission_overwrites. "
                                + "Your Javacord version might be outdated.");
                }
            }
        }
    }

    /**
     * Gets the overwritten role permissions.
     *
     * @return The overwritten role permissions.
     */
    public ConcurrentHashMap<Long, Permissions> getInternalOverwrittenRolePermissions() {
        return overwrittenRolePermissions;
    }

    /**
     * Gets the overwritten user permissions.
     *
     * @return The overwritten user permissions.
     */
    public ConcurrentHashMap<Long, Permissions> getInternalOverwrittenUserPermissions() {
        return overwrittenUserPermissions;
    }

    @Override
    public <T extends Permissionable & DiscordEntity> Permissions getOverwrittenPermissions(T permissionable) {
        Map<Long, Permissions> permissionsMap = Collections.emptyMap();
        if (permissionable instanceof User) {
            permissionsMap = overwrittenUserPermissions;
        } else if (permissionable instanceof Role) {
            permissionsMap = overwrittenRolePermissions;
        }
        return permissionsMap.getOrDefault(permissionable.getId(), PermissionsImpl.EMPTY_PERMISSIONS);
    }

    @Override
    public Map<Long, Permissions> getOverwrittenUserPermissions() {
        return Collections.unmodifiableMap(new HashMap<>(overwrittenUserPermissions));
    }

    @Override
    public Map<Long, Permissions> getOverwrittenRolePermissions() {
        return Collections.unmodifiableMap(new HashMap<>(overwrittenRolePermissions));
    }

    @Override
    public Permissions getEffectiveOverwrittenPermissions(Member member) {
        PermissionsBuilder builder = new PermissionsBuilder(PermissionsImpl.EMPTY_PERMISSIONS);
        Server server = getServer();
        Role everyoneRole = server.getEveryoneRole();
        Permissions everyoneRolePermissionOverwrites = getOverwrittenPermissions(everyoneRole);
        for (PermissionType type : PermissionType.values()) {
            if (everyoneRolePermissionOverwrites.getState(type) == PermissionState.DENIED) {
                builder.setState(type, PermissionState.DENIED);
            }
            if (everyoneRolePermissionOverwrites.getState(type) == PermissionState.ALLOWED) {
                builder.setState(type, PermissionState.ALLOWED);
            }
        }
        List<Role> rolesOfUser = new ArrayList<>(member.getRoles());
        rolesOfUser.remove(everyoneRole);
        List<Permissions> permissionOverwrites = rolesOfUser.stream()
                .map(this::getOverwrittenPermissions)
                .collect(Collectors.toList());
        for (Permissions permissions : permissionOverwrites) {
            for (PermissionType type : PermissionType.values()) {
                if (permissions.getState(type) == PermissionState.DENIED) {
                    builder.setState(type, PermissionState.DENIED);
                }
            }
        }
        for (Permissions permissions : permissionOverwrites) {
            for (PermissionType type : PermissionType.values()) {
                if (permissions.getState(type) == PermissionState.ALLOWED) {
                    builder.setState(type, PermissionState.ALLOWED);
                }
            }
        }
        for (PermissionType type : PermissionType.values()) {
            Permissions permissions = overwrittenUserPermissions
                    .getOrDefault(member.getId(), PermissionsImpl.EMPTY_PERMISSIONS);
            if (permissions.getState(type) == PermissionState.DENIED) {
                builder.setState(type, PermissionState.DENIED);
            }
            if (permissions.getState(type) == PermissionState.ALLOWED) {
                builder.setState(type, PermissionState.ALLOWED);
            }
        }
        return builder.build();
    }

    @Override
    public int getRawPosition() {
        return rawPosition;
    }

    /**
     * Sets the raw position of the channel.
     *
     * @param position The new raw position of the channel.
     */
    public void setRawPosition(int position) {
        this.rawPosition = position;
    }
}
