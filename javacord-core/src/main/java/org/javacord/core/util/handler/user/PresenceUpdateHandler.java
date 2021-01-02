package org.javacord.core.util.handler.user;

import com.fasterxml.jackson.databind.JsonNode;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.DiscordClient;
import org.javacord.api.entity.activity.Activity;
import org.javacord.api.entity.user.UserStatus;
import org.javacord.api.event.user.UserChangeActivityEvent;
import org.javacord.api.event.user.UserChangeStatusEvent;
import org.javacord.core.entity.activity.ActivityImpl;
import org.javacord.core.entity.user.UserImpl;
import org.javacord.core.entity.user.UserPresence;
import org.javacord.core.event.user.UserChangeActivityEventImpl;
import org.javacord.core.event.user.UserChangeStatusEventImpl;
import org.javacord.core.util.gateway.PacketHandler;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

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

        AtomicReference<UserPresence> presence = new AtomicReference<>(
                api.getEntityCache().get().getUserPresenceCache().getPresenceByUserId(userId)
                        .orElseGet(() -> new UserPresence(userId, null, null, io.vavr.collection.HashMap.empty()))
        );

        if (packet.hasNonNull("activities")) {
            Set<Activity> newActivities = new HashSet<>();
            for (JsonNode activityJson : packet.get("activities")) {
                if (!activityJson.isNull()) {
                    newActivities.add(new ActivityImpl(api, activityJson));
                }
            }
            Set<Activity> oldActivities = api.getEntityCache().get()
                    .getUserPresenceCache()
                    .getPresenceByUserId(userId)
                    .map(UserPresence::getActivities)
                    .orElse(Collections.emptySet());
            presence.set(presence.get().setActivities(newActivities));

            if (!Objects.deepEquals(newActivities.toArray(), oldActivities.toArray())) {
                dispatchUserActivityChangeEvent(userId, newActivities, oldActivities);
            }
        }

        UserStatus oldStatus = api.getEntityCache().get().getUserPresenceCache().getPresenceByUserId(userId)
                .map(UserPresence::getStatus)
                .orElse(UserStatus.OFFLINE);
        UserStatus newStatus;
        if (packet.has("status")) {
            newStatus = UserStatus.fromString(packet.get("status").asText(null));
            presence.set(presence.get().setStatus(newStatus));
        } else {
            newStatus = oldStatus;
        }
        Map<DiscordClient, UserStatus> oldClientStatus = api.getEntityCache().get().getUserPresenceCache()
                .getPresenceByUserId(userId)
                .map(UserPresence::getClientStatus)
                .orElse(HashMap.empty());
        for (DiscordClient client : DiscordClient.values()) {
            if (packet.has("client_status")) {
                JsonNode clientStatus = packet.get("client_status");
                if (clientStatus.hasNonNull(client.getName())) {
                    UserStatus status = UserStatus.fromString(clientStatus.get(client.getName()).asText());
                    presence.set(presence.get().setClientStatus(presence.get().getClientStatus().put(client, status)));
                } else {
                    presence.set(presence.get()
                            .setClientStatus(presence.get().getClientStatus().put(client, UserStatus.OFFLINE)));
                }
            }
        }
        Map<DiscordClient, UserStatus> newClientStatus = api.getEntityCache().get().getUserPresenceCache()
                .getPresenceByUserId(userId)
                .map(UserPresence::getClientStatus)
                .orElse(HashMap.empty());

        api.updateUserPresence(userId, p -> presence.get());

        dispatchUserStatusChangeEventIfChangeDetected(userId, newStatus, oldStatus, newClientStatus, oldClientStatus);
    }

    private void dispatchUserActivityChangeEvent(long userId, Set<Activity> newActivities,
                                                 Set<Activity> oldActivities) {
        UserImpl user = api.getCachedUserById(userId).map(UserImpl.class::cast).orElse(null);
        UserChangeActivityEvent event = new UserChangeActivityEventImpl(api, userId, newActivities, oldActivities);

        api.getEventDispatcher().dispatchUserChangeActivityEvent(
                api,
                user == null ? Collections.emptySet() : user.getMutualServers(),
                user == null ? Collections.emptySet() : Collections.singleton(user),
                event
        );
    }

    private void dispatchUserStatusChangeEventIfChangeDetected(long userId, UserStatus newStatus, UserStatus oldStatus,
                                                               Map<DiscordClient, UserStatus> newClientStatus,
                                                               Map<DiscordClient, UserStatus> oldClientStatus) {
        UserImpl user = api.getCachedUserById(userId).map(UserImpl.class::cast).orElse(null);
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
                new UserChangeStatusEventImpl(api, userId, newStatus, oldStatus, newClientStatus, oldClientStatus);

        api.getEventDispatcher().dispatchUserChangeStatusEvent(
                api,
                user == null ? Collections.emptySet() : user.getMutualServers(),
                user == null ? Collections.emptySet() : Collections.singleton(user),
                event
        );
    }
}
