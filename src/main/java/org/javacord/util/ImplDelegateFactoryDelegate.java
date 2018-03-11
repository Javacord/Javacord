package org.javacord.util;

import org.javacord.AccountUpdaterDelegate;
import org.javacord.DiscordApi;
import org.javacord.DiscordApiBuilderDelegate;
import org.javacord.ImplAccountUpdaterDelegate;
import org.javacord.ImplDiscordApi;
import org.javacord.ImplDiscordApiBuilderDelegate;
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
import org.javacord.entity.channel.impl.ImplChannelCategoryBuilderDelegate;
import org.javacord.entity.channel.impl.ImplGroupChannelUpdaterDelegate;
import org.javacord.entity.channel.impl.ImplServerChannelUpdaterDelegate;
import org.javacord.entity.channel.impl.ImplServerTextChannelBuilderDelegate;
import org.javacord.entity.channel.impl.ImplServerTextChannelUpdaterDelegate;
import org.javacord.entity.channel.impl.ImplServerVoiceChannelBuilderDelegate;
import org.javacord.entity.channel.impl.ImplServerVoiceChannelUpdaterDelegate;
import org.javacord.entity.emoji.CustomEmojiBuilderDelegate;
import org.javacord.entity.emoji.CustomEmojiUpdaterDelegate;
import org.javacord.entity.emoji.KnownCustomEmoji;
import org.javacord.entity.emoji.impl.ImplCustomEmojiBuilderDelegate;
import org.javacord.entity.emoji.impl.ImplCustomEmojiUpdaterDelegate;
import org.javacord.entity.message.MessageBuilderDelegate;
import org.javacord.entity.message.embed.EmbedBuilderDelegate;
import org.javacord.entity.message.embed.impl.ImplEmbedBuilderDelegate;
import org.javacord.entity.message.impl.ImplMessageBuilderDelegate;
import org.javacord.entity.permission.Permissions;
import org.javacord.entity.permission.PermissionsBuilderDelegate;
import org.javacord.entity.permission.RoleBuilderDelegate;
import org.javacord.entity.permission.impl.ImplPermissionsBuilderDelegate;
import org.javacord.entity.permission.impl.ImplRoleBuilderDelegate;
import org.javacord.entity.server.Server;
import org.javacord.entity.server.ServerBuilderDelegate;
import org.javacord.entity.server.impl.ImplServer;
import org.javacord.entity.server.impl.ImplServerBuilderDelegate;
import org.javacord.entity.server.invite.InviteBuilderDelegate;
import org.javacord.entity.server.invite.impl.ImplInviteBuilderDelegate;
import org.javacord.entity.webhook.WebhookBuilderDelegate;
import org.javacord.entity.webhook.impl.ImplWebhookBuilderDelegate;
import org.javacord.util.exception.DiscordExceptionValidator;
import org.javacord.util.exception.impl.ImplDiscordExceptionValidator;

/**
 * The implementation of {@link DelegateFactoryDelegate}.
 */
public class ImplDelegateFactoryDelegate implements DelegateFactoryDelegate {

    @Override
    public DiscordApiBuilderDelegate createDiscordApiBuilderDelegate() {
        return new ImplDiscordApiBuilderDelegate();
    }

    @Override
    public EmbedBuilderDelegate createEmbedBuilderDelegate() {
        return new ImplEmbedBuilderDelegate();
    }

    @Override
    public MessageBuilderDelegate createMessageBuilderDelegate() {
        return new ImplMessageBuilderDelegate();
    }

    @Override
    public PermissionsBuilderDelegate createPermissionsBuilderDelegate() {
        return new ImplPermissionsBuilderDelegate();
    }

    @Override
    public PermissionsBuilderDelegate createPermissionsBuilderDelegate(Permissions permissions) {
        return new ImplPermissionsBuilderDelegate(permissions);
    }

    @Override
    public ChannelCategoryBuilderDelegate createChannelCategoryBuilderDelegate(Server server) {
        return new ImplChannelCategoryBuilderDelegate((ImplServer) server);
    }

    @Override
    public ServerTextChannelBuilderDelegate createServerTextChannelBuilderDelegate(Server server) {
        return new ImplServerTextChannelBuilderDelegate((ImplServer) server);
    }

    @Override
    public ServerVoiceChannelBuilderDelegate createServerVoiceChannelBuilderDelegate(Server server) {
        return new ImplServerVoiceChannelBuilderDelegate((ImplServer) server);
    }

    @Override
    public CustomEmojiBuilderDelegate createCustomEmojiBuilderDelegate(Server server) {
        return new ImplCustomEmojiBuilderDelegate((ImplServer) server);
    }

    @Override
    public WebhookBuilderDelegate createWebhookBuilderDelegate(ServerTextChannel channel) {
        return new ImplWebhookBuilderDelegate(channel);
    }

    @Override
    public ServerBuilderDelegate createServerBuilderDelegate(DiscordApi api) {
        return new ImplServerBuilderDelegate((ImplDiscordApi) api);
    }

    @Override
    public RoleBuilderDelegate createRoleBuilderDelegate(Server server) {
        return new ImplRoleBuilderDelegate((ImplServer) server);
    }

    @Override
    public InviteBuilderDelegate createInviteBuilderDelegate(ServerChannel channel) {
        return new ImplInviteBuilderDelegate(channel);
    }

    @Override
    public AccountUpdaterDelegate createAccountUpdaterDelegate(DiscordApi api) {
        return new ImplAccountUpdaterDelegate(((ImplDiscordApi) api));
    }

    @Override
    public GroupChannelUpdaterDelegate createGroupChannelUpdaterDelegate(GroupChannel channel) {
        return new ImplGroupChannelUpdaterDelegate(channel);
    }

    @Override
    public ServerChannelUpdaterDelegate createServerChannelUpdaterDelegate(ServerChannel channel) {
        return new ImplServerChannelUpdaterDelegate(channel);
    }

    @Override
    public ServerTextChannelUpdaterDelegate createServerTextChannelUpdaterDelegate(ServerTextChannel channel) {
        return new ImplServerTextChannelUpdaterDelegate(channel);
    }

    @Override
    public ServerVoiceChannelUpdaterDelegate createServerVoiceChannelUpdaterDelegate(ServerVoiceChannel channel) {
        return new ImplServerVoiceChannelUpdaterDelegate(channel);
    }

    @Override
    public CustomEmojiUpdaterDelegate createCustomEmojiUpdaterDelegate(KnownCustomEmoji emoji) {
        return new ImplCustomEmojiUpdaterDelegate(emoji);
    }

    @Override
    public DiscordExceptionValidator createDiscordExceptionValidator() {
        return new ImplDiscordExceptionValidator();
    }

}
