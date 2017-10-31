package de.btobastian.javacord.entities.channels;

import com.mashape.unirest.http.HttpMethod;
import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.permissions.*;
import de.btobastian.javacord.entities.permissions.impl.ImplPermissions;
import de.btobastian.javacord.listeners.server.channel.ServerChannelChangeNameListener;
import de.btobastian.javacord.listeners.server.channel.ServerChannelChangeOverwrittenPermissionsListener;
import de.btobastian.javacord.listeners.server.channel.ServerChannelChangePositionListener;
import de.btobastian.javacord.listeners.server.channel.ServerChannelDeleteListener;
import de.btobastian.javacord.utils.rest.RestEndpoint;
import de.btobastian.javacord.utils.rest.RestRequest;
import org.json.JSONObject;

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
     * Gets the raw position of the channel.
     * This is the positions sent from Discord and might not be unique and have gaps.
     * Also every channel type (text, voice and category) has it's own position counter.
     *
     * @return The raw position of the channel.
     */
    int getRawPosition();

    /**
     * Gets the real position of the channel.
     * Returns <code>-1</code> if the channel is deleted.
     *
     * @return The real position of the channel.
     */
    default int getPosition() {
        return getServer().getChannels().indexOf(this);
    }

    /**
     * Updates the name of the channel.
     *
     * @param name The new name of the channel.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateName(String name) {
        return new RestRequest<Void>(getApi(), HttpMethod.PATCH, RestEndpoint.CHANNEL)
                .setUrlParameters(String.valueOf(getId()))
                .setBody(new JSONObject().put("name", name == null ? JSONObject.NULL : name))
                .execute(res -> null);
    }

    /**
     * Updates the position of the channel.
     *
     * @param position The new position of the channel.
     *                 If you want to update the position based on other channels, make sure to use
     *                 {@link ServerChannel#getRawPosition()} instead of {@link ServerChannel#getPosition()}!
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updatePosition(int position) {
        return new RestRequest<Void>(getApi(), HttpMethod.PATCH, RestEndpoint.CHANNEL)
                .setUrlParameters(String.valueOf(getId()))
                .setBody(new JSONObject().put("position", position))
                .execute(res -> null);
    }

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
     * Checks if the user has a given set of permissions.
     *
     * @param user The user to check.
     * @param type The permission type(s) to check.
     * @return Whether the user has all given permissions of not.
     * @see #getEffectiveAllowedPermissions(User)
     */
    default boolean hasPermissions(User user, PermissionType... type) {
        return getEffectiveAllowedPermissions(user).containsAll(Arrays.asList(type));
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
     * Checks if the given user can see this channel.
     *
     * @param user The user to check.
     * @return Whether the given user can see this channel or not.
     */
    default boolean canSee(User user) {
        return hasPermissions(user, PermissionType.ADMINISTRATOR) || hasPermissions(user, PermissionType.READ_MESSAGES);
    }

    /**
     * Adds a listener, which listens to this channel being deleted.
     *
     * @param listener The listener to add.
     */
    default void addServerChannelDeleteListener(ServerChannelDeleteListener listener) {
        ((ImplDiscordApi) getApi()).addObjectListener(
                ServerChannel.class, getId(), ServerChannelDeleteListener.class, listener);
    }

    /**
     * Gets a list with all registered server channel delete listeners.
     *
     * @return A list with all registered server channel delete listeners.
     */
    default List<ServerChannelDeleteListener> getServerChannelDeleteListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(
                ServerChannel.class, getId(), ServerChannelDeleteListener.class);
    }

    /**
     * Adds a listener, which listens this server channel name changes.
     *
     * @param listener The listener to add.
     */
    default void addServerChannelChangeNameListener(ServerChannelChangeNameListener listener) {
        ((ImplDiscordApi) getApi()).addObjectListener(
                ServerChannel.class, getId(), ServerChannelChangeNameListener.class, listener);
    }

    /**
     * Gets a list with all registered server channel change name listeners.
     *
     * @return A list with all registered server channel change name listeners.
     */
    default List<ServerChannelChangeNameListener> getServerChannelChangeNameListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(
                ServerChannel.class, getId(), ServerChannelChangeNameListener.class);
    }

    /**
     * Adds a listener, which listens this server channel position changes.
     *
     * @param listener The listener to add.
     */
    default void addServerChannelChangePositionListener(ServerChannelChangePositionListener listener) {
        ((ImplDiscordApi) getApi()).addObjectListener(
                ServerChannel.class, getId(), ServerChannelChangePositionListener.class, listener);
    }

    /**
     * Gets a list with all registered server channel change position listeners.
     *
     * @return A list with all registered server channel change position listeners.
     */
    default List<ServerChannelChangePositionListener> getServerChannelChangePositionListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(
                ServerChannel.class, getId(), ServerChannelChangePositionListener.class);
    }

    /**
     * Adds a listener, which listens to overwritten permission changes of this server.
     *
     * @param listener The listener to add.
     */
    default void addServerChannelChangeOverwrittenPermissionsListener(ServerChannelChangeOverwrittenPermissionsListener listener) {
        ((ImplDiscordApi) getApi()).addObjectListener(
                ServerChannel.class, getId(), ServerChannelChangeOverwrittenPermissionsListener.class, listener);
    }

    /**
     * Gets a list with all registered server channel change overwritten permissions listeners.
     *
     * @return A list with all registered server channel change overwritten permissions listeners.
     */
    default List<ServerChannelChangeOverwrittenPermissionsListener> getServerChannelChangeOverwrittenPermissionsListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(
                ServerChannel.class, getId(), ServerChannelChangeOverwrittenPermissionsListener.class);
    }


}
