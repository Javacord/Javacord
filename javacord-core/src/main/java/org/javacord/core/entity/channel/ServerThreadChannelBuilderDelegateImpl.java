package org.javacord.core.entity.channel;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.channel.ChannelType;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.ServerThreadChannel;
import org.javacord.api.entity.channel.internal.ServerThreadChannelBuilderDelegate;
import org.javacord.api.entity.message.Message;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;
import java.util.concurrent.CompletableFuture;

/**
 * The implementation of {@link ServerThreadChannelBuilderDelegate}.
 */
public class ServerThreadChannelBuilderDelegateImpl extends ServerChannelBuilderDelegateImpl
        implements ServerThreadChannelBuilderDelegate {

    /**
     * The slowmode duration of this thread.
     */
    private Integer slowMode = null;
    /**
     * The server thread channel type for this to be created thread.
     */
    private ChannelType channelType = null;
    /**
     * The auto archive duration of this thread.
     */
    private Integer autoArchiveDuration = null;
    /**
     * The inviteable flag of this thread.
     */
    private Boolean inviteable = null;
    /**
     * The message this thread should be created for. Either this is set or the ServerTextChannel property.
     */
    private Message message = null;
    /**
     * The server thread channel his thread should be created in. Either this is set or the Message property.
     */
    private ServerTextChannel serverTextChannel = null;

    /**
     * Creates a new server thread channel builder delegate.
     *
     * @param serverTextChannel The server text channel where the thread will be created in.
     */
    public ServerThreadChannelBuilderDelegateImpl(ServerTextChannel serverTextChannel) {
        super((ServerImpl) serverTextChannel.getServer());
        this.serverTextChannel = serverTextChannel;
    }

    /**
     * Creates a new server thread channel builder delegate.
     *
     * @param message The message where this thread will be created for.
     */
    public ServerThreadChannelBuilderDelegateImpl(Message message) {
        super((ServerImpl) message.getServer()
                .orElseThrow(() -> new IllegalArgumentException("Message must be from a server")));
        this.message = message;
    }

    @Override
    public void setInvitableFlag(Boolean inviteable) {
        this.inviteable = inviteable;
    }

    @Override
    public void setChannelType(ChannelType channelType) {
        this.channelType = channelType;
    }

    @Override
    public void setAutoArchiveDuration(Integer autoArchiveDuration) {
        this.autoArchiveDuration = autoArchiveDuration;
    }

    @Override
    public void setSlowmodeDelayInSeconds(int delay) {
        this.slowMode = delay;
    }

    @Override
    protected void prepareBody(ObjectNode body) {
        super.prepareBody(body);
        if (channelType != null) {
            body.put("type", channelType.getId());
        }

        if (slowMode != null) {
            body.put("rate_limit_per_user", slowMode);
        }

        if (autoArchiveDuration != null) {
            body.put("auto_archive_duration", autoArchiveDuration);
        }

        if (inviteable != null) {
            body.put("invitable", inviteable);
        }
    }

    @Override
    public CompletableFuture<ServerThreadChannel> create() {
        final ObjectNode body = JsonNodeFactory.instance.objectNode();
        prepareBody(body);

        if (message != null) {
            return new RestRequest<ServerThreadChannel>(message.getApi(), RestMethod.POST,
                    RestEndpoint.START_THREAD_WITH_MESSAGE).setUrlParameters(message.getChannel().getIdAsString(),
                    message.getIdAsString()).setBody(body).execute(result -> ((ServerImpl) message.getServer().get())
                    .getOrCreateServerThreadChannel(result.getJsonBody()));
        } else {
            return new RestRequest<ServerThreadChannel>(serverTextChannel.getApi(), RestMethod.POST,
                    RestEndpoint.START_THREAD_WITHOUT_MESSAGE).setUrlParameters(serverTextChannel.getIdAsString())
                    .setBody(body).execute(
                            result -> ((ServerImpl) serverTextChannel.getServer()).getOrCreateServerThreadChannel(
                                    result.getJsonBody()));
        }
    }

}
