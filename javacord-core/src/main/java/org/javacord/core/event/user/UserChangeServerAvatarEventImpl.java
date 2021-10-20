package org.javacord.core.event.user;

import org.javacord.api.entity.Icon;
import org.javacord.api.event.user.UserChangeServerAvatarEvent;
import org.javacord.core.entity.user.Member;

import java.util.Optional;

/**
 * The implementation of {@link UserChangeServerAvatarEvent}.
 */
public class UserChangeServerAvatarEventImpl extends ServerUserEventImpl implements UserChangeServerAvatarEvent {

    private final Member newMember;
    private final Member oldMember;

    /**
     * Creates a new user change server avatar event.
     *
     * @param newMember The new member.
     * @param oldMember The old member.
     */
    public UserChangeServerAvatarEventImpl(Member newMember, Member oldMember) {
        super(oldMember.getUser(), oldMember.getServer());
        this.newMember = newMember;
        this.oldMember = oldMember;
    }

    @Override
    public Optional<Icon> getOldServerAvatar() {
        return oldMember.getServerAvatar();
    }

    @Override
    public Optional<Icon> getOldServerAvatar(int size) {
        return oldMember.getServerAvatar(size);
    }

    @Override
    public Optional<Icon> getNewServerAvatar() {
        return newMember.getServerAvatar();
    }

    @Override
    public Optional<Icon> getNewServerAvatar(int size) {
        return newMember.getServerAvatar(size);
    }
}
