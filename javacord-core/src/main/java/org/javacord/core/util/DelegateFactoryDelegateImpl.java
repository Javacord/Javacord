package org.javacord.core.util;

import org.javacord.api.DiscordApi;
import org.javacord.api.audio.internal.AudioSourceBaseDelegate;
import org.javacord.api.entity.channel.*;
import org.javacord.api.entity.channel.internal.*;
import org.javacord.api.entity.emoji.KnownCustomEmoji;
import org.javacord.api.entity.emoji.internal.CustomEmojiBuilderDelegate;
import org.javacord.api.entity.emoji.internal.CustomEmojiUpdaterDelegate;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.component.internal.*;
import org.javacord.api.entity.message.embed.internal.EmbedBuilderDelegate;
import org.javacord.api.entity.message.internal.InteractionMessageBuilderDelegate;
import org.javacord.api.entity.message.internal.MessageBuilderBaseDelegate;
import org.javacord.api.entity.message.internal.WebhookMessageBuilderDelegate;
import org.javacord.api.entity.message.mention.internal.AllowedMentionsBuilderDelegate;
import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.permission.internal.PermissionsBuilderDelegate;
import org.javacord.api.entity.permission.internal.RoleBuilderDelegate;
import org.javacord.api.entity.permission.internal.RoleUpdaterDelegate;
import org.javacord.api.entity.server.ScheduledEvent;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.server.internal.EventBuilderDelegate;
import org.javacord.api.entity.server.internal.EventUpdaterDelegate;
import org.javacord.api.entity.server.internal.ServerBuilderDelegate;
import org.javacord.api.entity.server.internal.ServerUpdaterDelegate;
import org.javacord.api.entity.server.invite.internal.InviteBuilderDelegate;
import org.javacord.api.entity.sticker.internal.StickerBuilderDelegate;
import org.javacord.api.entity.sticker.internal.StickerUpdaterDelegate;
import org.javacord.api.entity.webhook.Webhook;
import org.javacord.api.entity.webhook.internal.WebhookBuilderDelegate;
import org.javacord.api.entity.webhook.internal.WebhookUpdaterDelegate;
import org.javacord.api.interaction.internal.*;
import org.javacord.api.internal.AccountUpdaterDelegate;
import org.javacord.api.internal.DiscordApiBuilderDelegate;
import org.javacord.api.util.exception.DiscordExceptionValidator;
import org.javacord.api.util.internal.DelegateFactoryDelegate;
import org.javacord.api.util.logging.internal.ExceptionLoggerDelegate;
import org.javacord.core.AccountUpdaterDelegateImpl;
import org.javacord.core.DiscordApiBuilderDelegateImpl;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.audio.AudioSourceBaseDelegateImpl;
import org.javacord.core.entity.channel.*;
import org.javacord.core.entity.emoji.CustomEmojiBuilderDelegateImpl;
import org.javacord.core.entity.emoji.CustomEmojiUpdaterDelegateImpl;
import org.javacord.core.entity.message.InteractionMessageBuilderDelegateImpl;
import org.javacord.core.entity.message.MessageBuilderBaseDelegateImpl;
import org.javacord.core.entity.message.WebhookMessageBuilderDelegateImpl;
import org.javacord.core.entity.message.component.internal.*;
import org.javacord.core.entity.message.embed.EmbedBuilderDelegateImpl;
import org.javacord.core.entity.message.mention.AllowedMentionsBuilderDelegateImpl;
import org.javacord.core.entity.permission.PermissionsBuilderDelegateImpl;
import org.javacord.core.entity.permission.RoleBuilderDelegateImpl;
import org.javacord.core.entity.permission.RoleUpdaterDelegateImpl;
import org.javacord.core.entity.server.*;
import org.javacord.core.entity.server.invite.InviteBuilderDelegateImpl;
import org.javacord.core.entity.sticker.StickerBuilderDelegateImpl;
import org.javacord.core.entity.sticker.StickerUpdaterDelegateImpl;
import org.javacord.core.entity.webhook.WebhookBuilderDelegateImpl;
import org.javacord.core.entity.webhook.WebhookUpdaterDelegateImpl;
import org.javacord.core.interaction.*;
import org.javacord.core.util.exception.DiscordExceptionValidatorImpl;
import org.javacord.core.util.logging.ExceptionLoggerDelegateImpl;

/**
 * The implementation of {@link DelegateFactoryDelegate}.
 */
public class DelegateFactoryDelegateImpl implements DelegateFactoryDelegate {

