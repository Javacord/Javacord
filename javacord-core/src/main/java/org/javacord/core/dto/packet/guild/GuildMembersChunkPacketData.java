package org.javacord.core.dto.packet.guild;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.javacord.core.dto.entity.MemberDto;
import org.javacord.core.dto.entity.PresenceDto;
import org.jetbrains.annotations.Nullable;

public class GuildMembersChunkPacketData {

    private final String guildId;
    private final MemberDto[] members;
    private final int chunkIndex;
    private final int chunkCount;
    @Nullable
    private final String[] notFound;
    @Nullable
    private final PresenceDto[] presences;
    private final String nonce;

    @JsonCreator
    public GuildMembersChunkPacketData(String guildId, MemberDto[] members, int chunkIndex, int chunkCount, @Nullable String[] notFound, @Nullable PresenceDto[] presences, String nonce) {
        this.guildId = guildId;
        this.members = members;
        this.chunkIndex = chunkIndex;
        this.chunkCount = chunkCount;
        this.notFound = notFound;
        this.presences = presences;
        this.nonce = nonce;
    }

    public String getGuildId() {
        return guildId;
    }

    public MemberDto[] getMembers() {
        return members;
    }

    public int getChunkIndex() {
        return chunkIndex;
    }

    public int getChunkCount() {
        return chunkCount;
    }

    public String[] getNotFound() {
        return notFound;
    }

    public PresenceDto[] getPresences() {
        return presences;
    }

    public String getNonce() {
        return nonce;
    }
}
