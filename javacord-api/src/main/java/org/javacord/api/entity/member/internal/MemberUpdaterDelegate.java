package org.javacord.api.entity.member.internal;

import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.member.Member;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.ServerUpdater;
import java.time.Instant;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * This class is internally used by the {@link ServerUpdater} to update servers.
 * You usually don't want to interact with this object.
 */
public interface MemberUpdaterDelegate {

    /**
     * Sets the reason for this update. This reason will be visible in the audit log entry(s).
     *
     * @param reason The reason for this update.
     */
    void setAuditLogReason(String reason);

    /**
     * Queues a member's nickname to be updated.
     *
     * @param member The member whose nickname should be updated.
     * @param nickname The new nickname of the member.
     */
    void setNickname(Member member, String nickname);

    /**
     * Queues a member's timeout to be updated.
     *
     * @param member The member whose nickname should be updated.
     * @param timeout The new timeout of the member.
     */
    void setMemberTimeout(Member member, Instant timeout);

    /**
     * Queues a member's muted state to be updated.
     *
     * @param member The member whose muted state should be updated.
     * @param muted The new muted state of the member.
     */
    void setMuted(Member member, boolean muted);

    /**
     * Queues a member's deafened state to be updated.
     *
     * @param member The member whose deafened state should be updated.
     * @param deafened The new deafened state of the member.
     */
    void setDeafened(Member member, boolean deafened);

    /**
     * Queues a moving a member to a different voice channel.
     *
     * @param member The member who should be moved.
     * @param channel The new voice channel of the member.
     */
    void setVoiceChannel(Member member, ServerVoiceChannel channel);

    /**
     * Queues a role to be assigned to the member.
     *
     * @param member The member to whom the role should be assigned.
     * @param role The role to be assigned.
     */
    void addRoleToMember(Member member, Role role);

    /**
     * Queues a collection of roles to be assigned to the member.
     *
     * @param member The member to whom the roles should be assigned.
     * @param roles The collection of roles to be assigned.
     */
    void addRolesToMember(Member member, Collection<Role> roles);

    /**
     * Queues a role to be removed from the member.
     *
     * @param member The member who should lose the role.
     * @param role The role to be removed.
     */
    void removeRoleFromMember(Member member, Role role);

    /**
     * Queues a collection of roles to be removed from the member.
     *
     * @param member The member who should lose the roles.
     * @param roles The collection of roles to be removed.
     */
    void removeRolesFromMember(Member member, Collection<Role> roles);

    /**
     * Queues all roles to be removed from the member.
     *
     * @param member The member who should lose the roles.
     */
    void removeAllRolesFromMember(Member member);

    /**
     * Performs the queued updates.
     *
     * @return A future to check if the update was successful.
     */
    CompletableFuture<Void> update();

}
