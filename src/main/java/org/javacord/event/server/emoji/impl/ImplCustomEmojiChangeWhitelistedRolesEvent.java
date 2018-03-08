package org.javacord.event.server.emoji.impl;

import org.javacord.entity.emoji.KnownCustomEmoji;
import org.javacord.entity.permission.Role;
import org.javacord.event.server.emoji.CustomEmojiChangeWhitelistedRolesEvent;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

/**
 * The implementation of {@link CustomEmojiChangeWhitelistedRolesEvent}.
 */
public class ImplCustomEmojiChangeWhitelistedRolesEvent extends ImplCustomEmojiEvent
        implements CustomEmojiChangeWhitelistedRolesEvent {

    /**
     * The new whitelist.
     */
    private Collection<Role> newWhitelist;

    /**
     * The new whitelist.
     */
    private Collection<Role> oldWhitelist;

    /**
     * Creates a new custom emoji change whitelisted roles event.
     *
     * @param emoji The updated emoji.
     * @param newWhitelist The new whitelist of the custom emoji.
     * @param oldWhitelist The old whitelist of the custom emoji.
     */
    public ImplCustomEmojiChangeWhitelistedRolesEvent(KnownCustomEmoji emoji, Collection<Role> newWhitelist,
                                                      Collection<Role> oldWhitelist) {
        super(emoji);
        this.newWhitelist = newWhitelist;
        this.oldWhitelist = oldWhitelist;
    }

    @Override
    public Optional<Collection<Role>> getOldWhitelistedRoles() {
        return oldWhitelist == null || oldWhitelist.isEmpty() ?
                Optional.empty() :
                Optional.of(Collections.unmodifiableCollection(new HashSet<>(oldWhitelist)));
    }

    @Override
    public Optional<Collection<Role>> getNewWhitelistedRoles() {
        return newWhitelist == null || newWhitelist.isEmpty() ?
                Optional.empty() :
                Optional.of(Collections.unmodifiableCollection(new HashSet<>(newWhitelist)));
    }

}
