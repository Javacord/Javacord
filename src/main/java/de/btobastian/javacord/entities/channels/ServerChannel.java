package de.btobastian.javacord.entities.channels;

import com.mashape.unirest.http.HttpMethod;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.permissions.PermissionState;
import de.btobastian.javacord.entities.permissions.PermissionType;
import de.btobastian.javacord.entities.permissions.Permissions;
import de.btobastian.javacord.entities.permissions.PermissionsBuilder;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.javacord.entities.permissions.impl.ImplPermissions;
import de.btobastian.javacord.listeners.server.channel.ServerChannelChangeNameListener;
import de.btobastian.javacord.listeners.server.channel.ServerChannelChangeOverwrittenPermissionsListener;
import de.btobastian.javacord.listeners.server.channel.ServerChannelChangePositionListener;
import de.btobastian.javacord.listeners.server.channel.ServerChannelDeleteListener;
import de.btobastian.javacord.utils.rest.RestEndpoint;
import de.btobastian.javacord.utils.rest.RestRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * This class represents a server channel.
 */
public interface ServerChannel extends Channel {

    /**
     * Gets the name of the channel.
     *
     * @return The name of the channel.
     */
    String getName();

    /**
     * Gets the server of the channel.
     *
     * @return The server of the channel.
     */
    Server getServer();

    /**
     * Gets the position of the channel.
     *
     * @return The position of the channel.
     */
    int getPosition();

    /**
     * Gets the overwritten permissions of a user in this channel.
     *
     * @param user The user.
     * @return The overwritten permissions of a user.
     */
    Permissions getOverwrittenPermissions(User user);

    /**
     * Gets the overwritten permissions of a role in this channel.
     *
     * @param role The role.
     * @return The overwritten permissions of a role.
     */
    Permissions getOverwrittenPermissions(Role role);

    /**
     * Gets the effective overwritten permissions of a user.
     * This method also takes into account the roles of the user and their hierarchy.
     * It doesn't take into account the "global" permissions!
     *
     * @param user The user.
     * @return The effective overwritten permissions of the user.
     */
    default Permissions getEffectiveOverwrittenPermissions(User user) {
        PermissionsBuilder builder = new PermissionsBuilder(ImplPermissions.EMPTY_PERMISSIONS);
        List<Permissions> permissionOverwrites = new ArrayList<>();
        for (Role role : getServer().getRolesOf(user)) {
            permissionOverwrites.add(getOverwrittenPermissions(role));
        }
        permissionOverwrites.add(getOverwrittenPermissions(user));
        for (Permissions permissions : permissionOverwrites) {
            Arrays.stream(PermissionType.values())
                    .filter(type -> permissions.getState(type) != PermissionState.NONE)
                    .forEachOrdered(type -> builder.setState(type, permissions.getState(type)));
        }
        return builder.build();
    }

    /**
     * Gets the effective permissions of a user in this channel.
     * The returned permission object will only have {@link PermissionState#ALLOWED} and
     * {@link PermissionState#DENIED} states!
     * It takes into account global permissions and the effective overwritten permissions of a user.
     * Remember, that some permissions affect others!
     * E.g. a user who has {@link PermissionType#SEND_MESSAGES} but not {@link PermissionType#READ_MESSAGES} cannot
     * send messages, even though he has the {@link PermissionType#SEND_MESSAGES} permission.
     *
     * @param user The user.
     * @return The effective permissions of the user in this channel.
     */
    default Permissions getEffectivePermissions(User user) {
        if (getServer().getOwner() == user) {
            return getServer().getPermissionsOf(user);
        }
        PermissionsBuilder builder = new PermissionsBuilder(getServer().getPermissionsOf(user));
        Permissions effectiveOverwrittenPermissions = getEffectiveOverwrittenPermissions(user);
        Arrays.stream(PermissionType.values())
                .filter(type -> effectiveOverwrittenPermissions.getState(type) != PermissionState.NONE)
                .forEachOrdered(type -> builder.setState(type, effectiveOverwrittenPermissions.getState(type)));
        Arrays.stream(PermissionType.values())
                .filter(type -> builder.getState(type) == PermissionState.NONE)
                .forEachOrdered(type -> builder.setState(type, PermissionState.DENIED));
        return builder.build();
    }

