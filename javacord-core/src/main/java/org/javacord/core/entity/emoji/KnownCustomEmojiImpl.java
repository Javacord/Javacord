package org.javacord.core.entity.emoji;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.emoji.KnownCustomEmoji;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.emoji.CustomEmojiAttachableListener;
import org.javacord.api.listener.server.emoji.CustomEmojiChangeNameListener;
import org.javacord.api.listener.server.emoji.CustomEmojiChangeWhitelistedRolesListener;
import org.javacord.api.listener.server.emoji.CustomEmojiDeleteListener;
import org.javacord.api.util.event.ListenerManager;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.util.ClassHelper;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * The implementation of {@link KnownCustomEmoji}.
 */
public class KnownCustomEmojiImpl extends CustomEmojiImpl implements KnownCustomEmoji {

    /**
     * The server of the emoji.
     */
    private final Server server;

    /**
     * A list with all whitelisted roles.
     * Might be <code>null</code>!
     */
    private Collection<Role> whitelist;

    /**
     * Whether this emoji must be wrapped in colons or not.
     */
    private final boolean requiresColons;

    /**
     * Whether this emojiis managed or not.
     */
    private final boolean managed;

    /**
     * Creates a new known custom emoji.
     *
     * @param api The discord api instance.
     * @param server The server of the emoji.
     * @param data The json data of the emoji.
     */
    public KnownCustomEmojiImpl(DiscordApiImpl api, Server server, JsonNode data) {
        super(api, data);
        this.server = server;
        if (data.hasNonNull("roles")) {
            whitelist = new HashSet<>();
            for (JsonNode roleIdJson : data.get("roles")) {
                server.getRoleById(roleIdJson.asLong()).ifPresent(whitelist::add);
            }
        }
        requiresColons = !data.hasNonNull("require_colons") || data.get("require_colons").asBoolean();
        managed = data.get("managed").asBoolean(false);
    }

    /**
     * Sets the name of the custom emoji.
     *
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the whitelist of the custom emoji.
     *
     * @param whitelist The whitelist to set.
     */
    public void setWhitelist(Collection<Role> whitelist) {
        this.whitelist = whitelist;
    }

    @Override
    public Server getServer() {
        return server;
    }

    @Override
    public CompletableFuture<Void> delete(String reason) {
        return new RestRequest<Void>(getApi(), RestMethod.DELETE, RestEndpoint.CUSTOM_EMOJI)
                .setUrlParameters(getServer().getIdAsString(), getIdAsString())
                .setAuditLogReason(reason)
                .execute(result -> null);
    }

    @Override
    public Optional<Collection<Role>> getWhitelistedRoles() {
        return whitelist == null || whitelist.isEmpty() ?
                Optional.empty() :
                Optional.of(Collections.unmodifiableCollection(new HashSet<>(whitelist)));
    }

    @Override
    public boolean requiresColons() {
        return requiresColons;
    }

    @Override
    public boolean isManaged() {
        return managed;
    }

    @Override
    public ListenerManager<CustomEmojiChangeNameListener> addCustomEmojiChangeNameListener(
            CustomEmojiChangeNameListener listener) {
        return ((DiscordApiImpl) getApi())
                .addObjectListener(KnownCustomEmoji.class, getId(), CustomEmojiChangeNameListener.class, listener);
    }

    @Override
    public List<CustomEmojiChangeNameListener> getCustomEmojiChangeNameListeners() {
        return ((DiscordApiImpl) getApi())
                .getObjectListeners(KnownCustomEmoji.class, getId(), CustomEmojiChangeNameListener.class);
    }

    @Override
    public ListenerManager<CustomEmojiChangeWhitelistedRolesListener> addCustomEmojiChangeWhitelistedRolesListener(
            CustomEmojiChangeWhitelistedRolesListener listener) {
        return ((DiscordApiImpl) getApi()).addObjectListener(
                KnownCustomEmoji.class, getId(), CustomEmojiChangeWhitelistedRolesListener.class, listener);
    }

    @Override
    public List<CustomEmojiChangeWhitelistedRolesListener> getCustomEmojiChangeWhitelistedRolesListeners() {
        return ((DiscordApiImpl) getApi())
                .getObjectListeners(KnownCustomEmoji.class, getId(), CustomEmojiChangeWhitelistedRolesListener.class);
    }

    @Override
    public ListenerManager<CustomEmojiDeleteListener> addCustomEmojiDeleteListener(
            CustomEmojiDeleteListener listener) {
        return ((DiscordApiImpl) getApi())
                .addObjectListener(KnownCustomEmoji.class, getId(), CustomEmojiDeleteListener.class, listener);
    }

    @Override
    public List<CustomEmojiDeleteListener> getCustomEmojiDeleteListeners() {
        return ((DiscordApiImpl) getApi())
                .getObjectListeners(KnownCustomEmoji.class, getId(), CustomEmojiDeleteListener.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends CustomEmojiAttachableListener & ObjectAttachableListener> Collection<ListenerManager<T>>
    addCustomEmojiAttachableListener(T listener) {
        return ClassHelper.getInterfacesAsStream(listener.getClass())
                .filter(CustomEmojiAttachableListener.class::isAssignableFrom)
                .filter(ObjectAttachableListener.class::isAssignableFrom)
                .map(listenerClass -> (Class<T>) listenerClass)
                .map(listenerClass -> ((DiscordApiImpl) getApi()).addObjectListener(KnownCustomEmoji.class, getId(),
                                                                                    listenerClass, listener))
                .collect(Collectors.toList());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends CustomEmojiAttachableListener & ObjectAttachableListener> void
    removeCustomEmojiAttachableListener(T listener) {
        ClassHelper.getInterfacesAsStream(listener.getClass())
                .filter(CustomEmojiAttachableListener.class::isAssignableFrom)
                .filter(ObjectAttachableListener.class::isAssignableFrom)
                .map(listenerClass -> (Class<T>) listenerClass)
                .forEach(listenerClass -> ((DiscordApiImpl) getApi()).removeObjectListener(KnownCustomEmoji.class, getId(),
                                                                                           listenerClass, listener));
    }

    @Override
    public <T extends CustomEmojiAttachableListener & ObjectAttachableListener> Map<T, List<Class<T>>>
    getCustomEmojiAttachableListeners() {
        return ((DiscordApiImpl) getApi()).getObjectListeners(KnownCustomEmoji.class, getId());
    }

    @Override
    public <T extends CustomEmojiAttachableListener & ObjectAttachableListener> void removeListener(
            Class<T> listenerClass, T listener) {
        ((DiscordApiImpl) getApi()).removeObjectListener(KnownCustomEmoji.class, getId(), listenerClass, listener);
    }

    @Override
    public String toString() {
        return String.format("KnownCustomEmoji (id: %s, name: %s, animated: %b, server: %s)",
                getIdAsString(), getName(), isAnimated(), getServer());
    }

}
