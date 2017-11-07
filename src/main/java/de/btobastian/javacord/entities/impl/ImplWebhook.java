package de.btobastian.javacord.entities.impl;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.Webhook;
import de.btobastian.javacord.entities.channels.TextChannel;
import org.json.JSONObject;

import java.util.Optional;

/**
 * The implementation of {@link Webhook}.
 */
public class ImplWebhook implements Webhook {

    private final ImplDiscordApi api;

    private final long id;
    private final Server server;
    private final TextChannel channel;
    private final User user;
    private final String name;
    private final String token;

    /**
     * Creates a new webhook.
     *
     * @param api The discord api instance.
     * @param data The json data of the webhook.
     */
    public ImplWebhook(DiscordApi api, JSONObject data) {
        this.api = (ImplDiscordApi) api;

        this.id = Long.parseLong(data.getString("id"));
        this.server = data.has("guild_id") ? api.getServerById(data.getString("guild_id")).orElse(null) : null;
        this.channel = api.getTextChannelById(data.getString("channel_id")).orElse(null);
        this.user = data.has("user") ? this.api.getOrCreateUser(data.getJSONObject("user")) : null;
        this.name = data.has("name") && !data.isNull("name") ? data.getString("name") : null;
        this.token = data.getString("token");
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
    public Optional<Server> getServer() {
        return Optional.ofNullable(server);
    }

    @Override
    public TextChannel getChannel() {
        return channel;
    }

    @Override
    public Optional<User> getCreator() {
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    @Override
    public String getToken() {
        return token;
    }
}
