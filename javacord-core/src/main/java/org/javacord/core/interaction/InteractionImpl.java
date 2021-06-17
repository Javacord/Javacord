package org.javacord.core.interaction;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.Interaction;
import org.javacord.api.interaction.InteractionType;
import org.javacord.api.interaction.callback.InteractionFollowupMessageBuilder;
import org.javacord.api.interaction.callback.InteractionImmediateResponseBuilder;
import org.javacord.api.interaction.callback.InteractionOriginalResponseUpdater;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.message.InteractionCallbackType;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.entity.user.MemberImpl;
import org.javacord.core.entity.user.UserImpl;
import org.javacord.core.util.logging.LoggerUtil;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public abstract class InteractionImpl implements Interaction {

    private static final Logger logger = LoggerUtil.getLogger(InteractionImpl.class);

    private static final String RESPOND_LATER_BODY =
            "{\"type\": " + InteractionCallbackType.DEFERRED_CHANNEL_MESSAGE_WITH_SOURCE.getId() + "}";

    private final DiscordApiImpl api;
    private final TextChannel channel;

    private final long id;
    private final long applicationId;
    private final UserImpl user;
    private final String token;
    private final int version;

    /**
     * Class constructor.
     *
     * @param api      The api instance.
     * @param channel  The channel in which the interaction happened. Can be {@code null}.
     * @param jsonData The json data of the interaction.
     */
    public InteractionImpl(DiscordApiImpl api, TextChannel channel, JsonNode jsonData) {
        this.api = api;
        this.channel = channel;

        id = jsonData.get("id").asLong();
        applicationId = jsonData.get("application_id").asLong();
        if (jsonData.hasNonNull("member")) {
            MemberImpl member = new MemberImpl(
                    api,
                    (ServerImpl) getServer().orElseThrow(AssertionError::new),
                    jsonData.get("member"),
                    null
            );
            user = (UserImpl) member.getUser();
        } else if (jsonData.hasNonNull("user")) {
            user = new UserImpl(api, jsonData.get("user"), (MemberImpl) null, null);
        } else {
            user = null;
            logger.error("Received interaction without a member AND without a user field");
        }
        token = jsonData.get("token").asText();
        version = jsonData.get("version").asInt();
    }

    @Override
    public DiscordApi getApi() {
        return api;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public long getApplicationId() {
        return applicationId;
    }

    @Override
    public abstract InteractionType getType();

    @Override
    public InteractionImmediateResponseBuilder createImmediateResponder() {
        return new InteractionImmediateResponseBuilderImpl(this);
    }

    @Override
    public CompletableFuture<InteractionOriginalResponseUpdater> respondLater() {
        return new RestRequest<InteractionOriginalResponseUpdater>(this.api,
                RestMethod.POST, RestEndpoint.INTERACTION_RESPONSE)
                .setUrlParameters(getIdAsString(), token)
                .setBody(RESPOND_LATER_BODY)
                .execute(result -> new InteractionOriginalResponseUpdaterImpl(this));
    }

    @Override
    public InteractionFollowupMessageBuilder createFollowupMessageBuilder() {
        return new InteractionFollowupMessageBuilderImpl(this);
    }

    @Override
    public Optional<Server> getServer() {
        return channel.asServerChannel().map(ServerChannel::getServer);
    }

    @Override
    public Optional<TextChannel> getChannel() {
        return Optional.ofNullable(channel);
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public int getVersion() {
        return version;
    }
}
