package org.javacord.core.event.user;

import org.javacord.api.event.user.UserChangeNicknameEvent;
import org.javacord.core.entity.user.Member;

import java.util.Optional;

/**
 * The implementation of {@link UserChangeNicknameEvent}.
 */
public class UserChangeNicknameEventImpl extends ServerUserEventImpl implements UserChangeNicknameEvent {

    private final Member newMember;
    private final Member oldMember;

    /**
     * Creates a new user change nickname event.
     *
     * @param newMember The new member.
     * @param oldMember The old member.
     */
    public UserChangeNicknameEventImpl(Member newMember, Member oldMember) {
        super(newMember.getUser(), newMember.getServer());
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
