package org.javacord.core.util.handler.guild;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.Region;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.server.DefaultMessageNotificationLevel;
import org.javacord.api.entity.server.ExplicitContentFilterLevel;
import org.javacord.api.entity.server.MultiFactorAuthenticationLevel;
import org.javacord.api.entity.server.VerificationLevel;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.server.ServerChangeAfkChannelEvent;
import org.javacord.api.event.server.ServerChangeAfkTimeoutEvent;
import org.javacord.api.event.server.ServerChangeDefaultMessageNotificationLevelEvent;
import org.javacord.api.event.server.ServerChangeExplicitContentFilterLevelEvent;
import org.javacord.api.event.server.ServerChangeIconEvent;
import org.javacord.api.event.server.ServerChangeMultiFactorAuthenticationLevelEvent;
import org.javacord.api.event.server.ServerChangeNameEvent;
import org.javacord.api.event.server.ServerChangeOwnerEvent;
import org.javacord.api.event.server.ServerChangeRegionEvent;
import org.javacord.api.event.server.ServerChangeSplashEvent;
import org.javacord.api.event.server.ServerChangeSystemChannelEvent;
import org.javacord.api.event.server.ServerChangeVerificationLevelEvent;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.event.server.ServerChangeAfkChannelEventImpl;
import org.javacord.core.event.server.ServerChangeAfkTimeoutEventImpl;
import org.javacord.core.event.server.ServerChangeDefaultMessageNotificationLevelEventImpl;
import org.javacord.core.event.server.ServerChangeExplicitContentFilterLevelEventImpl;
import org.javacord.core.event.server.ServerChangeIconEventImpl;
import org.javacord.core.event.server.ServerChangeMultiFactorAuthenticationLevelEventImpl;
import org.javacord.core.event.server.ServerChangeNameEventImpl;
import org.javacord.core.event.server.ServerChangeOwnerEventImpl;
import org.javacord.core.event.server.ServerChangeRegionEventImpl;
import org.javacord.core.event.server.ServerChangeSplashEventImpl;
import org.javacord.core.event.server.ServerChangeSystemChannelEventImpl;
import org.javacord.core.event.server.ServerChangeVerificationLevelEventImpl;
import org.javacord.core.util.gateway.PacketHandler;

import java.util.Objects;

/**
 * Handles the guild update packet.
 */
