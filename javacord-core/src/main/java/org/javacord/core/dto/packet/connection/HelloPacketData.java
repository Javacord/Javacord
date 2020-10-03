package org.javacord.core.dto.packet.connection;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.time.Duration;

public class HelloPacketData {

    private final int heartbeatInterval;

    @JsonCreator
    public HelloPacketData(int heartbeatInterval) {
        this.heartbeatInterval = heartbeatInterval;
    }

    public Duration getHeartbeatInterval() {
        return Duration.ofMillis(heartbeatInterval);
    }
}
