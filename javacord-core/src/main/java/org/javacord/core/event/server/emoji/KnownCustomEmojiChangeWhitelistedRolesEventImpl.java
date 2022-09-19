package org.javacord.core.event.server.emoji;

import org.javacord.api.entity.emoji.KnownCustomEmoji;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.event.server.emoji.KnownCustomEmojiChangeWhitelistedRolesEvent;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

/**
 * The implementation of {@link KnownCustomEmojiChangeWhitelistedRolesEvent}.
 */
public class KnownCustomEmojiChangeWhitelistedRolesEventImpl extends KnownCustomEmojiEventImpl
        implements KnownCustomEmojiChangeWhitelistedRolesEvent {

    /**
     * The new whitelist.
     */
    private final Set<Role> newWhitelist;

    /**
     * The new whitelist.
     */
    private final Set<Role> oldWhitelist;

    /**
     * Creates a new custom emoji change whitelisted roles event.
     *
     * @param emoji The updated emoji.
     * @param newWhitelist The new whitelist of the custom emoji.
     * @param oldWhitelist The old whitelist of the custom emoji.
     */
    public KnownCustomEmojiChangeWhitelistedRolesEventImpl(KnownCustomEmoji emoji, Set<Role> newWhitelist,
                                                           Set<Role> oldWhitelist) {
        super(emoji);
        this.newWhitelist = newWhitelist;
        this.oldWhitelist = oldWhitelist;
    }

    @Override
    public Optional<Set<Role>> getOldWhitelistedRoles() {
        return oldWhitelist == null || oldWhitelist.isEmpty()
                ? Optional.empty()
                : Optional.of(Collections.unmodifiableSet(oldWhitelist));
    }

    @Override
    public Optional<Set<Role>> getNewWhitelistedRoles() {
        return newWhitelist == null || newWhitelist.isEmpty()
                ? Optional.empty()
                : Optional.of(Collections.unmodifiableSet(newWhitelist));
    }

}
