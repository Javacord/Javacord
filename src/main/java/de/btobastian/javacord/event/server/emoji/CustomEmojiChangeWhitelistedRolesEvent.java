package de.btobastian.javacord.event.server.emoji;

import de.btobastian.javacord.entity.emoji.KnownCustomEmoji;
import de.btobastian.javacord.entity.permission.Role;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

/**
 * A custom emoji change whitelisted roles event.
 */
public class CustomEmojiChangeWhitelistedRolesEvent extends CustomEmojiEvent {

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
    public CustomEmojiChangeWhitelistedRolesEvent(KnownCustomEmoji emoji, Collection<Role> newWhitelist,
                                                  Collection<Role> oldWhitelist) {
        super(emoji);
        this.newWhitelist = newWhitelist;
        this.oldWhitelist = oldWhitelist;
    }

    /**
     * Gets a list with the old whitelisted roles.
     *
     * @return A list with the old whitelisted roles.
     */
    public Optional<Collection<Role>> getOldWhitelistedRoles() {
        return oldWhitelist == null || oldWhitelist.isEmpty() ?
                Optional.empty() :
                Optional.of(Collections.unmodifiableCollection(new HashSet<>(oldWhitelist)));
    }

    /**
     * Gets a list with the new whitelisted roles.
     *
     * @return A list with the new whitelisted roles.
     */
    public Optional<Collection<Role>> getNewWhitelistedRoles() {
        return newWhitelist == null || newWhitelist.isEmpty() ?
                Optional.empty() :
                Optional.of(Collections.unmodifiableCollection(new HashSet<>(newWhitelist)));
    }

}
