package org.javacord.core.event.user;

import org.javacord.api.entity.DiscordClient;
import org.javacord.api.entity.user.User;
import org.javacord.api.entity.user.UserStatus;
import org.javacord.api.event.user.UserChangeStatusEvent;

import java.util.Map;

/**
 * The implementation of {@link UserChangeStatusEvent}.
 */
public class UserChangeStatusEventImpl extends UserEventImpl implements UserChangeStatusEvent {

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
     * @param user The user of the event.
     * @param newStatus The new status of the user.
     * @param oldStatus The old status of the user.
     * @param newClientStatus The new client specific status of the user.
     * @param oldClientStatus The old client specific status of the user.
     */
    public UserChangeStatusEventImpl(User user, UserStatus newStatus, UserStatus oldStatus,
                                     Map<DiscordClient, UserStatus> newClientStatus,
                                     Map<DiscordClient, UserStatus> oldClientStatus) {
        super(user);
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
        return oldClientStatus.getOrDefault(client, UserStatus.OFFLINE);
    }

    @Override
    public UserStatus getNewStatusOnClient(DiscordClient client) {
        return newClientStatus.getOrDefault(client, UserStatus.OFFLINE);
    }

}
