package org.javacord.core.entity.server.invite;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.server.invite.Invite;
import org.javacord.api.entity.server.invite.internal.InviteBuilderDelegate;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;

import java.util.concurrent.CompletableFuture;

/**
 * The implementation of {@link InviteBuilderDelegate}.
 */
public class InviteBuilderDelegateImpl implements InviteBuilderDelegate {

    /**
     * The server channel for the invite.
     */
    private final ServerChannel channel;

    /**
     * The reason for the creation.
     */
    private String reason = null;

    /**
     * The duration of the invite in seconds before expiry, or 0 for never.
     */
    private int maxAge = 86400;

    /**
     * The max number of uses or 0 for unlimited.
     */
    private int maxUses = 0;

    /**
     * Whether this invite only grants temporary membership or not.
     */
    private boolean temporary = false;

    /**
     * Whether this invite is be unique or not.
     */
    private boolean unique = false;

    /**
     * Creates a new invite builder delegate.
     *
     * @param channel The channel for the invite.
     */
    public InviteBuilderDelegateImpl(ServerChannel channel) {
        this.channel = channel;
    }

    @Override
    public void setAuditLogReason(String reason) {
        this.reason = reason;
    }

    @Override
    public void setMaxAgeInSeconds(int maxAge) {
        this.maxAge = maxAge;
    }

    @Override
    public void setNeverExpire() {
        setMaxAgeInSeconds(0);
    }

    @Override
    public void setMaxUses(int maxUses) {
        this.maxUses = maxUses;
    }

    @Override
    public void setTemporary(boolean temporary) {
        this.temporary = temporary;
    }

    @Override
    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    @Override
    public CompletableFuture<Invite> create() {
        return new RestRequest<Invite>(channel.getApi(), RestMethod.POST, RestEndpoint.CHANNEL_INVITE)
                .setUrlParameters(channel.getIdAsString())
                .setBody(JsonNodeFactory.instance.objectNode()
                                 .put("max_age", maxAge)
                                 .put("max_uses", maxUses)
                                 .put("temporary", temporary)
                                 .put("unique", unique))
                .setAuditLogReason(reason)
                .execute(result -> new InviteImpl(channel.getApi(), result.getJsonBody()));
    }

}