public class GuildUpdateHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public GuildUpdateHandler(DiscordApi api) {
        super(api, true, "GUILD_UPDATE");
    }

    @Override
    public void handle(JsonNode packet) {
        if (packet.has("unavailable") && packet.get("unavailable").asBoolean()) {
            return;
        }
        long id = packet.get("id").asLong();
        api.getPossiblyUnreadyServerById(id).map(server -> (ServerImpl) server).ifPresent(server -> {
            long oldApplicationId = server.getApplicationId().orElse(-1L);
            long newApplicationId = packet.hasNonNull("application_id") ? packet.get("application_id").asLong() : -1L;
            if (oldApplicationId != newApplicationId) {
                server.setApplicationId(newApplicationId);
            }

            String newName = packet.get("name").asText();
            String oldName = server.getName();
            if (!Objects.deepEquals(oldName, newName)) {
                server.setName(newName);
                ServerChangeNameEvent event = new ServerChangeNameEventImpl(server, newName, oldName);

                api.getEventDispatcher().dispatchServerChangeNameEvent(server, server, event);
            }

            String newIconHash = packet.get("icon").asText(null);
            String oldIconHash = server.getIconHash();
            if (!Objects.deepEquals(oldIconHash, newIconHash)) {
                server.setIconHash(newIconHash);
                ServerChangeIconEvent event = new ServerChangeIconEventImpl(server, newIconHash, oldIconHash);

                api.getEventDispatcher().dispatchServerChangeIconEvent(server, server, event);
            }

            String newSplashHash = packet.get("splash").asText(null);
            String oldSplashHash = server.getSplashHash();
            if (!Objects.deepEquals(oldSplashHash, newSplashHash)) {
                server.setSplashHash(newSplashHash);
                ServerChangeSplashEvent event = new ServerChangeSplashEventImpl(server, newSplashHash, oldSplashHash);

                api.getEventDispatcher().dispatchServerChangeSplashEvent(server, server, event);
            }

            VerificationLevel newVerificationLevel = VerificationLevel.fromId(packet.get("verification_level").asInt());
            VerificationLevel oldVerificationLevel = server.getVerificationLevel();
            if (newVerificationLevel != oldVerificationLevel) {
                server.setVerificationLevel(newVerificationLevel);
                ServerChangeVerificationLevelEvent event = new ServerChangeVerificationLevelEventImpl(
                        server, newVerificationLevel, oldVerificationLevel);

                api.getEventDispatcher().dispatchServerChangeVerificationLevelEvent(server, server, event);
            }

            Region newRegion = Region.getRegionByKey(packet.get("region").asText());
            Region oldRegion = server.getRegion();
            if (oldRegion != newRegion) {
                server.setRegion(newRegion);
                ServerChangeRegionEvent event = new ServerChangeRegionEventImpl(server, newRegion, oldRegion);

                api.getEventDispatcher().dispatchServerChangeRegionEvent(server, server, event);
            }

            DefaultMessageNotificationLevel newDefaultMessageNotificationLevel =
                    DefaultMessageNotificationLevel.fromId(packet.get("default_message_notifications").asInt());
            DefaultMessageNotificationLevel oldDefaultMessageNotificationLevel =
                    server.getDefaultMessageNotificationLevel();
            if (newDefaultMessageNotificationLevel != oldDefaultMessageNotificationLevel) {
                server.setDefaultMessageNotificationLevel(newDefaultMessageNotificationLevel);
                ServerChangeDefaultMessageNotificationLevelEvent event =
                        new ServerChangeDefaultMessageNotificationLevelEventImpl(
                                server, newDefaultMessageNotificationLevel, oldDefaultMessageNotificationLevel);

                api.getEventDispatcher().dispatchServerChangeDefaultMessageNotificationLevelEvent(
                        server, server, event);
            }

            long newOwnerId = packet.get("owner_id").asLong();
            User oldOwner = server.getOwner();
            if (!api.getCachedUserById(newOwnerId).map(oldOwner::equals).orElse(false)) {
                server.setOwnerId(newOwnerId);
                ServerChangeOwnerEvent event = new ServerChangeOwnerEventImpl(server, newOwnerId, oldOwner);

                api.getEventDispatcher().dispatchServerChangeOwnerEvent(server, server, event);
            }

            if (packet.has("system_channel_id")) {
                ServerTextChannel newSystemChannel = packet.get("system_channel_id").isNull()
                        ? null
                        : server.getTextChannelById(packet.get("system_channel_id").asLong()).orElse(null);
                ServerTextChannel oldSystemChannel = server.getSystemChannel().orElse(null);
                if (oldSystemChannel != newSystemChannel) {
                    server.setSystemChannelId(newSystemChannel == null ? -1 : newSystemChannel.getId());
                    ServerChangeSystemChannelEvent event =
                            new ServerChangeSystemChannelEventImpl(server, newSystemChannel, oldSystemChannel);

                    api.getEventDispatcher().dispatchServerChangeSystemChannelEvent(server, server, event);
                }
            }

            if (packet.has("afk_channel_id")) {
                ServerVoiceChannel newAfkChannel = packet.get("afk_channel_id").isNull()
                        ? null
                        : server.getVoiceChannelById(packet.get("afk_channel_id").asLong()).orElse(null);
                ServerVoiceChannel oldAfkChannel = server.getAfkChannel().orElse(null);
                if (oldAfkChannel != newAfkChannel) {
                    server.setAfkChannelId(newAfkChannel == null ? -1 : newAfkChannel.getId());
                    ServerChangeAfkChannelEvent event =
                            new ServerChangeAfkChannelEventImpl(server, newAfkChannel, oldAfkChannel);

                    api.getEventDispatcher().dispatchServerChangeAfkChannelEvent(server, server, event);
                }
            }

            int newAfkTimeout = packet.get("afk_timeout").asInt();
            int oldAfkTimeout = server.getAfkTimeoutInSeconds();
            if (oldAfkTimeout != newAfkTimeout) {
                server.setAfkTimeout(newAfkTimeout);
                ServerChangeAfkTimeoutEvent event =
                        new ServerChangeAfkTimeoutEventImpl(server, newAfkTimeout, oldAfkTimeout);

                api.getEventDispatcher().dispatchServerChangeAfkTimeoutEvent(server, server, event);
            }

            ExplicitContentFilterLevel newExplicitContentFilterLevel =
                    ExplicitContentFilterLevel.fromId(packet.get("explicit_content_filter").asInt());
            ExplicitContentFilterLevel oldExplicitContentFilterLevel = server.getExplicitContentFilterLevel();
            if (oldExplicitContentFilterLevel != newExplicitContentFilterLevel) {
                server.setExplicitContentFilterLevel(newExplicitContentFilterLevel);
                ServerChangeExplicitContentFilterLevelEvent event = new ServerChangeExplicitContentFilterLevelEventImpl(
                        server, newExplicitContentFilterLevel, oldExplicitContentFilterLevel);

                api.getEventDispatcher().dispatchServerChangeExplicitContentFilterLevelEvent(server, server, event);
            }

            MultiFactorAuthenticationLevel newMultiFactorAuthenticationLevel =
                    MultiFactorAuthenticationLevel.fromId(packet.get("mfa_level").asInt());
            MultiFactorAuthenticationLevel oldMultiFactorAuthenticationLevel =
                    server.getMultiFactorAuthenticationLevel();
            if (oldMultiFactorAuthenticationLevel != newMultiFactorAuthenticationLevel) {
                server.setMultiFactorAuthenticationLevel(newMultiFactorAuthenticationLevel);
                ServerChangeMultiFactorAuthenticationLevelEvent event =
                        new ServerChangeMultiFactorAuthenticationLevelEventImpl(
                                server, newMultiFactorAuthenticationLevel, oldMultiFactorAuthenticationLevel);

                api.getEventDispatcher().dispatchServerChangeMultiFactorAuthenticationLevelEvent(server, server, event);
            }
        });
    }

}
