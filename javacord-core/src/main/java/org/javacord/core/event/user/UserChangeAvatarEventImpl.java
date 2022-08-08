package org.javacord.core.event.user;

import org.javacord.api.entity.Icon;
import org.javacord.api.entity.member.Member;
import org.javacord.api.event.user.UserChangeAvatarEvent;
import org.javacord.core.entity.user.UserImpl;
import org.javacord.core.event.server.member.ServerMemberEventImpl;

/**
 * The implementation of {@link UserChangeAvatarEvent}.
 */
public class UserChangeAvatarEventImpl extends ServerMemberEventImpl implements UserChangeAvatarEvent {

    /**
     * The new avatar hash of the user.
     */
    private final String newAvatarHash;

    /**
     * The old avatar hash of the user.
     */
    private final String oldAvatarHash;

    /**
     * Creates a new member change avatar event.
     *
     * @param member The member of the event.
     * @param newAvatarHash The new avatar hash of the member.
     * @param oldAvatarHash The old avatar hash of the member.
     */
    public UserChangeAvatarEventImpl(Member member, String newAvatarHash, String oldAvatarHash) {
        super(member);
        this.newAvatarHash = newAvatarHash;
        this.oldAvatarHash = oldAvatarHash;
    }

    @Override
    public Icon getNewAvatar() {
        return UserImpl.getAvatar(api, newAvatarHash, getMember().getUser().getDiscriminator(), getMember().getId());
    }

    @Override
    public boolean newAvatarIsDefaultAvatar() {
        return newAvatarHash == null;
    }

    @Override
    public Icon getOldAvatar() {
        return UserImpl.getAvatar(api, oldAvatarHash, getMember().getUser().getDiscriminator(), getMember().getId());
    }

    @Override
    public boolean oldAvatarIsDefaultAvatar() {
        return oldAvatarHash == null;
    }

}
