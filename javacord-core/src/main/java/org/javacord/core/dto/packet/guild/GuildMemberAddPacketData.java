package org.javacord.core.dto.packet.guild;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.javacord.core.dto.entity.MemberDto;

public class GuildMemberAddPacketData {

    private final String guildId;
    @JsonUnwrapped
    private final MemberDto member;

    // Default constructor is necessary, when having fields that are annotated with @JsonUnwrapped
    private GuildMemberAddPacketData() {
        this(null, null);
    }

    public GuildMemberAddPacketData(String guildId, MemberDto member) {
        this.guildId = guildId;
        this.member = member;
    }

    public String getGuildId() {
        return guildId;
    }

    public MemberDto getMember() {
        return member;
    }
}
