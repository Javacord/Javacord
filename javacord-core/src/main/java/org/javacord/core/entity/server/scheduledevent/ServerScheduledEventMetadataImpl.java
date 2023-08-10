package org.javacord.core.entity.server.scheduledevent;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.server.scheduledevent.ServerScheduledEventMetadata;
import java.util.Optional;

public class ServerScheduledEventMetadataImpl implements ServerScheduledEventMetadata {

    private final String location;

    /**
     * Creates a new entity metadata.
     *
     * @param data The json data of the entity metadata.
     */
    public ServerScheduledEventMetadataImpl(JsonNode data) {
        this.location = data.path("location").textValue();
    }

    @Override
    public Optional<String> getLocation() {
        return Optional.ofNullable(location);
    }
}
