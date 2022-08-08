package org.javacord.core.entity.member;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.member.Member;
import org.javacord.api.entity.member.internal.MemberUpdaterDelegate;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * The implementation of {@link MemberUpdaterDelegate}.
 */
public class MemberUpdaterDelegateImpl implements MemberUpdaterDelegate {

    /**
     * The server to update.
     */
    private final Server server;

    /**
     * The reason for the update.
     */
    private String reason = null;

    /**
     * A map with all member roles to update.
     */
    private final Map<Member, Collection<Role>> memberRoles = new HashMap<>();

    /**
     * A map with all member nicknames to update.
     */
    private final Map<Member, String> memberNicknames = new HashMap<>();

    /**
     * A map with all member muted states to update.
     */
    private final Map<Member, Boolean> memberMuted = new HashMap<>();

    /**
     * A map with all member deafened states to update.
     */
    private final Map<Member, Boolean> memberDeafened = new HashMap<>();

    /**
     * A map with all channels to move member to.
     */
    private final Map<Member, ServerVoiceChannel> memberMoveTargets = new HashMap<>();

    /**
     * A map with all member timeouts to update.
     */
    private final Map<Member, Instant> memberTimeouts = new HashMap<>();

    /**
     * Creates a new server updater delegate.
     *
     * @param server The server to update.
     */
    public MemberUpdaterDelegateImpl(Server server) {
        this.server = server;
    }

    @Override
    public void setAuditLogReason(String reason) {
        this.reason = reason;
    }

    @Override
    public void setNickname(Member member, String nickname) {
        memberNicknames.put(member, nickname);
    }

    @Override
    public void setMemberTimeout(Member member, Instant timeout) {
        memberTimeouts.put(member, timeout);
    }

    @Override
    public void setMuted(Member member, boolean muted) {
        memberMuted.put(member, muted);
    }

    @Override
    public void setDeafened(Member member, boolean deafened) {
        memberDeafened.put(member, deafened);
    }

    @Override
    public void setVoiceChannel(Member member, ServerVoiceChannel channel) {
        memberMoveTargets.put(member, channel);
    }

    @Override
    public void addRoleToMember(Member member, Role role) {
        Collection<Role> memberRoles =
                this.memberRoles.computeIfAbsent(member, u -> new ArrayList<>(member.getRoles()));
        memberRoles.add(role);
    }

    @Override
    public void addRolesToMember(Member member, Collection<Role> roles) {
        Collection<Role> memberRoles =
                this.memberRoles.computeIfAbsent(member, u -> new ArrayList<>(member.getRoles()));
        memberRoles.addAll(roles);
    }

    @Override
    public void removeRoleFromMember(Member member, Role role) {
        Collection<Role> memberRoles =
                this.memberRoles.computeIfAbsent(member, u -> new ArrayList<>(member.getRoles()));
        memberRoles.remove(role);
    }

    @Override
    public void removeRolesFromMember(Member member, Collection<Role> roles) {
        Collection<Role> memberRoles =
                this.memberRoles.computeIfAbsent(member, u -> new ArrayList<>(member.getRoles()));
        memberRoles.removeAll(roles);
    }

    @Override
    public void removeAllRolesFromMember(Member member) {
        Collection<Role> memberRoles =
                this.memberRoles.computeIfAbsent(member, u -> new ArrayList<>(member.getRoles()));
        memberRoles.clear();
    }

    @Override
    public CompletableFuture<Void> update() {
        // All members that get updates
        HashSet<Member> members = new HashSet<>(memberRoles.keySet());
        members.addAll(memberNicknames.keySet());
        members.addAll(memberMuted.keySet());
        members.addAll(memberDeafened.keySet());
        members.addAll(memberMoveTargets.keySet());
        members.addAll(memberTimeouts.keySet());

        // All tasks
        List<CompletableFuture<?>> tasks = new ArrayList<>();

        members.forEach(member -> {
            boolean patchMember = false;
            ObjectNode updateNode = JsonNodeFactory.instance.objectNode();

            Collection<Role> roles = memberRoles.get(member);
            if (roles != null) {
                ArrayNode rolesJson = updateNode.putArray("roles");
                roles.stream()
                        .map(DiscordEntity::getIdAsString)
                        .forEach(rolesJson::add);
                patchMember = true;
            }

            if (memberNicknames.containsKey(member)) {
                String nickname = memberNicknames.get(member);
                if (member.getUser().isYourself()) {
                    tasks.add(
                            new RestRequest<Void>(server.getApi(), RestMethod.PATCH, RestEndpoint.OWN_NICKNAME)
                                    .setUrlParameters(server.getIdAsString())
                                    .setBody(JsonNodeFactory.instance.objectNode().put("nick", nickname))
                                    .setAuditLogReason(reason)
                                    .execute(result -> null));
                } else {
                    updateNode.put("nick", (nickname == null) ? "" : nickname);
                    patchMember = true;
                }
            }

            if (memberMuted.containsKey(member)) {
                updateNode.put("mute", memberMuted.get(member));
                patchMember = true;
            }

            if (memberDeafened.containsKey(member)) {
                updateNode.put("deaf", memberDeafened.get(member));
                patchMember = true;
            }

            if (memberMoveTargets.containsKey(member)) {
                ServerVoiceChannel channel = memberMoveTargets.get(member);
                if (member.getUser().isYourself()) {
                    ((DiscordApiImpl) server.getApi()).getWebSocketAdapter()
                            .sendVoiceStateUpdate(server, channel, null, null);
                } else if (channel != null) {
                    updateNode.put("channel_id", channel.getId());
                    patchMember = true;
                } else {
                    updateNode.putNull("channel_id");
                    patchMember = true;
                }
            }

            if (memberTimeouts.containsKey(member)) {
                updateNode.put("communication_disabled_until",
                        memberTimeouts.get(member).equals(Instant.MIN)
                                ? null
                                : DateTimeFormatter.ISO_LOCAL_DATE_TIME.withZone(ZoneId.from(ZoneOffset.UTC))
                                .format(memberTimeouts.get(member)));
                patchMember = true;
            }

            if (patchMember) {
                tasks.add(
                        new RestRequest<Void>(server.getApi(), RestMethod.PATCH, RestEndpoint.SERVER_MEMBER)
                                .setUrlParameters(server.getIdAsString(), member.getIdAsString())
                                .setBody(updateNode)
                                .setAuditLogReason(reason)
                                .execute(result -> null));
            }
        });


        CompletableFuture<?>[] tasksArray = tasks.toArray(new CompletableFuture<?>[tasks.size()]);
        return CompletableFuture.allOf(tasksArray);
    }

}
