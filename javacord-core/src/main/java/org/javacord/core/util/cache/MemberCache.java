package org.javacord.core.util.cache;

import io.vavr.Tuple;
import org.javacord.core.entity.user.Member;
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

    private static final MemberCache EMPTY_CACHE = new MemberCache(
            Cache.<Member>empty()
                    .addIndex(ID_INDEX_NAME, Member::getId)
                    .addIndex(SERVER_ID_INDEX_NAME, member -> member.getServer().getId())
                    .addIndex(ID_AND_SERVER_ID_INDEX_NAME,
                            member -> Tuple.of(member.getId(), member.getServer().getId())),
            UserCache.empty()
    );

    private final Cache<Member> cache;
    private final UserCache userCache;

    private MemberCache(Cache<Member> cache, UserCache userCache) {
        this.cache = cache;
        this.userCache = userCache;
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
                        .addUser(member.getUser())
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
        return new MemberCache(
                cache.removeElement(member),
                userCache.getUserById(member.getId())
                        .map(userCache::removeUser)
                        .orElse(userCache)
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
     * Gets a set with all channels in the cache.
     *
     * @return A set with all channels.
     */
    public Set<Member> getMembers() {
        return ImmutableToJavaMapper.mapToJava(cache.getAll());
    }

    /**
     * Get a set with all members with the given id.
     *
     * @param id The id of the member.
     * @return A set with all member with the given id.
     */
    public Set<Member> getMembersById(long id) {
        return ImmutableToJavaMapper.mapToJava(cache.findByIndex(ID_INDEX_NAME, id));
    }

    /**
     * Get a set with all members in the server with the given id.
     *
     * @param serverId The server id.
     * @return A set with all member of the server with the given id.
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
