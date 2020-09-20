package org.javacord.api.event.user;

import org.javacord.api.entity.DiscordClient;
import org.javacord.api.entity.user.UserStatus;

/**
 * A user change status event.
 */
public interface UserChangeStatusEvent extends OptionalUserEvent {

    /**
     * Gets the old connection status of the user.
     *
     * <p>This will return {@link UserStatus#OFFLINE} for invisible users.
     *
     * @return The old status of the user.
     */
    UserStatus getOldStatus();

    /**
     * Gets the new connection status of the user.
     *
     * <p>This will return {@link UserStatus#OFFLINE} for invisible users.
     *
     * @return The new status of the user.
     */
    UserStatus getNewStatus();

    /**
     * Gets the old status of the user on the {@link DiscordClient#DESKTOP desktop} client.
     *
     * <p>This will return {@link UserStatus#OFFLINE} for invisible users.
     *
     * @return The status of the the user.
     * @see #getOldStatusOnClient(DiscordClient)
     */
    default UserStatus getOldDesktopStatus() {
        return getOldStatusOnClient(DiscordClient.DESKTOP);
    }

    /**
     * Gets the old status of the user on the {@link DiscordClient#MOBILE mobile} client.
     *
     * <p>This will return {@link UserStatus#OFFLINE} for invisible users.
     *
     * @return The status of the the user.
     * @see #getOldStatusOnClient(DiscordClient)
     */
    default UserStatus getOldMobileStatus() {
        return getOldStatusOnClient(DiscordClient.MOBILE);
    }

    /**
     * Gets the old status of the user on the {@link DiscordClient#WEB web} (browser) client.
     *
     * <p>This will return {@link UserStatus#OFFLINE} for invisible users.
     *
     * @return The status of the the user.
     * @see #getOldStatusOnClient(DiscordClient)
     */
    default UserStatus getOldWebStatus() {
        return getOldStatusOnClient(DiscordClient.WEB);
    }

    /**
     * Gets the new status of the user on the {@link DiscordClient#DESKTOP desktop} client.
     *
     * <p>This will return {@link UserStatus#OFFLINE} for invisible users.
     *
     * @return The status of the the user.
     * @see #getNewStatusOnClient(DiscordClient)
     */
    default UserStatus getNewDesktopStatus() {
        return getNewStatusOnClient(DiscordClient.DESKTOP);
    }

    /**
     * Gets the new status of the user on the {@link DiscordClient#MOBILE mobile} client.
     *
     * <p>This will return {@link UserStatus#OFFLINE} for invisible users.
     *
     * @return The status of the the user.
     * @see #getNewStatusOnClient(DiscordClient)
     */
    default UserStatus getNewMobileStatus() {
        return getNewStatusOnClient(DiscordClient.MOBILE);
    }

    /**
     * Gets the new status of the user on the {@link DiscordClient#WEB web} (browser) client.
     *
     * <p>This will return {@link UserStatus#OFFLINE} for invisible users.
     *
     * @return The status of the the user.
     * @see #getNewStatusOnClient(DiscordClient)
     */
    default UserStatus getNewWebStatus() {
        return getNewStatusOnClient(DiscordClient.WEB);
    }

    /**
     * Gets the old status on the given client.
     *
     * @param client The client.
     * @return The old status on the given client.
     */
    UserStatus getOldStatusOnClient(DiscordClient client);

    /**
     * Gets the new status on the given client.
     *
     * @param client The client.
     * @return The new status on the given client.
     */
    UserStatus getNewStatusOnClient(DiscordClient client);

    /**
     * Checks if the status has changed on the given client.
     *
     * @param client The client.
     * @return Wether the status changed on the given client or not.
     */
    default boolean hasStatusChangeOnClient(DiscordClient client) {
        return getOldStatusOnClient(client) != getNewStatusOnClient(client);
    }

}
