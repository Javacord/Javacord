package org.javacord.api.entity.server;

import java.util.Collection;

/**
 * The response of a bulk ban request.
 */
public interface BulkBanResponse {

    /**
     * Gets the user ids that were banned successfully.
     *
     * @return The banned user ids.
     */
    Collection<Long> getBannedUserIds();

    /**
     * Gets the user ids that could not be banned.
     *
     * @return The failed user ids.
     */
    Collection<Long> getFailedUserIds();
}
