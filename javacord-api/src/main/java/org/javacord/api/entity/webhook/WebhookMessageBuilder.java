package org.javacord.api.entity.webhook;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;

import org.javacord.api.entity.Icon;
import org.javacord.api.entity.Mentionable;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.MessageDecoration;
import org.javacord.api.entity.message.embed.EmbedBuilder;

public class WebhookMessageBuilder extends MessageBuilder {

    /**
     * Default constructor.
     *
     * @throws IllegalStateException if the defined Webhook's token is not present.
     */
    public WebhookMessageBuilder(Webhook webhook) throws IllegalStateException {
        if (!webhook.getToken().isPresent())
            throw new IllegalStateException("Cannot use a WebHook without a defined token!");

        delegate.setWebhook(webhook);
    }

    /**
     * Sets the display name for the webhook in the sent message.
     *
     * @param displayName The display name to set.
     *
     * @return This instance.
     */
    public WebhookMessageBuilder setDisplayName(String displayName) {
        delegate.setDisplayName(displayName);
        return this;
    }

    /**
     * Sets the display avater for the webhook in the sent message.
     *
     * @param avatarUrl The avatar url to use.
     *
     * @return This instance.
     */
    public WebhookMessageBuilder setDisplayAvatar(URL avatarUrl) {
        delegate.setDisplayAvatar(avatarUrl);
        return this;
    }

    @Override
    public WebhookMessageBuilder appendCode(String language, String code) {
        return (WebhookMessageBuilder) super.appendCode(language, code);
    }

    @Override
    public WebhookMessageBuilder append(String message, MessageDecoration... decorations) {
        return (WebhookMessageBuilder) super.append(message, decorations);
    }

    @Override
    public WebhookMessageBuilder append(Mentionable entity) {
        return (WebhookMessageBuilder) super.append(entity);
    }

    @Override
    public WebhookMessageBuilder append(Object object) {
        return (WebhookMessageBuilder) super.append(object);
    }

    @Override
    public WebhookMessageBuilder appendNewLine() {
        return (WebhookMessageBuilder) super.appendNewLine();
    }

    @Override
    public WebhookMessageBuilder setContent(String content) {
        return (WebhookMessageBuilder) super.setContent(content);
    }

    @Override
    public WebhookMessageBuilder setEmbed(EmbedBuilder embed) {
        return (WebhookMessageBuilder) super.setEmbed(embed);
    }

    @Override
    public WebhookMessageBuilder setTts(boolean tts) {
        return (WebhookMessageBuilder) super.setTts(tts);
    }

    @Override
    public WebhookMessageBuilder addFile(BufferedImage image, String fileName) {
        return (WebhookMessageBuilder) super.addFile(image, fileName);
    }

    @Override
    public WebhookMessageBuilder addFile(File file) {
        return (WebhookMessageBuilder) super.addFile(file);
    }

    @Override
    public WebhookMessageBuilder addFile(Icon icon) {
        return (WebhookMessageBuilder) super.addFile(icon);
    }

    @Override
    public WebhookMessageBuilder addFile(URL url) {
        return (WebhookMessageBuilder) super.addFile(url);
    }

    @Override
    public WebhookMessageBuilder addFile(byte[] bytes, String fileName) {
        return (WebhookMessageBuilder) super.addFile(bytes, fileName);
    }

    @Override
    public WebhookMessageBuilder addFile(InputStream stream, String fileName) {
        return (WebhookMessageBuilder) super.addFile(stream, fileName);
    }

    @Override
    public WebhookMessageBuilder addFileAsSpoiler(BufferedImage image, String fileName) {
        return (WebhookMessageBuilder) super.addFileAsSpoiler(image, fileName);
    }

    @Override
    public WebhookMessageBuilder addFileAsSpoiler(File file) {
        return (WebhookMessageBuilder) super.addFileAsSpoiler(file);
    }

    @Override
    public WebhookMessageBuilder addFileAsSpoiler(Icon icon) {
        return (WebhookMessageBuilder) super.addFileAsSpoiler(icon);
    }

    @Override
    public WebhookMessageBuilder addFileAsSpoiler(URL url) {
        return (WebhookMessageBuilder) super.addFileAsSpoiler(url);
    }

    @Override
    public WebhookMessageBuilder addFileAsSpoiler(byte[] bytes, String fileName) {
        return (WebhookMessageBuilder) super.addFileAsSpoiler(bytes, fileName);
    }

    @Override
    public WebhookMessageBuilder addFileAsSpoiler(InputStream stream, String fileName) {
        return (WebhookMessageBuilder) super.addFileAsSpoiler(stream, fileName);
    }

    @Override
    public WebhookMessageBuilder addAttachment(BufferedImage image, String fileName) {
        return (WebhookMessageBuilder) super.addAttachment(image, fileName);
    }

    @Override
    public WebhookMessageBuilder addAttachment(File file) {
        return (WebhookMessageBuilder) super.addAttachment(file);
    }

    @Override
    public WebhookMessageBuilder addAttachment(Icon icon) {
        return (WebhookMessageBuilder) super.addAttachment(icon);
    }

    @Override
    public WebhookMessageBuilder addAttachment(URL url) {
        return (WebhookMessageBuilder) super.addAttachment(url);
    }

    @Override
    public WebhookMessageBuilder addAttachment(byte[] bytes, String fileName) {
        return (WebhookMessageBuilder) super.addAttachment(bytes, fileName);
    }

    @Override
    public WebhookMessageBuilder addAttachment(InputStream stream, String fileName) {
        return (WebhookMessageBuilder) super.addAttachment(stream, fileName);
    }

    @Override
    public WebhookMessageBuilder addAttachmentAsSpoiler(BufferedImage image, String fileName) {
        return (WebhookMessageBuilder) super.addAttachmentAsSpoiler(image, fileName);
    }

    @Override
    public WebhookMessageBuilder addAttachmentAsSpoiler(File file) {
        return (WebhookMessageBuilder) super.addAttachmentAsSpoiler(file);
    }

    @Override
    public WebhookMessageBuilder addAttachmentAsSpoiler(Icon icon) {
        return (WebhookMessageBuilder) super.addAttachmentAsSpoiler(icon);
    }

    @Override
    public WebhookMessageBuilder addAttachmentAsSpoiler(URL url) {
        return (WebhookMessageBuilder) super.addAttachmentAsSpoiler(url);
    }

    @Override
    public WebhookMessageBuilder addAttachmentAsSpoiler(byte[] bytes, String fileName) {
        return (WebhookMessageBuilder) super.addAttachmentAsSpoiler(bytes, fileName);
    }

    @Override
    public WebhookMessageBuilder addAttachmentAsSpoiler(InputStream stream, String fileName) {
        return (WebhookMessageBuilder) super.addAttachmentAsSpoiler(stream, fileName);
    }

    @Override
    public WebhookMessageBuilder setNonce(String nonce) {
        return (WebhookMessageBuilder) super.setNonce(nonce);
    }
}
