package org.javacord.core.event.user;

import org.javacord.api.entity.member.Member;
import org.javacord.api.event.user.UserChangeDiscriminatorEvent;
import org.javacord.core.event.server.member.ServerMemberEventImpl;

/**
 * The implementation of {@link UserChangeDiscriminatorEvent}.
 */
public class UserChangeDiscriminatorEventImpl extends ServerMemberEventImpl implements UserChangeDiscriminatorEvent {

    /**
     * The new discriminator of the user.
     */
    private final String newDiscriminator;

    /**
     * The old discriminator of the user.
     */
    private final String oldDiscriminator;

    /**
     * Creates a new member change discriminator event.
     *
     * @param member The member of the event.
     * @param newDiscriminator The new discriminator of the member.
     * @param oldDiscriminator The old discriminator of the member.
     */
    public UserChangeDiscriminatorEventImpl(Member member, String newDiscriminator, String oldDiscriminator) {
        super(member);
        this.newDiscriminator = newDiscriminator;
        this.oldDiscriminator = oldDiscriminator;
    }

    @Override
    public String getNewDiscriminator() {
        return newDiscriminator;
    }

    @Override
    public String getOldDiscriminator() {
        return oldDiscriminator;
    }

}
