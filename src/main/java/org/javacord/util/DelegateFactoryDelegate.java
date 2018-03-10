package org.javacord.util;

import org.javacord.DiscordApi;
import org.javacord.DiscordApiBuilderDelegate;
import org.javacord.entity.channel.ChannelCategoryBuilderDelegate;
import org.javacord.entity.channel.ServerChannel;
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
import org.javacord.entity.server.invite.InviteBuilderDelegate;
import org.javacord.entity.webhook.WebhookBuilderDelegate;

/**
 * This class is internally used by the {@link DelegateFactory} to create delegate instances.
 * You usually don't want to interact with this object.
 */
public interface DelegateFactoryDelegate {

    /**
     * Creates a new discord api builder delegate.
     *
     * @return A new discord api builder delegate.
     */
    DiscordApiBuilderDelegate createDiscordApiBuilderDelegate();

    /**
     * Creates a new embed builder delegate.
     *
     * @return A new embed builder delegate.
     */
    EmbedBuilderDelegate createEmbedBuilderDelegate();

    /**
     * Creates a new message builder delegate.
     *
     * @return A new message builder delegate.
     */
    MessageBuilderDelegate createMessageBuilderDelegate();

    /**
     * Creates a new permissions builder delegate.
     *
     * @return A new permissions builder delegate.
     */
    PermissionsBuilderDelegate createPermissionsBuilderDelegate();

    /**
     * Creates a new permissions builder delegate initialized with the given permissions.
     *
     * @param permissions The permissions which should be copied.
     * @return A new permissions builder delegate initialized with the given permissions.
     */
    PermissionsBuilderDelegate createPermissionsBuilderDelegate(Permissions permissions);

    /**
     * Creates a new channel category builder delegate.
     *
     * @param server The server of the channel category.
     * @return A new channel category builder delegate.
     */
    ChannelCategoryBuilderDelegate createChannelCategoryBuilderDelegate(Server server);

    /**
     * Creates a new server text channel builder delegate.
     *
     * @param server The server of the server text channel.
     * @return A new server text channel builder delegate.
     */
    ServerTextChannelBuilderDelegate createServerTextChannelBuilderDelegate(Server server);

    /**
     * Creates a new server voice channel builder delegate.
     *
     * @param server The server of the server voice channel.
     * @return A new server voice channel builder delegate.
     */
    ServerVoiceChannelBuilderDelegate createServerVoiceChannelBuilderDelegate(Server server);

    /**
     * Creates a new custom emoji builder delegate.
     *
     * @param server The server of the custom emoji.
     * @return A new custom emoji builder delegate.
     */
    CustomEmojiBuilderDelegate createCustomEmojiBuilderDelegate(Server server);

    /**
     * Creates a new webhook builder delegate.
     *
     * @param channel The server text channel of the webhook.
     * @return A new webhook builder delegate.
     */
    WebhookBuilderDelegate createWebhookBuilderDelegate(ServerTextChannel channel);

    /**
     * Creates a new server builder delegate.
     *
     * @param api The discord api instance.
     * @return A new server builder delegate.
     */
    ServerBuilderDelegate createServerBuilderDelegate(DiscordApi api);

    /**
     * Creates a new role builder delegate.
     *
     * @param server The server for which the role should be created.
     * @return A new role builder delegate.
     */
    RoleBuilderDelegate createRoleBuilderDelegate(Server server);

    /**
     * Creates a new invite builder delegate.
     *
     * @param channel The channel for the invite.
     * @return A new invite builder delegate.
     */
    InviteBuilderDelegate createInviteBuilderDelegate(ServerChannel channel);

}
