package org.javacord.core.interaction;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.ApplicationCommandInteractionData;
import org.javacord.api.interaction.Interaction;
import org.javacord.api.interaction.InteractionComponentData;
import org.javacord.api.interaction.InteractionData;
import org.javacord.api.interaction.InteractionType;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.message.MessageImpl;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.entity.user.MemberImpl;
import org.javacord.core.entity.user.UserImpl;
import org.javacord.core.util.logging.LoggerUtil;

import java.util.Optional;

public class InteractionImpl implements Interaction {

    private static final Logger logger = LoggerUtil.getLogger(InteractionImpl.class);

    private final DiscordApiImpl api;
    private final TextChannel channel;

    private final long id;
    private final long applicationId;
    private final InteractionType type;
    private final InteractionData data;
    private final UserImpl user;
    private final Message message;
    private final String token;
    private final int version;

    /**
     * Class constructor.
     *
     * @param api The api instance.
     * @param channel The channel in which the interaction happened. Can be {@code null}.
     * @param jsonData The json data of the interaction.
     */
    public InteractionImpl(DiscordApiImpl api, TextChannel channel, JsonNode jsonData) {
        this.api = api;
        this.channel = channel;

        id = jsonData.get("id").asLong();
        applicationId = jsonData.get("application_id").asLong();
        type = InteractionType.fromValue(jsonData.get("type").asInt());
        message = jsonData.has("message") ? new MessageImpl(api, channel, jsonData.get("message")) : null;
        if (jsonData.has("data")) {
            if (type == InteractionType.APPLICATION_COMMAND) {
                data = new ApplicationCommandInteractionDataImpl(api, jsonData.get("data"));
            } else if (type == InteractionType.MESSAGE_COMPONENT) {
                data = new InteractionComponentDataImpl(jsonData.get("data"));
            } else {
                data = null;
            }
        } else {
            data = null;
        }
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
    public InteractionType getType() {
        return type;
    }

    @Override
    public Optional<InteractionData> getData() {
        return Optional.ofNullable(data);
    }

    @Override
    public Optional<ApplicationCommandInteractionData> getCommandData() {
        ApplicationCommandInteractionData res = null;
        if (this.type == InteractionType.APPLICATION_COMMAND) {
            res = (ApplicationCommandInteractionDataImpl) this.data;
        }
        return Optional.ofNullable(res);
    }

    @Override
    public Optional<InteractionComponentData> getComponentData() {
        InteractionComponentData res = null;
        if (this.type == InteractionType.MESSAGE_COMPONENT) {
            res = (InteractionComponentDataImpl) this.data;
        }
        return Optional.ofNullable(res);
    }

    @Override
    public Optional<Server> getServer() {
        if (channel instanceof ServerChannel) {
            return Optional.of(((ServerChannel) channel).getServer());
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<TextChannel> getChannel() {
        return Optional.ofNullable(channel);
    }

    @Override
    public Optional<Message> getMessage() {
        return Optional.ofNullable(message);
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