    @Override
    public DiscordApiBuilderDelegate createDiscordApiBuilderDelegate() {
        return new DiscordApiBuilderDelegateImpl();
    }

    @Override
    public EmbedBuilderDelegate createEmbedBuilderDelegate() {
        return new EmbedBuilderDelegateImpl();
    }

    @Override
    public AllowedMentionsBuilderDelegate createAllowedMentionsBuilderDelegate() {
        return new AllowedMentionsBuilderDelegateImpl();
    }

    @Override
    public MessageBuilderBaseDelegate createMessageBuilderDelegate() {
        return new MessageBuilderBaseDelegateImpl();
    }

    @Override
    public InteractionMessageBuilderDelegate createInteractionMessageBuilderDelegate() {
        return new InteractionMessageBuilderDelegateImpl();
    }

    @Override
    public WebhookMessageBuilderDelegate createWebhookMessageBuilderDelegate() {
        return new WebhookMessageBuilderDelegateImpl();
    }

    @Override
    public PermissionsBuilderDelegate createPermissionsBuilderDelegate() {
        return new PermissionsBuilderDelegateImpl();
    }

    @Override
    public PermissionsBuilderDelegate createPermissionsBuilderDelegate(Permissions permissions) {
        return new PermissionsBuilderDelegateImpl(permissions);
    }

    @Override
    public ChannelCategoryBuilderDelegate createChannelCategoryBuilderDelegate(Server server) {
        return new ChannelCategoryBuilderDelegateImpl((ServerImpl) server);
    }

    @Override
    public ServerTextChannelBuilderDelegate createServerTextChannelBuilderDelegate(Server server) {
        return new ServerTextChannelBuilderDelegateImpl((ServerImpl) server);
    }

    @Override
    public ServerForumChannelBuilderDelegate createServerForumChannelBuilderDelegate(Server server) {
        return new ServerForumChannelBuilderDelegateImpl((ServerImpl) server);
    }

    @Override
    public ServerThreadChannelBuilderDelegate createServerThreadChannelBuilderDelegate(
            ServerTextChannel serverTextChannel) {
        return new ServerThreadChannelBuilderDelegateImpl(serverTextChannel);
    }

    @Override
    public ServerThreadChannelBuilderDelegate createServerThreadChannelBuilderDelegate(Message message) {
        return new ServerThreadChannelBuilderDelegateImpl(message);
    }

    @Override
    public ServerVoiceChannelBuilderDelegate createServerVoiceChannelBuilderDelegate(Server server) {
        return new ServerVoiceChannelBuilderDelegateImpl((ServerImpl) server);
    }

    @Override
    public CustomEmojiBuilderDelegate createCustomEmojiBuilderDelegate(Server server) {
        return new CustomEmojiBuilderDelegateImpl((ServerImpl) server);
    }

    @Override
    public WebhookBuilderDelegate createWebhookBuilderDelegate(ServerTextChannel channel) {
        return new WebhookBuilderDelegateImpl(channel);
    }

    @Override
    public ServerBuilderDelegate createServerBuilderDelegate(DiscordApi api) {
        return new ServerBuilderDelegateImpl((DiscordApiImpl) api);
    }

    @Override
    public RoleBuilderDelegate createRoleBuilderDelegate(Server server) {
        return new RoleBuilderDelegateImpl((ServerImpl) server);
    }

    @Override
    public InviteBuilderDelegate createInviteBuilderDelegate(ServerChannel channel) {
        return new InviteBuilderDelegateImpl(channel);
    }

    @Override
    public AccountUpdaterDelegate createAccountUpdaterDelegate(DiscordApi api) {
        return new AccountUpdaterDelegateImpl(((DiscordApiImpl) api));
    }

    @Override
    public SlashCommandUpdaterDelegate createSlashCommandUpdaterDelegate(long commandId) {
        return new SlashCommandUpdaterDelegateImpl(commandId);
    }

    @Override
    public UserContextMenuUpdaterDelegate createUserContextMenuUpdaterDelegate(long commandId) {
        return new UserContextMenuUpdaterDelegateImpl(commandId);
    }

    @Override
    public MessageContextMenuUpdaterDelegate createMessageContextMenuUpdaterDelegate(long commandId) {
        return new MessageContextMenuUpdaterDelegateImpl(commandId);
    }

    @Override
    public ServerChannelUpdaterDelegate createServerChannelUpdaterDelegate(ServerChannel channel) {
        return new ServerChannelUpdaterDelegateImpl(channel);
    }

    @Override
    public RegularServerChannelUpdaterDelegate createRegularServerChannelUpdaterDelegate(
            RegularServerChannel channel) {
        return new RegularServerChannelUpdaterDelegateImpl(channel);
    }

