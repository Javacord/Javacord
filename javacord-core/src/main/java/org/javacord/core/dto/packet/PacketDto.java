package org.javacord.core.dto.packet;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.javacord.core.util.gateway.PacketDeserializer;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

@JsonDeserialize(using = PacketDeserializer.class)
public class PacketDto {

    private final int op;
    @Nullable
    @JsonProperty("s")
    private final Integer sequenceNumber;
    @Nullable
    @JsonProperty("t")
    private final String type;
    @Nullable
    private final Object data;

    public PacketDto(int op, @Nullable Integer sequenceNumber, @Nullable String type, @Nullable Object data) {
        this.op = op;
        this.sequenceNumber = sequenceNumber;
        this.type = type;
        this.data = data;
    }

    public int getOp() {
        return op;
    }

    public Optional<Integer> getSequenceNumber() {
        return Optional.ofNullable(sequenceNumber);
    }

    public Optional<String> getType() {
        return Optional.ofNullable(type);
    }

    public Optional<Object> getData() {
        return Optional.ofNullable(data);
    }
}