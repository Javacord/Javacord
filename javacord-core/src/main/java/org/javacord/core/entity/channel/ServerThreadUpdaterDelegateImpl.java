package org.javacord.core.entity.channel;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.channel.AutoArchiveDuration;
import org.javacord.api.entity.channel.ServerThreadChannel;
import org.javacord.api.entity.channel.internal.ServerThreadUpdaterDelegate;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;

import java.util.concurrent.CompletableFuture;

/**
 * The implementation of {@link ServerThreadUpdaterDelegate}.
 */
public class ServerThreadUpdaterDelegateImpl implements ServerThreadUpdaterDelegate {
    /**
     * The thread to update.
     */
    protected final ServerThreadChannel thread;
    
    /**
     * The reason for the update.
     */
    protected String reason = null;
    
    /**
     * The name to update.
     */
    protected String name = null;
    
    /**
     * The archived flag to update.
     */
    protected Boolean archived = null;
    
    /**
     * The auto arhcive duration to update.
     */
    protected AutoArchiveDuration autoArchiveDuration = null;
    
    /**
     * the locked flag to update.
     */
    protected Boolean locked = null;
    
    /**
     * The invitable flag to update.
     */
    protected Boolean invitable = null;
    
    /**
     * The slowmode delay.
     */
    protected Integer delay = null;
    
    /**
     * Creates a new server thread channel delegate.
     * @param thread The thread to update.
     */
    public ServerThreadUpdaterDelegateImpl(ServerThreadChannel thread) {
        this.thread = thread;
    }
    
    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setArchivedFlag(boolean archived) {
        this.archived = archived;
    }

    @Override
    public void setAutoArchiveDuration(AutoArchiveDuration autoArchiveDuration) {
        this.autoArchiveDuration = autoArchiveDuration;
    }

    @Override
    public void setLockedFlag(boolean locked) {
        this.locked = locked;
    }

    @Override
    public void setInvitableFlag(boolean invitable) {
        this.invitable = invitable;
    }

    @Override
    public void setSlowmodeDelayInSeconds(int delay) {
        this.delay = delay;
    }
    
    @Override
    public void setAuditLogReason(String reason) {
        this.reason = reason;
    }
    
    protected boolean prepareUpdateBody(ObjectNode body) {
        boolean patchThread = false;
        if (name != null) {
            body.put("name", name);
            patchThread = true;
        }
        if (archived != null) {
            body.put("archived", archived);
            patchThread = true;
        }
        if (autoArchiveDuration != null) {
            body.put("auto_archive_duration", autoArchiveDuration.asInt());
            patchThread = true;
        }
        if (locked != null) {
            body.put("locked", locked);
            patchThread = true;
        }
        if (invitable != null) {
            body.put("invitable", invitable);
            patchThread = true;
        }
        if (delay != null) {
            body.put("rate_limit_per_user", delay);
            patchThread = true;
        }
        return patchThread;
    }

    @Override
    public CompletableFuture<Void> update() {
        ObjectNode body = JsonNodeFactory.instance.objectNode();
        if (prepareUpdateBody(body)) {
            return new RestRequest<Void>(thread.getApi(), RestMethod.PATCH, RestEndpoint.CHANNEL)
                    .setUrlParameters(thread.getIdAsString())
                    .setBody(body)
                    .setAuditLogReason(reason)
                    .execute(result -> null);
        } else {
            return CompletableFuture.completedFuture(null);
        }
    }
}