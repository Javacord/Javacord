package org.javacord.core.entity.server;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.server.BulkBanResponse;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class BulkBanResponseImpl implements BulkBanResponse {

    /**
     * The user ids that were banned successfully.
     */
    private final Set<Long> bannedUserIds = new LinkedHashSet<>();

    /**
     * The user ids that could not be banned.
     */
    private final Set<Long> failedUserIds = new LinkedHashSet<>();

    /**
     * Create a new BulkBanResponseImpl.
     *
     * @param data The json data about this response.
     */
    public BulkBanResponseImpl(JsonNode data) {
        data.get("banned_users").forEach(bannedUser -> bannedUserIds.add(bannedUser.asLong()));
        data.get("failed_users").forEach(failedUser -> failedUserIds.add(failedUser.asLong()));
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
