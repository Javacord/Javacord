package org.javacord.core.util.handler.user;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.DiscordClient;
import org.javacord.api.entity.activity.Activity;
import org.javacord.api.entity.user.User;
import org.javacord.api.entity.user.UserStatus;
import org.javacord.api.event.user.UserChangeActivityEvent;
import org.javacord.api.event.user.UserChangeAvatarEvent;
import org.javacord.api.event.user.UserChangeDiscriminatorEvent;
import org.javacord.api.event.user.UserChangeNameEvent;
import org.javacord.api.event.user.UserChangeStatusEvent;
import org.javacord.core.entity.activity.ActivityImpl;
import org.javacord.core.entity.user.UserImpl;
import org.javacord.core.event.user.UserChangeActivityEventImpl;
import org.javacord.core.event.user.UserChangeAvatarEventImpl;
import org.javacord.core.event.user.UserChangeDiscriminatorEventImpl;
import org.javacord.core.event.user.UserChangeNameEventImpl;
import org.javacord.core.event.user.UserChangeStatusEventImpl;
import org.javacord.core.util.gateway.PacketHandler;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
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
        // ignore the guild_id and send to all mutual servers instead or we must track the properties per server
        // or all packets after the first do not detect a change and will not send around an event for the server
        long userId = packet.get("user").get("id").asLong();
        api.getCachedUserById(userId).map(UserImpl.class::cast).ifPresent(user -> {
            if (packet.has("game")) {
                Activity newActivity = null;
                if (!packet.get("game").isNull()) {
                    newActivity = new ActivityImpl(api, packet.get("game"));
                }
                Activity oldActivity = user.getActivity().orElse(null);
                user.setActivity(newActivity);
                if (!Objects.deepEquals(newActivity, oldActivity)) {
                    dispatchUserActivityChangeEvent(user, newActivity, oldActivity);
                }
            }
            UserStatus oldStatus = user.getStatus();
            UserStatus newStatus = oldStatus;
            if (packet.has("status")) {
                newStatus = UserStatus.fromString(packet.get("status").asText(null));
                user.setStatus(newStatus);
            }
            Map<DiscordClient, UserStatus> newClientStatus = new HashMap<>();
            Map<DiscordClient, UserStatus> oldClientStatus = new HashMap<>();
            for (DiscordClient client : DiscordClient.values()) {
                oldClientStatus.put(client, user.getStatusOnClient(client));
                if (packet.has("client_status")) {
                    JsonNode clientStatus = packet.get("client_status");
                    if (clientStatus.hasNonNull(client.getName())) {
                        UserStatus status = UserStatus.fromString(clientStatus.get(client.getName()).asText());
                        user.setClientStatus(client, status);
                    } else {
                        user.setClientStatus(client, UserStatus.OFFLINE);
                    }
                }
                newClientStatus.put(client, user.getStatusOnClient(client));
            }

            dispatchUserStatusChangeEventIfChangeDetected(user, newStatus, oldStatus, newClientStatus, oldClientStatus);

            if (packet.get("user").has("username")) {
                String newName = packet.get("user").get("username").asText();
                String oldName = user.getName();
                if (!oldName.equals(newName)) {
                    user.setName(newName);
                    dispatchUserChangeNameEvent(user, newName, oldName);
                }
            }
            if (packet.get("user").has("discriminator")) {
                String newDiscriminator = packet.get("user").get("discriminator").asText();
                String oldDiscriminator = user.getDiscriminator();
                if (!oldDiscriminator.equals(newDiscriminator)) {
                    user.setDiscriminator(newDiscriminator);
                    dispatchUserChangeDiscriminatorEvent(user, newDiscriminator, oldDiscriminator);
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
        UserChangeActivityEvent event = new UserChangeActivityEventImpl(user, newActivity, oldActivity);

        api.getEventDispatcher().dispatchUserChangeActivityEvent(
                api, user.getMutualServers(), Collections.singleton(user), event);
    }

    private void dispatchUserStatusChangeEventIfChangeDetected(User user, UserStatus newStatus, UserStatus oldStatus,
                                                               Map<DiscordClient, UserStatus> newClientStatus,
                                                               Map<DiscordClient, UserStatus> oldClientStatus) {
        // Only dispatch the event if something changed
        boolean shouldDispatch = false;
        if (newClientStatus != oldClientStatus) {
            shouldDispatch = true;
        }
        for (DiscordClient client : DiscordClient.values()) {
            if (newClientStatus.get(client) != oldClientStatus.get(client)) {
                shouldDispatch = true;
            }
        }
        if (!shouldDispatch) {
            return;
        }

        UserChangeStatusEvent event =
                new UserChangeStatusEventImpl(user, newStatus, oldStatus, newClientStatus, oldClientStatus);

        api.getEventDispatcher().dispatchUserChangeStatusEvent(
                api, user.getMutualServers(), Collections.singleton(user), event);
    }

    private void dispatchUserChangeNameEvent(User user, String newName, String oldName) {
        UserChangeNameEvent event = new UserChangeNameEventImpl(user, newName, oldName);

        api.getEventDispatcher().dispatchUserChangeNameEvent(
                api, user.getMutualServers(), Collections.singleton(user), event);
    }

    private void dispatchUserChangeDiscriminatorEvent(User user, String newDiscriminator, String oldDiscriminator) {
        UserChangeDiscriminatorEvent event =
                new UserChangeDiscriminatorEventImpl(user, newDiscriminator, oldDiscriminator);

        api.getEventDispatcher().dispatchUserChangeDiscriminatorEvent(
                api, user.getMutualServers(), Collections.singleton(user), event);
    }

    private void dispatchUserChangeAvatarEvent(User user, String newAvatarHash, String oldAvatarHash) {
        UserChangeAvatarEvent event = new UserChangeAvatarEventImpl(user, newAvatarHash, oldAvatarHash);

        api.getEventDispatcher().dispatchUserChangeAvatarEvent(
                api, user.getMutualServers(), Collections.singleton(user), event);
    }

}
