package org.javacord.core.event.server.member;

import org.javacord.api.entity.Icon;
import org.javacord.api.entity.member.Member;
import org.javacord.api.event.server.member.ServerMemberChangeServerAvatarEvent;
import java.util.Optional;

/**
 * The implementation of {@link ServerMemberChangeServerAvatarEvent}.
 */
public class ServerMemberChangeServerAvatarEventImpl extends ServerMemberEventImpl implements
        ServerMemberChangeServerAvatarEvent {

    private final Member newMember;
    private final Member oldMember;

    /**
     * Creates a new user change server avatar event.
     *
     * @param newMember The new member.
     * @param oldMember The old member.
     */
    public ServerMemberChangeServerAvatarEventImpl(Member newMember, Member oldMember) {
        super(newMember);
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
