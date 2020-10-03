package org.javacord.core.util.gateway;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import org.apache.logging.log4j.Logger;
import org.javacord.core.dto.packet.PacketDto;
import org.javacord.core.dto.packet.channel.ChannelCreatePacketData;
import org.javacord.core.dto.packet.channel.ChannelDeletePacketData;
import org.javacord.core.dto.packet.channel.ChannelPinsUpdatePacketData;
import org.javacord.core.dto.packet.channel.ChannelUpdatePacketData;
import org.javacord.core.dto.packet.connection.HelloPacketData;
import org.javacord.core.dto.packet.guild.GuildBanAddPacketData;
import org.javacord.core.dto.packet.guild.GuildBanRemovePacketData;
import org.javacord.core.dto.packet.guild.GuildCreatePacketData;
import org.javacord.core.dto.packet.guild.GuildDeletePacketData;
import org.javacord.core.dto.packet.guild.GuildEmojisUpdatePacketData;
import org.javacord.core.dto.packet.guild.GuildIntegrationsUpdatePacketData;
import org.javacord.core.dto.packet.guild.GuildMemberAddPacketData;
import org.javacord.core.dto.packet.guild.GuildMemberRemovePacketData;
import org.javacord.core.dto.packet.guild.GuildMemberUpdatePacketData;
import org.javacord.core.dto.packet.guild.GuildMembersChunkPacketData;
import org.javacord.core.dto.packet.guild.GuildRoleCreatePacketData;
import org.javacord.core.dto.packet.guild.GuildRoleDeletePacketData;
import org.javacord.core.dto.packet.guild.GuildRoleUpdatePacketData;
import org.javacord.core.dto.packet.guild.GuildUpdatePacketData;
import org.javacord.core.dto.packet.invite.InviteCreatePacketData;
import org.javacord.core.dto.packet.invite.InviteDeletePacketData;
import org.javacord.core.dto.packet.message.MessageCreatePacketData;
import org.javacord.core.dto.packet.message.MessageDeleteBulkPacketData;
import org.javacord.core.dto.packet.message.MessageDeletePacketData;
import org.javacord.core.dto.packet.message.MessageReactionAddPacketData;
import org.javacord.core.dto.packet.message.MessageReactionRemoveAllPacketData;
import org.javacord.core.dto.packet.message.MessageReactionRemoveEmojiPacketData;
import org.javacord.core.dto.packet.message.MessageReactionRemovePacketData;
import org.javacord.core.dto.packet.message.MessageUpdatePacketData;
import org.javacord.core.dto.packet.presence.PresenceUpdatePacketData;
import org.javacord.core.dto.packet.presence.TypingStartPacketData;
import org.javacord.core.dto.packet.presence.UserUpdatePacketData;
import org.javacord.core.dto.packet.voice.VoiceServerUpdatePacketData;
import org.javacord.core.dto.packet.voice.VoiceStateUpdatePacketData;
import org.javacord.core.dto.packet.webhook.WebhooksUpdatePacketData;
import org.javacord.core.util.logging.LoggerUtil;

import java.io.IOException;

/**
 * Efficient Jackson deserializer for the {@link PacketDto} class.
 */
public class PacketDeserializer extends StdDeserializer<PacketDto> {

    private static final Logger logger = LoggerUtil.getLogger(PacketDeserializer.class);

