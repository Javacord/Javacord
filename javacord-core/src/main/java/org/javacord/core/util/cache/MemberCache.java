package org.javacord.core.util.cache;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import org.javacord.api.entity.member.Member;
import org.javacord.api.entity.server.Server;
import org.javacord.core.util.ImmutableToJavaMapper;
import java.util.Optional;
import java.util.Set;

/**
 * An immutable cache for all member entities.
 */
public class MemberCache {

    private static final String ID_INDEX_NAME = "id";
    private static final String SERVER_ID_INDEX_NAME = "server-id";
    private static final String ID_AND_SERVER_ID_INDEX_NAME = "server-id | type";

    private static final String MEMBER_SERVER_MEMBER_ID_INDEX_NAME = "ms > member-id";
    private static final String MEMBER_SERVER_MEMBER_ID_SERVER_ID_INDEX_NAME = "ms > member-id | server-id";

    private static final MemberCache EMPTY_CACHE = new MemberCache(
            Cache.<Member>empty()
                    .addIndex(ID_INDEX_NAME, Member::getId)
                    .addIndex(SERVER_ID_INDEX_NAME, member -> member.getServer().getId())
                    .addIndex(ID_AND_SERVER_ID_INDEX_NAME,
                            member -> Tuple.of(member.getId(), member.getServer().getId())),
            UserCache.empty(),
            Cache.<Tuple2<Member, Server>>empty()
                    .addIndex(MEMBER_SERVER_MEMBER_ID_INDEX_NAME, tuple -> tuple._1().getId())
                    .addIndex(MEMBER_SERVER_MEMBER_ID_SERVER_ID_INDEX_NAME,
                            tuple -> Tuple.of(tuple._1.getId(), tuple._2.getId()))
    );

    private final Cache<Tuple2<Member, Server>> memberServerCache;
    private final Cache<Member> cache;
    private final UserCache userCache;

    private MemberCache(Cache<Member> cache, UserCache userCache, Cache<Tuple2<Member, Server>> memberServerCache) {
        this.cache = cache;
        this.userCache = userCache;
        this.memberServerCache = memberServerCache;
    }

    /**
     * Gets an empty channel cache.
     *
     * @return An empty channel cache.
     */
    public static MemberCache empty() {
        return EMPTY_CACHE;
    }

    /**
     * Adds a member to the cache.
     *
     * <p>Automatically updates the underlying user cache, too.
     *
     * @param member The member to add.
     * @return The new member cache.
     */
    public MemberCache addMember(Member member) {
        return new MemberCache(
                cache.addElement(member),
                userCache.getUserById(member.getId())
                        .map(userCache::removeUser)
                        .orElse(userCache)
                        .addUser(member.getUser()),
                memberServerCache.addElement(Tuple.of(member, member.getServer()))
        );
    }

    /**
     * Removes a member from the cache.
     *
     * <p>Automatically updates the underlying user cache, too.
     *
     * @param member The member to remove.
     * @return The new member cache.
     */
    public MemberCache removeMember(Member member) {
        if (member == null) {
            return this;
        }
        Tuple2<Member, Server> memberServerTuple = memberServerCache
                .findAnyByIndex(
                        MEMBER_SERVER_MEMBER_ID_SERVER_ID_INDEX_NAME,
                        Tuple.of(member.getId(), member.getServer().getId())
                )
                .orElse(null);

        return new MemberCache(
                cache.removeElement(member),
                userCache.getUserById(member.getId())
                        .filter(user -> getMembersById(user.getId()).size() <= 1)
                        .map(userCache::removeUser)
                        .orElse(userCache),
                memberServerTuple == null ? memberServerCache : memberServerCache.removeElement(memberServerTuple)
        );
    }

    /**
     * Gets all servers that the user with the given id is a member of.
     *
     * @param userId The id of the user.
     * @return All servers that the user with the given id is a member of.
     */
    public Set<Server> getServers(long userId) {
        return ImmutableToJavaMapper.mapToJava(
                memberServerCache.findByIndex(MEMBER_SERVER_MEMBER_ID_INDEX_NAME, userId)
                        .map(tuple -> tuple._2)
        );
    }

    /**
     * Gets the underlying user cache.
     *
     * @return The underlying user cache.
     */
    public UserCache getUserCache() {
        return userCache;
    }

    /**
     * Gets all channels in the cache.
     *
     * @return All channels.
     */
    public Set<Member> getMembers() {
        return ImmutableToJavaMapper.mapToJava(cache.getAll());
    }

    /**
     * Get all members with the given id.
     *
     * @param id The id of the member.
     * @return All member with the given id.
     */
    public Set<Member> getMembersById(long id) {
        return ImmutableToJavaMapper.mapToJava(cache.findByIndex(ID_INDEX_NAME, id));
    }

    /**
     * Get all members in the server with the given id.
     *
     * @param serverId The server id.
     * @return All member of the server with the given id.
     */
    public Set<Member> getMembersByServer(long serverId) {
        return ImmutableToJavaMapper.mapToJava(cache.findByIndex(SERVER_ID_INDEX_NAME, serverId));
    }

    /**
     * Gets the member with the given id in the server with the given id.
     *
     * @param id The id of the member.
     * @param serverId The server id.
     * @return The member.
     */
    public Optional<Member> getMemberByIdAndServer(long id, long serverId) {
        return cache.findAnyByIndex(ID_AND_SERVER_ID_INDEX_NAME, Tuple.of(id, serverId));
    }
}
