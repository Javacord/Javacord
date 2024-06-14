package org.javacord.core.event;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.event.GatewayDispatchEvent;

public class GatewayDispatchEventImpl extends EventImpl implements GatewayDispatchEvent {
    private String type;
    private JsonNode payload;

    /**
     * Creates a new gateway dispatch event.
     *
     * @param api The api instance of the event.
     */
    public GatewayDispatchEventImpl(DiscordApi api, String type, JsonNode payload) {
        super(api);
        this.type = type;
        this.payload = payload;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Object getPayload() {
        return payload;
    }
}
