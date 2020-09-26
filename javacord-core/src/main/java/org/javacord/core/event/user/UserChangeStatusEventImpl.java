package org.javacord.core.event.user;

import io.vavr.collection.Map;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.DiscordClient;
import org.javacord.api.entity.user.UserStatus;
import org.javacord.api.event.user.UserChangeStatusEvent;

/**
 * The implementation of {@link UserChangeStatusEvent}.
 */
public class UserChangeStatusEventImpl extends OptionalUserEventImpl implements UserChangeStatusEvent {

    /**
     * The new status of the user.
     */
    private final UserStatus newStatus;

    /**
     * The old status of the user.
     */
    private final UserStatus oldStatus;

    /**
     * The new client specific status of the user.
     */
    private final Map<DiscordClient, UserStatus> newClientStatus;

    /**
     * The old client specific status of the user.
     */
    private final Map<DiscordClient, UserStatus> oldClientStatus;

    /**
     * Creates a new user change status event.
     *
     * @param api A discord api instance.
     * @param userId The id of the user of the event.
     * @param newStatus The new status of the user.
     * @param oldStatus The old status of the user.
     * @param newClientStatus The new client specific status of the user.
     * @param oldClientStatus The old client specific status of the user.
     */
    public UserChangeStatusEventImpl(DiscordApi api, long userId, UserStatus newStatus, UserStatus oldStatus,
                                     Map<DiscordClient, UserStatus> newClientStatus,
                                     Map<DiscordClient, UserStatus> oldClientStatus) {
        super(api, userId);
        this.newStatus = newStatus;
        this.oldStatus = oldStatus;
        this.newClientStatus = newClientStatus;
        this.oldClientStatus = oldClientStatus;
    }

    @Override
    public UserStatus getOldStatus() {
        return oldStatus;
    }

    @Override
    public UserStatus getNewStatus() {
        return newStatus;
    }

    @Override
    public UserStatus getOldStatusOnClient(DiscordClient client) {
        return oldClientStatus.getOrElse(client, UserStatus.OFFLINE);
    }

    @Override
    public UserStatus getNewStatusOnClient(DiscordClient client) {
        return newClientStatus.getOrElse(client, UserStatus.OFFLINE);
    }

}
