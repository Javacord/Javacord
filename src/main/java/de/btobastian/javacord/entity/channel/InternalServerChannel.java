package de.btobastian.javacord.entity.channel;

import com.fasterxml.jackson.databind.JsonNode;
import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entity.permission.PermissionState;
import de.btobastian.javacord.entity.permission.PermissionType;
import de.btobastian.javacord.entity.permission.Permissions;
import de.btobastian.javacord.entity.permission.PermissionsBuilder;
import de.btobastian.javacord.entity.permission.Role;
import de.btobastian.javacord.entity.permission.impl.ImplPermissions;
import de.btobastian.javacord.entity.server.Server;
import de.btobastian.javacord.entity.server.invite.RichInvite;
import de.btobastian.javacord.entity.server.invite.impl.ImplInvite;
import de.btobastian.javacord.entity.user.User;
import de.btobastian.javacord.listener.ChannelAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;
import de.btobastian.javacord.listener.channel.server.ServerChannelAttachableListener;
import de.btobastian.javacord.listener.channel.server.ServerChannelChangeNameListener;
import de.btobastian.javacord.listener.channel.server.ServerChannelChangeOverwrittenPermissionsListener;
import de.btobastian.javacord.listener.channel.server.ServerChannelChangePositionListener;
import de.btobastian.javacord.listener.channel.server.ServerChannelDeleteListener;
import de.btobastian.javacord.util.ClassHelper;
import de.btobastian.javacord.util.event.ListenerManager;
import de.btobastian.javacord.util.rest.RestEndpoint;
import de.btobastian.javacord.util.rest.RestMethod;
import de.btobastian.javacord.util.rest.RestRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface InternalServerChannel extends ServerChannel {

    @Override
    default CompletableFuture<Collection<RichInvite>> getInvites() {
        return new RestRequest<Collection<RichInvite>>(getApi(), RestMethod.GET, RestEndpoint.CHANNEL_INVITE)
                .setUrlParameters(getIdAsString())
                .execute(result -> {
                    Collection<RichInvite> invites = new HashSet<>();
                    for (JsonNode inviteJson : result.getJsonBody()) {
                        invites.add(new ImplInvite(getApi(), inviteJson));
                    }
                    return Collections.unmodifiableCollection(invites);
                });
    }

    @Override
    default Permissions getEffectiveOverwrittenPermissions(User user) {
        PermissionsBuilder builder = new PermissionsBuilder(ImplPermissions.EMPTY_PERMISSIONS);
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
        List<Role> rolesOfUser = new ArrayList<>(server.getRolesOf(user));
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
            Permissions permissions = getOverwrittenPermissions(user);
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
    default CompletableFuture<Void> delete(String reason) {
        return new RestRequest<Void>(getApi(), RestMethod.DELETE, RestEndpoint.CHANNEL)
                .setUrlParameters(getIdAsString())
                .setAuditLogReason(reason)
                .execute(result -> null);
    }

    @Override
    default ListenerManager<ServerChannelDeleteListener> addServerChannelDeleteListener(
            ServerChannelDeleteListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                ServerChannel.class, getId(), ServerChannelDeleteListener.class, listener);
    }

    @Override
    default List<ServerChannelDeleteListener> getServerChannelDeleteListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(
                ServerChannel.class, getId(), ServerChannelDeleteListener.class);
    }

    @Override
    default ListenerManager<ServerChannelChangeNameListener> addServerChannelChangeNameListener(
            ServerChannelChangeNameListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                ServerChannel.class, getId(), ServerChannelChangeNameListener.class, listener);
    }

    @Override
    default List<ServerChannelChangeNameListener> getServerChannelChangeNameListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(
                ServerChannel.class, getId(), ServerChannelChangeNameListener.class);
    }

    @Override
    default ListenerManager<ServerChannelChangePositionListener> addServerChannelChangePositionListener(
            ServerChannelChangePositionListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                ServerChannel.class, getId(), ServerChannelChangePositionListener.class, listener);
    }

    @Override
    default List<ServerChannelChangePositionListener> getServerChannelChangePositionListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(
                ServerChannel.class, getId(), ServerChannelChangePositionListener.class);
    }

    @Override
    default ListenerManager<ServerChannelChangeOverwrittenPermissionsListener>
    addServerChannelChangeOverwrittenPermissionsListener(ServerChannelChangeOverwrittenPermissionsListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                ServerChannel.class, getId(), ServerChannelChangeOverwrittenPermissionsListener.class, listener);
    }

    @Override
    default List<ServerChannelChangeOverwrittenPermissionsListener>
    getServerChannelChangeOverwrittenPermissionsListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(
                ServerChannel.class, getId(), ServerChannelChangeOverwrittenPermissionsListener.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    default <T extends ServerChannelAttachableListener & ObjectAttachableListener>
    Collection<ListenerManager<? extends ServerChannelAttachableListener>>
    addServerChannelAttachableListener(T listener) {
        return ClassHelper.getInterfacesAsStream(listener.getClass())
                .filter(ServerChannelAttachableListener.class::isAssignableFrom)
                .filter(ObjectAttachableListener.class::isAssignableFrom)
                .map(listenerClass -> (Class<T>) listenerClass)
                .flatMap(listenerClass -> {
                    if (ChannelAttachableListener.class.isAssignableFrom(listenerClass)) {
                        return addChannelAttachableListener(
                                (ChannelAttachableListener & ObjectAttachableListener) listener).stream();
                    } else {
                        return Stream.of(((ImplDiscordApi) getApi()).addObjectListener(ServerChannel.class, getId(),
                                                                                       listenerClass, listener));
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    @SuppressWarnings("unchecked")
    default <T extends ServerChannelAttachableListener & ObjectAttachableListener> void
    removeServerChannelAttachableListener(T listener) {
        ClassHelper.getInterfacesAsStream(listener.getClass())
                .filter(ServerChannelAttachableListener.class::isAssignableFrom)
                .filter(ObjectAttachableListener.class::isAssignableFrom)
                .map(listenerClass -> (Class<T>) listenerClass)
                .forEach(listenerClass -> {
                    if (ChannelAttachableListener.class.isAssignableFrom(listenerClass)) {
                        removeChannelAttachableListener(
                                (ChannelAttachableListener & ObjectAttachableListener) listener);
                    } else {
                        ((ImplDiscordApi) getApi()).removeObjectListener(ServerChannel.class, getId(),
                                                                         listenerClass, listener);
                    }
                });
    }

    @Override
    @SuppressWarnings("unchecked")
    default <T extends ServerChannelAttachableListener & ObjectAttachableListener> Map<T, List<Class<T>>>
    getServerChannelAttachableListeners() {
        Map<T, List<Class<T>>> serverChannelListeners =
                ((ImplDiscordApi) getApi()).getObjectListeners(ServerChannel.class, getId());
        getChannelAttachableListeners().forEach((listener, listenerClasses) -> serverChannelListeners
                .merge((T) listener,
                       (List<Class<T>>) (Object) listenerClasses,
                       (listenerClasses1, listenerClasses2) -> {
                           listenerClasses1.addAll(listenerClasses2);
                           return listenerClasses1;
                       }));
        return serverChannelListeners;
    }

    @Override
    default <T extends ServerChannelAttachableListener & ObjectAttachableListener> void removeListener(
            Class<T> listenerClass, T listener) {
        ((ImplDiscordApi) getApi()).removeObjectListener(ServerChannel.class, getId(), listenerClass, listener);
    }

}
