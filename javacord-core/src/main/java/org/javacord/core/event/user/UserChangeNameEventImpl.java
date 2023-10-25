package org.javacord.core.event.user;

import org.javacord.api.entity.member.Member;
import org.javacord.api.event.user.UserChangeNameEvent;
import org.javacord.core.event.server.member.ServerMemberEventImpl;

/**
 * The implementation of {@link UserChangeNameEvent}.
 */
public class UserChangeNameEventImpl extends ServerMemberEventImpl implements UserChangeNameEvent {

    /**
     * The new name of the user.
     */
    private final String newName;

    /**
     * The old name of the user.
     */
    private final String oldName;

    /**
     * Creates a new user change name event.
     *
     * @param member The member of the event.
     * @param newName The new name of the user.
     * @param oldName The old name of the user.
     */
    public UserChangeNameEventImpl(Member member, String newName, String oldName) {
        super(member);
        this.newName = newName;
        this.oldName = oldName;
    }

    @Override
    public String getNewName() {
        return newName;
    }

    @Override
    public String getOldName() {
        return oldName;
    }

}