    @Override
    public ServerTextChannelUpdaterDelegate createServerTextChannelUpdaterDelegate(ServerTextChannel channel) {
        return new ServerTextChannelUpdaterDelegateImpl(channel);
    }

    @Override
    public ServerForumChannelUpdaterDelegate createServerForumChannelUpdaterDelegate(ServerForumChannel channel) {
        return new ServerForumChannelUpdaterDelegateImpl(channel);
    }

    @Override
    public ServerVoiceChannelUpdaterDelegate createServerVoiceChannelUpdaterDelegate(ServerVoiceChannel channel) {
        return new ServerVoiceChannelUpdaterDelegateImpl(channel);
    }

    @Override
    public ServerThreadChannelUpdaterDelegate createServerThreadChannelUpdaterDelegate(ServerThreadChannel thread) {
        return new ServerThreadChannelUpdaterDelegateImpl(thread);
    }

    @Override
    public CustomEmojiUpdaterDelegate createCustomEmojiUpdaterDelegate(KnownCustomEmoji emoji) {
        return new CustomEmojiUpdaterDelegateImpl(emoji);
    }

    @Override
    public RoleUpdaterDelegate createRoleUpdaterDelegate(Role role) {
        return new RoleUpdaterDelegateImpl(role);
    }

    @Override
    public ServerUpdaterDelegate createServerUpdaterDelegate(Server server) {
        return new ServerUpdaterDelegateImpl(server);
    }

    @Override
    public WebhookUpdaterDelegate createWebhookUpdaterDelegate(Webhook webhook) {
        return new WebhookUpdaterDelegateImpl(webhook);
    }

    @Override
    public AudioSourceBaseDelegate createAudioSourceBaseDelegate(DiscordApi api) {
        return new AudioSourceBaseDelegateImpl(api);
    }

    @Override
    public SlashCommandBuilderDelegate createSlashCommandBuilderDelegate() {
        return new SlashCommandBuilderDelegateImpl();
    }

    @Override
    public UserContextMenuBuilderDelegate createUserContextMenuBuilderDelegate() {
        return new UserContextMenuBuilderDelegateImpl();
    }

    @Override
    public MessageContextMenuBuilderDelegate createMessageContextMenuBuilderDelegate() {
        return new MessageContextMenuBuilderDelegateImpl();
    }

    @Override
    public SlashCommandOptionBuilderDelegate createSlashCommandOptionBuilderDelegate() {
        return new SlashCommandOptionBuilderDelegateImpl();
    }

    @Override
    public SlashCommandOptionChoiceBuilderDelegate createSlashCommandOptionChoiceBuilderDelegate() {
        return new SlashCommandOptionChoiceBuilderDelegateImpl();
    }

    @Override
    public ActionRowBuilderDelegate createActionRowBuilderDelegate() {
        return new ActionRowBuilderDelegateImpl();
    }

    @Override
    public ButtonBuilderDelegate createButtonBuilderDelegate() {
        return new ButtonBuilderDelegateImpl();
    }

    @Override
    public TextInputBuilderDelegate createTextInputBuilderDelegate() {
        return new TextInputBuilderDelegateImpl();
    }

    @Override
    public SelectMenuBuilderDelegate createSelectMenuBuilderDelegate() {
        return new SelectMenuBuilderDelegateImpl();
    }

    @Override
    public SelectMenuOptionBuilderDelegate createSelectMenuOptionBuilderDelegate() {
        return new SelectMenuOptionBuilderDelegateImpl();
    }

    @Override
    public StickerBuilderDelegate createStickerBuilderDelegate(Server server) {
        return new StickerBuilderDelegateImpl((ServerImpl) server);
    }

    @Override
    public StickerUpdaterDelegate createStickerUpdaterDelegate(Server server, long id) {
        return new StickerUpdaterDelegateImpl((ServerImpl) server, id);
    }

    @Override
    public EventBuilderDelegate createEventBuilderDelegate(Server server) {
        return new EventBuilderDelegateImpl((ServerImpl) server);
    }

    @Override
    public EventUpdaterDelegate createEventUpdaterDelegate(Server server, ScheduledEvent event) {
        return new EventUpdaterDelegateImpl((ServerImpl) server, event);
    }

    @Override
    public ExceptionLoggerDelegate createExceptionLoggerDelegate() {
        return new ExceptionLoggerDelegateImpl();
    }

    @Override
    public DiscordExceptionValidator createDiscordExceptionValidator() {
        return new DiscordExceptionValidatorImpl();
    }

}
