package org.javacord.entity.server.invite;

import org.javacord.entity.Icon;
import org.javacord.entity.channel.ChannelType;
import org.javacord.entity.channel.ServerChannel;
import org.javacord.entity.server.Server;
import org.javacord.entity.server.invite.impl.ImplInvite;
import org.javacord.util.rest.RestEndpoint;
import org.javacord.util.rest.RestMethod;
import org.javacord.util.rest.RestRequest;
import org.javacord.entity.Icon;
import org.javacord.entity.server.Server;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * This class represents an invite object.
 * Invite objects won't receive any updates!
 */
public interface Invite {

    /**
     * Gets the code of the invite.
     *
     * @return The code of the invite.
     */
    String getCode();

    /**
     * Gets the url of the invite.
     *
     * @return The url of the invite.
     */
    default URL getUrl() {
        try {
            return new URL("https://discord.gg/" + getCode());
        } catch (MalformedURLException e) {
            // This should never ever happen
            return null;
        }
    }

    /**
     * Gets the server of the invite.
     *
     * @return The server of the invite.
     */
    Optional<Server> getServer();

    /**
     * Gets the id of the server.
     *
     * @return The id of the server.
     */
    long getServerId();

    /**
     * Gets the name of the server.
     *
     * @return The name of the server.
     */
    String getServerName();

    /**
     * Gets the icon of the server.
     *
     * @return The icon of the server.
     */
    Optional<Icon> getServerIcon();

    /**
     * Gets the splash of the server.
     *
     * @return The splash of the server.
     */
    Optional<Icon> getServerSplash();

    /**
     * Gets the channel of the invite.
     *
     * @return The channel of the invite.
     */
    Optional<ServerChannel> getChannel();

    /**
     * Gets the id of the channel.
     *
     * @return The id of the channel.
     */
    long getChannelId();

    /**
     * Gets the name of the channel.
     *
     * @return The name of the channel.
     */
    String getChannelName();

    /**
     * Gets the type of the channel.
     *
     * @return The type of the channel.
     */
    ChannelType getChannelType();

    /**
     * Deletes the invite.
     *
     * @return A future to check if the deletion was successful.
     */
    default CompletableFuture<Void> delete() {
        return delete(null);
    }

    /**
     * Deletes the invite.
     *
     * @param reason The audit log reason for the deletion.
     * @return A future to check if the deletion was successful.
     */
    default CompletableFuture<Void> delete(String reason) {
        return new RestRequest<Void>(((ImplInvite) this).getApi(), RestMethod.DELETE, RestEndpoint.INVITE)
                .setUrlParameters(getCode())
                .setAuditLogReason(reason)
                .execute(result -> null);
    }

}