    /**
     * Gets the effective allowed permissions of a user in this channel.
     * It takes into account global permissions and the effective overwritten permissions of a user.
     * Remember, that some permissions affect others!
     * E.g. a user who has {@link PermissionType#SEND_MESSAGES} but not {@link PermissionType#READ_MESSAGES} cannot
     * send messages, even though he has the {@link PermissionType#SEND_MESSAGES} permission.
     *
     * @param user The user.
     * @return The effective allowed permissions of a user in this channel.
     */
    default Collection<PermissionType> getEffectiveAllowedPermissions(User user) {
        Permissions effectivePermissions = getEffectivePermissions(user);
        return Arrays.stream(PermissionType.values())
                .filter(type -> effectivePermissions.getState(type) == PermissionState.ALLOWED)
                .collect(Collectors.toSet());
    }

    /**
     * Gets the effective denied permissions of a user in this channel.
     * It takes into account global permissions and the effective overwritten permissions of a user.
     * Remember, that some permissions affect others!
     * E.g. a user who has {@link PermissionType#SEND_MESSAGES} but not {@link PermissionType#READ_MESSAGES} cannot
     * send messages, even though he has the {@link PermissionType#SEND_MESSAGES} permission.
     *
     * @param user The user.
     * @return The effective denied permissions of a user in this channel.
     */
    default Collection<PermissionType> getEffectiveDeniedPermissions(User user) {
        Permissions effectivePermissions = getEffectivePermissions(user);
        return Arrays.stream(PermissionType.values())
                .filter(type -> effectivePermissions.getState(type) == PermissionState.DENIED)
                .collect(Collectors.toSet());
    }

    /**
     * Deletes the channel.
     *
     * @return A future to tell us if the deletion was successful.
     */
    default CompletableFuture<Void> delete() {
        return new RestRequest<Void>(getApi(), HttpMethod.DELETE, RestEndpoint.CHANNEL)
                .setUrlParameters(String.valueOf(getId()))
                .execute(res -> null);
    }

    /**
     * Adds a listener, which listens to this channel being deleted.
     *
     * @param listener The listener to add.
     */
    void addServerChannelDeleteListener(ServerChannelDeleteListener listener);

    /**
     * Gets a list with all registered server channel delete listeners.
     *
     * @return A list with all registered server channel delete listeners.
     */
    List<ServerChannelDeleteListener> getServerChannelDeleteListeners();

    /**
     * Adds a listener, which listens this server channel name changes.
     *
     * @param listener The listener to add.
     */
    void addServerChannelChangeNameListener(ServerChannelChangeNameListener listener);

    /**
     * Gets a list with all registered server channel change name listeners.
     *
     * @return A list with all registered server channel change name listeners.
     */
    List<ServerChannelChangeNameListener> getServerChannelChangeNameListeners();

    /**
     * Adds a listener, which listens this server channel position changes.
     *
     * @param listener The listener to add.
     */
    void addServerChannelChangePositionListener(ServerChannelChangePositionListener listener);

    /**
     * Gets a list with all registered server channel change position listeners.
     *
     * @return A list with all registered server channel change position listeners.
     */
    List<ServerChannelChangePositionListener> getServerChannelChangePositionListeners();

    /**
     * Adds a listener, which listens to overwritten permission changes of this server.
     *
     * @param listener The listener to add.
     */
    void addServerChannelChangeOverwrittenPermissionsListener(ServerChannelChangeOverwrittenPermissionsListener listener);

    /**
     * Gets a list with all registered server channel change overwritten permissions listeners.
     *
     * @return A list with all registered server channel change overwritten permissions listeners.
     */
    List<ServerChannelChangeOverwrittenPermissionsListener> getServerChannelChangeOverwrittenPermissionsListeners();
   
    /**
     * Determines whether a user using the following criteria: <br/> <br/> 
     * <b>{@link ServerTextChannel}:</b> Checks if a user may read messages.
     * <br>
     * <b>{@link ServerVoiceChannel}:</b> Checks if a user may connect.
     * <br/>
     * <b>{@link ChannelCategory}:</b> Checks if a user can see at least one child channel.
     * @param user - The user object to test upon
     * @return True if user can,false otherwise
     */
    public boolean canSee(User user);
    
}
