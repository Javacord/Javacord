package org.javacord.core.entity.channel;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.channel.internal.ServerChannelBuilderDelegate;
import org.javacord.core.entity.server.ServerImpl;

/**
 * The implementation of {@link ServerChannelBuilderDelegate}.
 */
public class ServerChannelBuilderDelegateImpl implements ServerChannelBuilderDelegate {

    /**
     * The server of the channel.
     */
    protected final ServerImpl server;

    /**
     * The reason for the creation.
     */
    protected String reason = null;

    /**
     * The name of the channel.
     */
    private String name = null;

    protected ServerChannelBuilderDelegateImpl(ServerImpl server) {
        this.server = server;
    }

    @Override
    public void setAuditLogReason(String reason) {
        this.reason = reason;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    protected void prepareBody(ObjectNode body) {
        if (name == null) {
            throw new IllegalStateException("Name is no optional parameter!");
        }
        body.put("name", name);
    }
}
