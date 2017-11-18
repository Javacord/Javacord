package de.btobastian.javacord.entities;

import com.mashape.unirest.http.HttpMethod;
import de.btobastian.javacord.entities.channels.ServerChannel;
import de.btobastian.javacord.entities.impl.ImplInvite;
import de.btobastian.javacord.utils.rest.RestEndpoint;
import de.btobastian.javacord.utils.rest.RestRequest;
import org.json.JSONObject;

import java.util.concurrent.CompletableFuture;

/**
 * A class to create {@link Invite invite} objects.
 */
public class InviteBuilder {

    /**
     * The server channel for the invite.
     */
    private final ServerChannel channel;

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
     * Creates a new invite builder.
     *
     * @param channel The channel for the invite.
     */
    public InviteBuilder(ServerChannel channel) {
        this.channel = channel;
    }

    /**
     * Sets the duration of the invite in seconds before expiry, or 0 for never.
     * The default value is 86400 (24 hours).
     *
     * @param maxAge The duration of the invite in seconds before expiry, or 0 for never.
     * @return The current instance in order to chain call methods.
     */
    public InviteBuilder setMaxAge(int maxAge) {
        this.maxAge = maxAge;
        return this;
    }

    /**
     * Sets the max number of uses or 0 for unlimited.
     * The default value is 0 (unlimited uses).
     *
     * @param maxUses The max number of uses or 0 for unlimited.
     * @return The current instance in order to chain call methods.
     */
    public InviteBuilder setMaxUses(int maxUses) {
        this.maxUses = maxUses;
        return this;
    }

    /**
     * Sets whether this invite should only grant temporary membership or not.
     * The default value is false.
     *
     * @param temporary Whether this invite should only grant temporary membership or not.
     * @return The current instance in order to chain call methods.
     */
    public InviteBuilder setTemporary(boolean temporary) {
        this.temporary = temporary;
        return this;
    }

    /**
     * Sets whether this invite should be unique or not.
     * The default value is false.
     *
     * @param unique Whether this invite should be unique or not.
     * @return The current instance in order to chain call methods.
     */
    public InviteBuilder setUnique(boolean unique) {
        this.unique = unique;
        return this;
    }

    /**
     * Creates the invite.
     *
     * @return The created invite.
     */
    public CompletableFuture<Invite> build() {
        return new RestRequest<Invite>(channel.getApi(), HttpMethod.POST, RestEndpoint.CHANNEL_INVITE)
                .setUrlParameters(channel.getIdAsString())
                .setBody(new JSONObject()
                        .put("max_age", maxAge)
                        .put("max_uses", maxUses)
                        .put("temporary", temporary)
                        .put("unique", unique))
                .execute(res -> new ImplInvite(channel.getApi(), res.getBody().getObject()));
    }

}
