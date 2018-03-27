package org.javacord.core.entity.activity;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.activity.ActivityParty;

import java.util.Objects;
import java.util.Optional;

/**
 * The implementation of {@link ActivityParty}.
 */
public class ActivityPartyImpl implements ActivityParty {

    private final String id;
    private final Integer currentSize;
    private final Integer maximumSize;

    /**
     * Creates a new activity party object.
     *
     * @param data The json data of the activity party.
     */
    public ActivityPartyImpl(JsonNode data) {
        this.id = data.has("id") ? data.get("id").asText(null) : null;
        if (data.has("size")) {
            // The size is an array with two integers
            this.currentSize = data.get("size").get(0).asInt();
            this.maximumSize = data.get("size").get(1).asInt();
        } else {
            this.currentSize = null;
            this.maximumSize = null;
        }
    }

    @Override
    public Optional<String> getId() {
        return Optional.ofNullable(id);
    }

    @Override
    public Optional<Integer> getCurrentSize() {
        return Optional.ofNullable(currentSize);
    }

    @Override
    public Optional<Integer> getMaximumSize() {
        return Optional.ofNullable(maximumSize);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ActivityPartyImpl)) {
            return false;
        }
        ActivityPartyImpl otherParty = (ActivityPartyImpl) obj;
        return Objects.deepEquals(id, otherParty.id)
                && Objects.deepEquals(currentSize, otherParty.currentSize)
                && Objects.deepEquals(maximumSize, otherParty.maximumSize);
    }

    @Override
    public int hashCode() {
        int hash = 42;
        int idHash = id == null ? 0 : id.hashCode();
        int currentSize = this.currentSize == null ? 0 : this.currentSize;
        int maximumSize = this.maximumSize == null ? 0 : this.maximumSize;

        hash = hash * 11 + idHash;
        hash = hash * 13 + currentSize;
        hash = hash * 17 + maximumSize;
        return hash;
    }
}
