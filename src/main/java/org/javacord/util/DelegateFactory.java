package org.javacord.util;

import org.javacord.DiscordApi;
import org.javacord.DiscordApiBuilderDelegate;
import org.javacord.entity.channel.ChannelCategoryBuilderDelegate;
import org.javacord.entity.channel.ServerTextChannel;
import org.javacord.entity.channel.ServerTextChannelBuilderDelegate;
import org.javacord.entity.channel.ServerVoiceChannelBuilderDelegate;
import org.javacord.entity.emoji.CustomEmojiBuilderDelegate;
import org.javacord.entity.message.MessageBuilderDelegate;
import org.javacord.entity.message.embed.EmbedBuilderDelegate;
import org.javacord.entity.permission.Permissions;
import org.javacord.entity.permission.PermissionsBuilderDelegate;
import org.javacord.entity.permission.RoleBuilderDelegate;
import org.javacord.entity.server.Server;
import org.javacord.entity.server.ServerBuilderDelegate;
import org.javacord.entity.webhook.WebhookBuilderDelegate;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * This class is used by Javacord internally.
 * You probably won't need it ever.
 */
public class DelegateFactory {

    /**
     * The factory builder delegate. It's used to create new factories.
     */
    private static final DelegateFactoryDelegate delegateFactoryDelegate;

    // Load it static, because it has a better performance to load it only once
    static {
        ServiceLoader<DelegateFactoryDelegate> delegateServiceLoader =
                ServiceLoader.load(DelegateFactoryDelegate.class);
        Iterator<DelegateFactoryDelegate> delegateIterator = delegateServiceLoader.iterator();
        if (delegateIterator.hasNext()) {
            delegateFactoryDelegate = delegateIterator.next();
            if (delegateIterator.hasNext()) {
                throw new IllegalStateException("Found more than one DelegateFactoryDelegate implementation!");
            }
        } else {
            throw new IllegalStateException("No DelegateFactoryDelegate implementation was found!");
        }
    }

    private DelegateFactory() {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates a new discord api builder delegate.
     *
     * @return A new discord api builder delegate.
     */
    public static DiscordApiBuilderDelegate createDiscordApiBuilderDelegate() {
        return delegateFactoryDelegate.createDiscordApiBuilderDelegate();
    }

    /**
     * Creates a new embed builder delegate.
     *
     * @return A new embed builder delegate.
     */
    public static EmbedBuilderDelegate createEmbedBuilderDelegate() {
        return delegateFactoryDelegate.createEmbedBuilderDelegate();
    }

    /**
     * Creates a new message builder delegate.
     *
     * @return A new message builder delegate.
     */
    public static MessageBuilderDelegate createMessageBuilderDelegate() {
        return delegateFactoryDelegate.createMessageBuilderDelegate();
    }

    /**
     * Creates a new permissions builder delegate.
     *
     * @return A new permissions builder delegate.
     */
    public static PermissionsBuilderDelegate createPermissionsBuilderDelegate() {
        return delegateFactoryDelegate.createPermissionsBuilderDelegate();
    }

    /**
     * Creates a new permissions builder delegate initialized with the given permissions.
     *
     * @param permissions The permissions which should be copied.
     * @return A new permissions builder delegate initialized with the given permissions.
     */
    public static PermissionsBuilderDelegate createPermissionsBuilderDelegate(Permissions permissions) {
        return delegateFactoryDelegate.createPermissionsBuilderDelegate(permissions);
    }

    /**
     * Creates a new channel category builder delegate.
     *
     * @param server The server of the channel category.
     * @return A new channel category builder delegate.
     */
    public static ChannelCategoryBuilderDelegate createChannelCategoryBuilderDelegate(Server server) {
        return delegateFactoryDelegate.createChannelCategoryBuilderDelegate(server);
    }

    /**
     * Creates a new server text channel builder delegate.
     *
     * @param server The server of the server text channel.
     * @return A new server text channel builder delegate.
     */
    public static ServerTextChannelBuilderDelegate createServerTextChannelBuilderDelegate(Server server) {
        return delegateFactoryDelegate.createServerTextChannelBuilderDelegate(server);
    }

    /**
     * Creates a new server voice channel builder delegate.
     *
     * @param server The server of the server voice channel.
     * @return A new server voice channel builder delegate.
     */
    public static ServerVoiceChannelBuilderDelegate createServerVoiceChannelBuilderDelegate(Server server) {
        return delegateFactoryDelegate.createServerVoiceChannelBuilderDelegate(server);
    }

    /**
     * Creates a new custom emoji builder delegate.
     *
     * @param server The server of the custom emoji.
     * @return A new custom emoji builder delegate.
     */
    public static CustomEmojiBuilderDelegate createCustomEmojiBuilderDelegate(Server server) {
        return delegateFactoryDelegate.createCustomEmojiBuilderDelegate(server);
    }

    /**
     * Creates a new webhook builder delegate.
     *
     * @param channel The server text channel of the webhook.
     * @return A new webhook builder delegate.
     */
    public static WebhookBuilderDelegate createWebhookBuilderDelegate(ServerTextChannel channel) {
        return delegateFactoryDelegate.createWebhookBuilderDelegate(channel);
    }

    /**
     * Creates a new server builder delegate.
     *
     * @param api The discord api instance.
     * @return A new server builder delegate.
     */
    public static ServerBuilderDelegate createServerBuilderDelegate(DiscordApi api) {
        return delegateFactoryDelegate.createServerBuilderDelegate(api);
    }

    /**
     * Creates a new role builder delegate.
     *
     * @param server The server for which the role should be created.
     * @return A new role builder delegate.
     */
    public static RoleBuilderDelegate createRoleBuilderDelegate(Server server) {
        return delegateFactoryDelegate.createRoleBuilderDelegate(server);
    }

}