    private static HashMap<Tuple2<GatewayOpcode, String>, Class<?>> classMap = HashMap.ofEntries(
            Tuple.of(Tuple.of(GatewayOpcode.HELLO, null), HelloPacketData.class),
            // Connecting and Resuming
            Tuple.of(Tuple.of(GatewayOpcode.DISPATCH, "RESUMED"), null),
            Tuple.of(Tuple.of(GatewayOpcode.DISPATCH, "RECONNECT"), null),
            Tuple.of(Tuple.of(GatewayOpcode.INVALID_SESSION, null), Boolean.class),
            // Channels
            Tuple.of(Tuple.of(GatewayOpcode.DISPATCH, "CHANNEL_CREATE"), ChannelCreatePacketData.class),
            Tuple.of(Tuple.of(GatewayOpcode.DISPATCH, "CHANNEL_UPDATE"), ChannelUpdatePacketData.class),
            Tuple.of(Tuple.of(GatewayOpcode.DISPATCH, "CHANNEL_DELETE"), ChannelDeletePacketData.class),
            Tuple.of(Tuple.of(GatewayOpcode.DISPATCH, "CHANNEL_PINS_UPDATE"), ChannelPinsUpdatePacketData.class),
            // Guilds
            Tuple.of(Tuple.of(GatewayOpcode.DISPATCH, "GUILD_CREATE"), GuildCreatePacketData.class),
            Tuple.of(Tuple.of(GatewayOpcode.DISPATCH, "GUILD_UPDATE"), GuildUpdatePacketData.class),
            Tuple.of(Tuple.of(GatewayOpcode.DISPATCH, "GUILD_DELETE"), GuildDeletePacketData.class),
            Tuple.of(Tuple.of(GatewayOpcode.DISPATCH, "GUILD_BAN_ADD"), GuildBanAddPacketData.class),
            Tuple.of(Tuple.of(GatewayOpcode.DISPATCH, "GUILD_BAN_REMOVE"), GuildBanRemovePacketData.class),
            Tuple.of(Tuple.of(GatewayOpcode.DISPATCH, "GUILD_EMOJIS_UPDATE"), GuildEmojisUpdatePacketData.class),
            Tuple.of(Tuple.of(GatewayOpcode.DISPATCH, "GUILD_INTEGRATIONS_UPDATE"),
                    GuildIntegrationsUpdatePacketData.class),
            Tuple.of(Tuple.of(GatewayOpcode.DISPATCH, "GUILD_MEMBER_ADD"), GuildMemberAddPacketData.class),
            Tuple.of(Tuple.of(GatewayOpcode.DISPATCH, "GUILD_MEMBER_REMOVE"), GuildMemberRemovePacketData.class),
            Tuple.of(Tuple.of(GatewayOpcode.DISPATCH, "GUILD_MEMBER_UPDATE"), GuildMemberUpdatePacketData.class),
            Tuple.of(Tuple.of(GatewayOpcode.DISPATCH, "GUILD_MEMBERS_CHUNK"), GuildMembersChunkPacketData.class),
            Tuple.of(Tuple.of(GatewayOpcode.DISPATCH, "GUILD_ROLE_CREATE"), GuildRoleCreatePacketData.class),
            Tuple.of(Tuple.of(GatewayOpcode.DISPATCH, "GUILD_ROLE_UPDATE"), GuildRoleUpdatePacketData.class),
            Tuple.of(Tuple.of(GatewayOpcode.DISPATCH, "GUILD_ROLE_DELETE"), GuildRoleDeletePacketData.class),
            // Invites
            Tuple.of(Tuple.of(GatewayOpcode.DISPATCH, "INVITE_CREATE"), InviteCreatePacketData.class),
            Tuple.of(Tuple.of(GatewayOpcode.DISPATCH, "INVITE_DELETE"), InviteDeletePacketData.class),
            // Messages
            Tuple.of(Tuple.of(GatewayOpcode.DISPATCH, "MESSAGE_CREATE"), MessageCreatePacketData.class),
            Tuple.of(Tuple.of(GatewayOpcode.DISPATCH, "MESSAGE_UPDATE"), MessageUpdatePacketData.class),
            Tuple.of(Tuple.of(GatewayOpcode.DISPATCH, "MESSAGE_DELETE"), MessageDeletePacketData.class),
            Tuple.of(Tuple.of(GatewayOpcode.DISPATCH, "MESSAGE_DELETE_BULK"), MessageDeleteBulkPacketData.class),
            Tuple.of(Tuple.of(GatewayOpcode.DISPATCH, "MESSAGE_REACTION_ADD"), MessageReactionAddPacketData.class),
            Tuple.of(Tuple.of(GatewayOpcode.DISPATCH, "MESSAGE_REACTION_REMOVE"),
                    MessageReactionRemovePacketData.class),
            Tuple.of(Tuple.of(GatewayOpcode.DISPATCH, "MESSAGE_REACTION_REMOVE_ALL"),
                    MessageReactionRemoveAllPacketData.class),
            Tuple.of(Tuple.of(GatewayOpcode.DISPATCH, "MESSAGE_REACTION_REMOVE_EMOJI"),
                    MessageReactionRemoveEmojiPacketData.class),
            // Presence
            Tuple.of(Tuple.of(GatewayOpcode.DISPATCH, "PRESENCE_UPDATE"), PresenceUpdatePacketData.class),
            Tuple.of(Tuple.of(GatewayOpcode.DISPATCH, "TYPING_START"), TypingStartPacketData.class),
            Tuple.of(Tuple.of(GatewayOpcode.DISPATCH, "USER_UPDATE"), UserUpdatePacketData.class),
            // Voice
            Tuple.of(Tuple.of(GatewayOpcode.DISPATCH, "VOICE_STATE_UPDATE"), VoiceStateUpdatePacketData.class),
            Tuple.of(Tuple.of(GatewayOpcode.DISPATCH, "VOICE_SERVER_UPDATE"), VoiceServerUpdatePacketData.class),
            // Webhooks
            Tuple.of(Tuple.of(GatewayOpcode.DISPATCH, "WEBHOOKS_UPDATE"), WebhooksUpdatePacketData.class)
    );

