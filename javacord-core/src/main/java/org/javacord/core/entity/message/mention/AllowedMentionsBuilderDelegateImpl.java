package org.javacord.core.entity.message.mention;

import org.javacord.api.entity.message.mention.AllowedMentions;
import org.javacord.api.entity.message.mention.internal.AllowedMentionsBuilderDelegate;

import java.util.ArrayList;
import java.util.Collection;

/**
 * The implementation of {@link AllowedMentionsBuilderDelegate}.
 */
public class AllowedMentionsBuilderDelegateImpl implements AllowedMentionsBuilderDelegate {

    // Lists
    private final ArrayList<Long> allowedUserMentions = new ArrayList<>();

    private final ArrayList<Long> allowedRoleMentions = new ArrayList<>();

    // General Mentions
    private boolean mentionAllRoles = false;

    private boolean mentionAllUsers = false;

    private boolean mentionEveryoneAndHere = false;


    @Override
    public void setMentionEveryoneAndHere(boolean value) {
        this.mentionEveryoneAndHere = value;
    }

    @Override
    public void setMentionRoles(boolean value) {
        this.mentionAllRoles = value;
    }

    @Override
    public void setMentionUsers(boolean value) {
        this.mentionAllUsers = value;
    }

    @Override
    public void addUser(long userId) {
        allowedUserMentions.add(userId);
    }

    @Override
    public void addUser(String userId) {
        addUser(Long.parseLong(userId));
    }

    @Override
    public void addUsers(Collection<Long> userIds) {
        allowedUserMentions.addAll(userIds);
    }

    @Override
    public void addRole(long roleId) {
        allowedRoleMentions.add(roleId);
    }

    @Override
    public void addRole(String roleId) {
        addRole(Long.parseLong(roleId));
    }

    @Override
    public void addRoles(Collection<Long> roleIds) {
        allowedRoleMentions.addAll(roleIds);
    }

    @Override
    public void removeUser(long userId) {
        allowedUserMentions.remove(userId);
    }

    @Override
    public void removeUser(String userId) {
        removeUser(Long.parseLong(userId));
    }

    @Override
    public void removeRole(long roleId) {
        allowedRoleMentions.remove(roleId);
    }

    @Override
    public void removeRole(String roleId) {
        removeRole(Long.parseLong(roleId));
    }

    @Override
    public void removeUsers(Collection<Long> userIds) {
        allowedUserMentions.removeAll(userIds);
    }

    @Override
    public void removeRoles(Collection<Long> roleIds) {
        allowedRoleMentions.removeAll(roleIds);
    }

    @Override
    public AllowedMentions build() {
        return new AllowedMentionsImpl(mentionAllRoles, mentionAllUsers, mentionEveryoneAndHere,
                allowedRoleMentions, allowedUserMentions);
    }
}