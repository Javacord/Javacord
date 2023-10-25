package org.javacord.api.entity.member;

import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.member.internal.MemberUpdaterDelegate;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.util.internal.DelegateFactory;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * This class can be used to update the settings of a server.
 */
public class MemberUpdater {

    /**
     * The server delegate used by this instance.
     */
    private final MemberUpdaterDelegate delegate;

    /**
     * Creates a new server updater.
     *
     * @param server The server to update.
     */
    public MemberUpdater(Server server) {
        delegate = DelegateFactory.createMemberUpdaterDelegate(server);
    }

    /**
     * Sets the reason for this update. This reason will be visible in the audit log entry(s).
     *
     * @param reason The reason for this update.
     * @return The current instance in order to chain call methods.
     */
    public MemberUpdater setAuditLogReason(String reason) {
        delegate.setAuditLogReason(reason);
        return this;
    }

    /**
     * Queues a member's nickname to be updated.
     *
     * @param member The member whose nickname should be updated.
     * @param nickname The new nickname of the member.
     * @return The current instance in order to chain call methods.
     */
    public MemberUpdater setNickname(Member member, String nickname) {
        delegate.setNickname(member, nickname);
        return this;
    }

    /**
     * Queues a member's timeout to be updated.
     *
     * @param member The member whose nickname should be updated.
     * @param timeout The new timeout of the member.
     * @return The current instance in order to chain call methods.
     */
    public MemberUpdater setMemberTimeout(Member member, Instant timeout) {
        delegate.setMemberTimeout(member, timeout);
        return this;
    }

    /**
     * Queues a member's timeout to be updated.
     *
     * @param member The member whose nickname should be updated.
     * @param duration The duration of the timeout.
     * @return The current instance in order to chain call methods.
     */
    public MemberUpdater setMemberTimeout(Member member, Duration duration) {
        return setMemberTimeout(member, Instant.now().plus(duration));
    }

    /**
     * Queues a member's timeout to be updated.
     *
     * @param member The member whose nickname should be updated.
     * @return The current instance in order to chain call methods.
     */
    public MemberUpdater removeMemberTimeout(Member member) {
        delegate.setMemberTimeout(member, Instant.MIN);
        return this;
    }

    /**
     * Queues a member's muted state to be updated.
     *
     * @param member The member whose muted state should be updated.
     * @param muted The new muted state of the member.
     * @return The current instance in order to chain call methods.
     */
    public MemberUpdater setMuted(Member member, boolean muted) {
        delegate.setMuted(member, muted);
        return this;
    }

    /**
     * Queues a member's deafened state to be updated.
     *
     * @param member The member whose deafened state should be updated.
     * @param deafened The new deafened state of the member.
     * @return The current instance in order to chain call methods.
     */
    public MemberUpdater setDeafened(Member member, boolean deafened) {
        delegate.setDeafened(member, deafened);
        return this;
    }

    /**
     * Queues a moving a member to a different voice channel.
     *
     * @param member The member who should be moved.
     * @param channel The new voice channel of the member.
     * @return The current instance in order to chain call methods.
     */
    public MemberUpdater setVoiceChannel(Member member, ServerVoiceChannel channel) {
        delegate.setVoiceChannel(member, channel);
        return this;
    }

    /**
     * Queues a role to be assigned to the member.
     *
     * @param member The server member the role should be added to.
     * @param role The role which should be added to the server member.
     * @return The current instance in order to chain call methods.
     */
    public MemberUpdater addRoleToMember(Member member, Role role) {
        delegate.addRoleToMember(member, role);
        return this;
    }

    /**
     * Queues a collection of roles to be assigned to the member.
     *
     * @param member The server member the role should be added to.
     * @param roles The roles which should be added to the server member.
     * @return The current instance in order to chain call methods.
     */
    public MemberUpdater addRolesToMember(Member member, Collection<Role> roles) {
        delegate.addRolesToMember(member, roles);
        return this;
    }

    /**
     * Queues a role to be removed from the member.
     *
     * @param member The server member the role should be removed from.
     * @param role The role which should be removed from the member.
     * @return The current instance in order to chain call methods.
     */
    public MemberUpdater removeRoleFromMember(Member member, Role role) {
        delegate.removeRoleFromMember(member, role);
        return this;
    }

    /**
     * Queues a collection of roles to be removed from the member.
     *
     * @param member The server member the roles should be removed from.
     * @param roles The roles which should be removed from the member.
     * @return The current instance in order to chain call methods.
     */
    public MemberUpdater removeRolesFromMember(Member member, Collection<Role> roles) {
        delegate.removeRolesFromMember(member, roles);
        return this;
    }

    /**
     * Queues all roles to be removed from the member.
     *
     * @param member The server member the roles should be removed from.
     * @return The current instance in order to chain call methods.
     */
    public MemberUpdater removeAllRolesFromMember(Member member) {
        delegate.removeAllRolesFromMember(member);
        return this;
    }

    /**
     * Performs the queued updates.
     *
     * @return A future to check if the update was successful.
     */
    public CompletableFuture<Void> update() {
        return delegate.update();
    }

}