    private PacketDeserializer() {
        this(null);
    }

    private PacketDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public PacketDto deserialize(JsonParser parser, DeserializationContext deserializationContext) throws IOException {
        // Parsing Discord packets is challenging: They have two field ('op' and 't') that are used to mark the type
        // of a data field 'd'. This parser tries to take advantage of Jackson's streaming api which reads the JSON
        // input token by token. If we already collected all necessary information to determine the type of the data
        // field ('d'), we directly parse deserialize the data. If the information is not available when reaching the
        // 'd' field, we
        int op = -1;
        boolean foundOp = false;
        String type = null;
        boolean foundType = false;
        Object data = null;
        boolean parsedData = false;
        Integer sequenceNumber = null;

        TokenBuffer dataBuffer = null;

        while (!parser.isClosed()) {
            JsonToken token = parser.nextToken();
            if (token == JsonToken.FIELD_NAME) {
                String fieldName = parser.getCurrentName();
                parser.nextToken();

                switch (fieldName) {
                    case "op":
                        op = parser.getValueAsInt(-1);
                        foundOp = true;
                        break;
                    case "t":
                        type = parser.getValueAsString();
                        foundType = true;
                        break;
                    case "s":
                        sequenceNumber = parser.getValueAsInt(-1);
                        break;
                    case "d":
                        if (!foundOp || (op == 0 && !foundType)) {
                            dataBuffer = parser.readValueAs(TokenBuffer.class);
                        } else {
                            Class<?> dataClass = getDataClassByOpAndType(op, type);
                            if (dataClass != null) {
                                data = deserializationContext.readValue(parser, getDataClassByOpAndType(op, type));
                            }
                            parsedData = true;
                        }
                        break;
                    default:
                        break;
                }
            }
        }

        if (!parsedData && dataBuffer != null) {
            Class<?> dataClass = getDataClassByOpAndType(op, type);
            if (dataClass != null) {
                data = dataBuffer.asParser(parser.getCodec()).readValueAs(getDataClassByOpAndType(op, type));
            }
        }

        return new PacketDto(op, sequenceNumber, type, data);
    }

    private static Class<?> getDataClassByOpAndType(int op, String type) {
        return classMap.getOrElse(Tuple.of(GatewayOpcode.fromCode(op).orElse(null), type), null);
    }
}
