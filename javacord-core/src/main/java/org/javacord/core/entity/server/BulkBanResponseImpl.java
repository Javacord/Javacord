package org.javacord.core.entity.server;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.server.BulkBanResponse;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class BulkBanResponseImpl implements BulkBanResponse {

    /**
     * The user ids that were banned successfully.
     */
    private final Set<Long> bannedUserIds;

    /**
     * The user ids that could not be banned.
     */
    private final Set<Long> failedUserIds;

    /**
     * Create a new BulkBanResponseImpl.
     *
     * @param data The json data about this response.
     */
    public BulkBanResponseImpl(JsonNode data) {
        Set<Long> bannedUsers = new HashSet<>();
        Set<Long> failedUsers = new HashSet<>();
        data.get("banned_users").forEach(bannedUser -> bannedUsers.add(bannedUser.asLong()));
        data.get("failed_users").forEach(failedUser -> failedUsers.add(failedUser.asLong()));
        bannedUserIds = Collections.unmodifiableSet(bannedUsers);
        failedUserIds = Collections.unmodifiableSet(failedUsers);
    }

    @Override
    public Collection<Long> getBannedUserIds() {
        return bannedUserIds;
    }

    @Override
    public Collection<Long> getFailedUserIds() {
        return failedUserIds;
    }
}
