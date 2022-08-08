package org.javacord.core.event.server.member;

import org.javacord.api.entity.member.Member;
import org.javacord.api.event.server.member.ServerMemberChangeNicknameEvent;
import java.util.Optional;

/**
 * The implementation of {@link ServerMemberChangeNicknameEvent}.
 */
public class ServerMemberChangeNicknameEventImpl extends ServerMemberEventImpl implements
        ServerMemberChangeNicknameEvent {

    private final Member newMember;
    private final Member oldMember;

    /**
     * Creates a new user change nickname event.
     *
     * @param newMember The new member.
     * @param oldMember The old member.
     */
    public ServerMemberChangeNicknameEventImpl(Member newMember, Member oldMember) {
        super(newMember);
        this.newMember = newMember;
        this.oldMember = oldMember;
    }

    @Override
    public Optional<String> getNewNickname() {
        return newMember.getNickname();
    }

    @Override
    public Optional<String> getOldNickname() {
        return oldMember.getNickname();
    }
}
