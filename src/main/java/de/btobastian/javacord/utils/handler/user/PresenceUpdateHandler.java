package de.btobastian.javacord.utils.handler.user;

import com.fasterxml.jackson.databind.JsonNode;
import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.Activity;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.UserStatus;
import de.btobastian.javacord.entities.impl.ImplActivity;
import de.btobastian.javacord.entities.impl.ImplUser;
import de.btobastian.javacord.events.user.UserChangeActivityEvent;
import de.btobastian.javacord.events.user.UserChangeAvatarEvent;
import de.btobastian.javacord.events.user.UserChangeNameEvent;
import de.btobastian.javacord.events.user.UserChangeStatusEvent;
import de.btobastian.javacord.listeners.user.UserChangeActivityListener;
import de.btobastian.javacord.listeners.user.UserChangeAvatarListener;
import de.btobastian.javacord.listeners.user.UserChangeNameListener;
import de.btobastian.javacord.listeners.user.UserChangeStatusListener;
import de.btobastian.javacord.utils.PacketHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Handles the presence update packet.
 */
public class PresenceUpdateHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public PresenceUpdateHandler(DiscordApi api) {
        super(api, true, "PRESENCE_UPDATE");
    }

    @Override
    public void handle(JsonNode packet) {
        long userId = packet.get("user").get("id").asLong();
        api.getUserById(userId).map(user -> ((ImplUser) user)).ifPresent(user -> {
            if (packet.has("game")) {
                Activity newActivity = null;
                if (!packet.get("game").isNull()) {
                    newActivity = new ImplActivity(packet.get("game"));
                }
                Activity oldActivity = user.getActivity().orElse(null);
                user.setActivity(newActivity);
                if (!Objects.deepEquals(newActivity, oldActivity)) {
                    dispatchUserActivityChangeEvent(user, newActivity, oldActivity);
                }
            }
            if (packet.has("status")) {
                UserStatus newStatus = UserStatus.fromString(
                        packet.has("status") ? packet.get("status").asText(null) : null);
                UserStatus oldStatus = user.getStatus();
                user.setStatus(newStatus);
                if (newStatus != oldStatus) {
                    dispatchUserStatusChangeEvent(user, newStatus, oldStatus);
                }
            }
            if (packet.get("user").has("username")) {
                String newName = packet.get("user").get("username").asText();
                String oldName = user.getName();
                if (!oldName.equals(newName)) {
                    user.setName(newName);
                    dispatchUserChangeNameEvent(user, newName, oldName);
                }
            }
            if (packet.get("user").has("username")) {
                String newName = packet.get("user").get("username").asText();
                String oldName = user.getName();
                if (!oldName.equals(newName)) {
                    user.setName(newName);
                    dispatchUserChangeNameEvent(user, newName, oldName);
                }
            }
            if (packet.get("user").has("avatar")) {
                String newAvatarHash = packet.get("user").get("avatar").asText(null);
                String oldAvatarHash = user.getAvatarHash();
                if (!Objects.deepEquals(newAvatarHash, oldAvatarHash)) {
                    user.setAvatarHash(newAvatarHash);
                    dispatchUserChangeAvatarEvent(user, newAvatarHash, oldAvatarHash);
                }
            }
        });
    }

    private void dispatchUserActivityChangeEvent(User user, Activity newActivity, Activity oldActivity) {
        UserChangeActivityEvent event = new UserChangeActivityEvent(api, user, newActivity, oldActivity);

        List<UserChangeActivityListener> listeners = new ArrayList<>();
        listeners.addAll(user.getUserChangeActivityListeners());
        user.getMutualServers().forEach(server -> listeners.addAll(server.getUserChangeActivityListeners()));
        listeners.addAll(api.getUserChangeActivityListeners());

        dispatchEvent(listeners, listener -> listener.onUserChangeActivity(event));
    }

    private void dispatchUserStatusChangeEvent(User user, UserStatus newStatus, UserStatus oldStatus) {
        UserChangeStatusEvent event = new UserChangeStatusEvent(api, user, newStatus, oldStatus);

        List<UserChangeStatusListener> listeners = new ArrayList<>();
        listeners.addAll(user.getUserChangeStatusListeners());
        user.getMutualServers().forEach(server -> listeners.addAll(server.getUserChangeStatusListeners()));
        listeners.addAll(api.getUserChangeStatusListeners());

        dispatchEvent(listeners, listener -> listener.onUserChangeStatus(event));
    }

    private void dispatchUserChangeNameEvent(User user, String newName, String oldName) {
        UserChangeNameEvent event = new UserChangeNameEvent(api, user, newName, oldName);

        List<UserChangeNameListener> listeners = new ArrayList<>();
        listeners.addAll(user.getUserChangeNameListeners());
        user.getMutualServers().forEach(server -> listeners.addAll(server.getUserChangeNameListeners()));
        listeners.addAll(api.getUserChangeNameListeners());

        dispatchEvent(listeners, listener -> listener.onUserChangeName(event));
    }

    private void dispatchUserChangeAvatarEvent(User user, String newAvatarHash, String oldAvatarHash) {
        UserChangeAvatarEvent event = new UserChangeAvatarEvent(api, user, newAvatarHash, oldAvatarHash);

        List<UserChangeAvatarListener> listeners = new ArrayList<>();
        listeners.addAll(user.getUserChangeAvatarListeners());
        user.getMutualServers().forEach(server -> listeners.addAll(server.getUserChangeAvatarListeners()));
        listeners.addAll(api.getUserChangeAvatarListeners());

        dispatchEvent(listeners, listener -> listener.onUserChangeAvatar(event));
    }

}