package de.btobastian.javacord.entities.impl;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entities.*;
import de.btobastian.javacord.entities.channels.ChannelType;
import de.btobastian.javacord.entities.channels.ServerChannel;
import de.btobastian.javacord.utils.logging.LoggerUtil;
import org.json.JSONObject;
import org.slf4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Optional;

/**
 * The implementation of {@link Invite}.
 */
public class ImplInvite implements RichInvite {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(ImplIcon.class);

    /**
     * The discord api instance.
     */
    private final DiscordApi api;

    /**
     * The code of the invite.
     */
    private final String code;

    /**
     * The id of the server.
     */
    private final long serverId;

    /**
     * The name of the server.
     */
    private final String serverName;

    /**
     * The icon hash of the server.
     */
    private final String serverIcon;

    /**
     * The splash of the server.
     */
    private final String serverSplash;

    /**
     * The id of the channel.
     */
    private final long channelId;

    /**
     * The name of the channel.
     */
    private final String channelName;

    /**
     * The type of the channel.
     */
    private final ChannelType channelType;

    /**
     * The creator of the invite. May be <code>null</code>.
     */
    private final User inviter;

    /**
     * The number of times this invite has been used.
     */
    private final int uses;

    /**
     * The max number of times this invite can be used.
     */
    private final int maxUses;

    /**
     * The duration (in seconds) after which the invite expires.
     */
    private final int maxAge;

    /**
     * Whether the invite only grants temporary membership or not.
     */
    private final boolean temporary;

    /**
     * The creation date of the invite.
     */
    private final Instant creationTimestamp;

    /**
     * Whether the invite is revoked or not.
     */
    private final boolean revoked;

    /**
     * Creates a new invite.
     *
     * @param api The discord api instance.
     * @param data The json data of the invite.
     */
    public ImplInvite(DiscordApi api, JSONObject data) {
        this.api = api;
        this.code = data.getString("code");
        this.serverId = Long.parseLong(data.getJSONObject("guild").getString("id"));
        this.serverName = data.getJSONObject("guild").getString("name");
        this.serverIcon = data.getJSONObject("guild").has("icon") && !data.getJSONObject("guild").isNull("icon") ?
                data.getJSONObject("guild").getString("icon") : null;
        this.serverSplash = data.getJSONObject("guild").has("splash") && !data.getJSONObject("guild").isNull("splash") ?
                data.getJSONObject("guild").getString("splash") : null;
        this.channelId = Long.parseLong(data.getJSONObject("channel").getString("id"));
        this.channelName = data.getJSONObject("channel").getString("name");
        this.channelType = ChannelType.fromId(data.getJSONObject("channel").getInt("type"));

        // Rich data (may not be present)
        this.inviter = data.has("inviter") ?
                ((ImplDiscordApi) api).getOrCreateUser(data.getJSONObject("inviter")) : null;
        this.uses = data.has("uses") ? data.getInt("uses") : -1;
        this.maxUses = data.has("max_uses") ? data.getInt("max_uses") : -1;
        this.maxAge = data.has("max_age") ? data.getInt("max_age") : -1;
        this.temporary = data.has("temporary") && data.getBoolean("temporary");
        this.creationTimestamp = data.has("created_at") ?
                OffsetDateTime.parse(data.getString("created_at")).toInstant() : null;
        this.revoked = data.has("revoked") && data.getBoolean("revoked");
    }

    /**
     * Gets the discord api instance.
     *
     * @return The discord api instance.
     */
    public DiscordApi getApi() {
        return api;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public Optional<Server> getServer() {
        return api.getServerById(serverId);
    }

    @Override
    public long getServerId() {
        return serverId;
    }

    @Override
    public String getServerName() {
        return serverName;
    }

    @Override
    public Optional<Icon> getServerIcon() {
        if (serverIcon == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(new ImplIcon(
                    api, new URL("https://cdn.discordapp.com/icons/" + getServerId() + "/" + serverIcon + ".png")));
        } catch (MalformedURLException e) {
            logger.warn("Seems like the url of the icon is malformed! Please contact the developer!", e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Icon> getServerSplash() {
        if (serverSplash == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(new ImplIcon(
                    api, new URL("https://cdn.discordapp.com/splashes/" + getServerId() + "/" + serverSplash + ".png")));
        } catch (MalformedURLException e) {
            logger.warn("Seems like the url of the icon is malformed! Please contact the developer!", e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<ServerChannel> getChannel() {
        return api.getServerChannelById(channelId);
    }

    @Override
    public long getChannelId() {
        return channelId;
    }

    @Override
    public String getChannelName() {
        return channelName;
    }

    @Override
    public ChannelType getChannelType() {
        return channelType;
    }

    @Override
    public User getInviter() {
        return inviter;
    }

    @Override
    public int getUses() {
        return uses;
    }

    @Override
    public int getMaxUses() {
        return maxUses;
    }

    @Override
    public int getMaxAge() {
        return maxAge;
    }

    @Override
    public boolean isTemporary() {
        return temporary;
    }

    @Override
    public Instant getCreationTimestamp() {
        return creationTimestamp;
    }

    @Override
    public boolean isRevoked() {
        return revoked;
    }
}
