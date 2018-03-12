package org.javacord.util;

import org.javacord.AccountUpdaterDelegate;
import org.javacord.DiscordApi;
import org.javacord.DiscordApiBuilderDelegate;
import org.javacord.entity.channel.ChannelCategoryBuilderDelegate;
import org.javacord.entity.channel.GroupChannel;
import org.javacord.entity.channel.GroupChannelUpdaterDelegate;
import org.javacord.entity.channel.ServerChannel;
import org.javacord.entity.channel.ServerChannelUpdaterDelegate;
import org.javacord.entity.channel.ServerTextChannel;
import org.javacord.entity.channel.ServerTextChannelBuilderDelegate;
import org.javacord.entity.channel.ServerTextChannelUpdaterDelegate;
import org.javacord.entity.channel.ServerVoiceChannel;
import org.javacord.entity.channel.ServerVoiceChannelBuilderDelegate;
import org.javacord.entity.channel.ServerVoiceChannelUpdaterDelegate;
import org.javacord.entity.emoji.CustomEmojiBuilderDelegate;
import org.javacord.entity.emoji.CustomEmojiUpdaterDelegate;
import org.javacord.entity.emoji.KnownCustomEmoji;
import org.javacord.entity.message.MessageBuilderDelegate;
import org.javacord.entity.message.embed.EmbedBuilderDelegate;
import org.javacord.entity.permission.Permissions;
import org.javacord.entity.permission.PermissionsBuilderDelegate;
import org.javacord.entity.permission.Role;
import org.javacord.entity.permission.RoleBuilderDelegate;
import org.javacord.entity.permission.RoleUpdaterDelegate;
import org.javacord.entity.server.Server;
import org.javacord.entity.server.ServerBuilderDelegate;
import org.javacord.entity.server.ServerUpdaterDelegate;
import org.javacord.entity.server.invite.InviteBuilderDelegate;
import org.javacord.entity.webhook.Webhook;
import org.javacord.entity.webhook.WebhookBuilderDelegate;
import org.javacord.entity.webhook.WebhookUpdaterDelegate;
import org.javacord.util.exception.DiscordExceptionValidator;
import org.javacord.util.logging.ExceptionLoggerDelegate;

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

    /**
     * Creates a new account updater delegate.
     *
     * @param api The discord api instance.
     * @return A new account updater delegate.
     */
    AccountUpdaterDelegate createAccountUpdaterDelegate(DiscordApi api);

    /**
     * Creates a new group channel updater delegate.
     *
     * @param channel The channel to update.
     * @return A new group channel updater delegate.
     */
    GroupChannelUpdaterDelegate createGroupChannelUpdaterDelegate(GroupChannel channel);

    /**
     * Creates a new server channel updater delegate.
     *
     * @param channel The channel to update.
     * @return A new server channel updater delegate.
     */
    ServerChannelUpdaterDelegate createServerChannelUpdaterDelegate(ServerChannel channel);

    /**
     * Creates a new server text channel updater delegate.
     *
     * @param channel The channel to update.
     * @return A new server text channel updater delegate.
     */
    ServerTextChannelUpdaterDelegate createServerTextChannelUpdaterDelegate(ServerTextChannel channel);

    /**
     * Creates a new server voice channel updater delegate.
     *
     * @param channel The channel to update.
     * @return A new server voice channel updater delegate.
     */
    ServerVoiceChannelUpdaterDelegate createServerVoiceChannelUpdaterDelegate(ServerVoiceChannel channel);

    /**
     * Creates a new custom emoji updater delegate.
     *
     * @param emoji The custom emoji to update.
     * @return A new custom emoji updater delegate.
     */
    CustomEmojiUpdaterDelegate createCustomEmojiUpdaterDelegate(KnownCustomEmoji emoji);

    /**
     * Creates a new role updater delegate.
     *
     * @param role The role to update.
     * @return A new role updater delegate.
     */
    RoleUpdaterDelegate createRoleUpdaterDelegate(Role role);

    /**
     * Creates a new server updater delegate.
     *
     * @param server The server to update.
     * @return A new server updater delegate.
     */
    ServerUpdaterDelegate createServerUpdaterDelegate(Server server);

    /**
     * Creates a new webhook updater delegate.
     *
     * @param webhook The webhook to update.
     * @return A new webhook updater delegate.
     */
    WebhookUpdaterDelegate createWebhookUpdaterDelegate(Webhook webhook);

    /**
     * Creates a new exception logger delegate.
     *
     * @return A new exception logger delegate.
     */
    ExceptionLoggerDelegate createExceptionLoggerDelegate();

    /**
     * Creates a new discord exception validator.
     *
     * @return A new discord exception validator.
     */
    DiscordExceptionValidator createDiscordExceptionValidator();

}
