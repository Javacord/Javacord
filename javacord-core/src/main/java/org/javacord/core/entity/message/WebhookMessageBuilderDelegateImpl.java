package org.javacord.core.entity.message;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import org.apache.logging.log4j.Logger;
import org.javacord.api.entity.Icon;
import org.javacord.api.entity.Mentionable;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageDecoration;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.message.internal.MessageBuilderDelegate;
import org.javacord.api.entity.message.internal.WebhookMessageBuilderDelegate;
import org.javacord.api.entity.message.mention.AllowedMentions;
import org.javacord.api.entity.webhook.IncomingWebhook;
import org.javacord.core.entity.message.embed.EmbedBuilderDelegateImpl;
import org.javacord.core.entity.message.mention.AllowedMentionsImpl;
import org.javacord.core.util.FileContainer;
import org.javacord.core.util.logging.LoggerUtil;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * The implementation of {@link MessageBuilderDelegate}.
 */
public class WebhookMessageBuilderDelegateImpl implements WebhookMessageBuilderDelegate {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(WebhookMessageBuilderDelegateImpl.class);

    /**
     * The avatar the webhook should use.
     */
    private URL avatarUrl = null;

    /**
     * The display name the webhook should use.
     */
    private String displayName = null;

    /**
     * The string builder used to create the message.
     */
    private final StringBuilder strBuilder = new StringBuilder();

    /**
     * The list of embed's of the message.
     */
    private List<EmbedBuilder> embeds = new ArrayList<>();

    /**
     * If the message should be text to speech or not.
     */
    private boolean tts = false;

    /**
     * A list with all attachments which should be added to the message.
     */
    private final List<FileContainer> attachments = new ArrayList<>();

    /**
     * The MentionsBuilder used to control mention behavior.
     */
    private AllowedMentions allowedMentions = null;

    @Override
    public void appendCode(String language, String code) {
        strBuilder
                .append("\n")
                .append(MessageDecoration.CODE_LONG.getPrefix())
                .append(language)
                .append("\n")
                .append(code)
                .append(MessageDecoration.CODE_LONG.getSuffix());
    }

    @Override
    public void append(String message, MessageDecoration... decorations) {
        for (MessageDecoration decoration : decorations) {
            strBuilder.append(decoration.getPrefix());
        }
        strBuilder.append(message);
        for (int i = decorations.length - 1; i >= 0; i--) {
            strBuilder.append(decorations[i].getSuffix());
        }
    }

    @Override
    public void append(Mentionable entity) {
        strBuilder.append(entity.getMentionTag());
    }

    @Override
    public void append(Object object) {
        strBuilder.append(object);
    }

    @Override
    public void appendNewLine() {
        strBuilder.append("\n");
    }

    @Override
    public void setContent(String content) {
        strBuilder.setLength(0);
        strBuilder.append(content);
    }

    @Override
    public void addEmbed(EmbedBuilder embed) {
        embeds.add(embed);
    }

    @Override
    public void addEmbeds(EmbedBuilder[] embeds) {
        this.embeds.addAll(Arrays.asList(embeds));
    }

    @Override
    public void removeEmbed(EmbedBuilder embed) {
        embeds.remove(embed);
    }

    @Override
    public void removeEmbeds(EmbedBuilder[] embeds) {
        this.embeds.removeAll(Arrays.asList(embeds));
    }

    @Override
    public void setTts(boolean tts) {
        this.tts = tts;
    }

    @Override
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public void setDisplayAvatar(URL avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    @Override
    public void setDisplayAvatar(Icon avatar) {
        this.avatarUrl = avatar == null ? null : avatar.getUrl();
    }

    @Override
    public void addFile(BufferedImage image, String fileName) {
        addAttachment(image, fileName);
    }

    @Override
    public void addFile(File file) {
        addAttachment(file);
    }

    @Override
    public void addFile(Icon icon) {
        addAttachment(icon);
    }

    @Override
    public void addFile(URL url) {
        addAttachment(url);
    }

    @Override
    public void addFile(byte[] bytes, String fileName) {
        addAttachment(bytes, fileName);
    }

    @Override
    public void addFile(InputStream stream, String fileName) {
        addAttachment(stream, fileName);
    }

    @Override
    public void addFileAsSpoiler(File file) {
        addAttachmentAsSpoiler(file);
    }

    @Override
    public void addFileAsSpoiler(Icon icon) {
        addAttachmentAsSpoiler(icon);
    }

    @Override
    public void addFileAsSpoiler(URL url) {
        addAttachmentAsSpoiler(url);
    }

    @Override
    public void addAttachment(BufferedImage image, String fileName) {
        if (image == null || fileName == null) {
            throw new IllegalArgumentException("image and fileName cannot be null!");
        }
        attachments.add(new FileContainer(image, fileName));
    }

    @Override
    public void addAttachment(File file) {
        if (file == null) {
            throw new IllegalArgumentException("file cannot be null!");
        }
        attachments.add(new FileContainer(file));
    }

    @Override
    public void addAttachment(Icon icon) {
        if (icon == null) {
            throw new IllegalArgumentException("icon cannot be null!");
        }
        attachments.add(new FileContainer(icon));
    }

    @Override
    public void addAttachment(URL url) {
        if (url == null) {
            throw new IllegalArgumentException("url cannot be null!");
        }
        attachments.add(new FileContainer(url));
    }

    @Override
    public void addAttachment(byte[] bytes, String fileName) {
        if (bytes == null || fileName == null) {
            throw new IllegalArgumentException("bytes and fileName cannot be null!");
        }
        attachments.add(new FileContainer(bytes, fileName));
    }

    @Override
    public void addAttachment(InputStream stream, String fileName) {
        if (stream == null || fileName == null) {
            throw new IllegalArgumentException("stream and fileName cannot be null!");
        }
        attachments.add(new FileContainer(stream, fileName));
    }

    @Override
    public void addAttachmentAsSpoiler(File file) {
        if (file == null) {
            throw new IllegalArgumentException("file cannot be null!");
        }
        attachments.add(new FileContainer(file, true));
    }

    @Override
    public void addAttachmentAsSpoiler(Icon icon) {
        if (icon == null) {
            throw new IllegalArgumentException("icon cannot be null!");
        }
        attachments.add(new FileContainer(icon, true));
    }

    @Override
    public void addAttachmentAsSpoiler(URL url) {
        if (url == null) {
            throw new IllegalArgumentException("url cannot be null!");
        }
        attachments.add(new FileContainer(url, true));
    }

    @Override
    public void setAllowedMentions(AllowedMentions allowedMentions) {
        if (allowedMentions == null) {
            throw new IllegalArgumentException("mention cannot be null!");
        }
        this.allowedMentions = allowedMentions;
    }

    @Override
    public StringBuilder getStringBuilder() {
        return strBuilder;
    }

    @Override
    public CompletableFuture<Message> send(IncomingWebhook webhook) throws IllegalStateException {

        if (!webhook.getChannel().isPresent()) {
            throw new IllegalStateException("Cannot use a webhook without a channel!");
        }

        ObjectNode body = JsonNodeFactory.instance.objectNode()
                .put("tts", this.tts);

        if (allowedMentions != null) {
            ((AllowedMentionsImpl) allowedMentions).toJsonNode(body.putObject("allowed_mentions"));
        }

        if (displayName != null) {
            body.put("username", displayName);
        }

        if (avatarUrl != null) {
            body.put("avatar_url", avatarUrl.toExternalForm());
        }


        if (embeds.size() != 0) {
            ArrayNode embedsNode = JsonNodeFactory.instance.objectNode().arrayNode();
            for (int i = 0; i < embeds.size() && i < 10; i++) {
                embedsNode.add(((EmbedBuilderDelegateImpl) embeds.get(i).getDelegate()).toJsonNode());
            }
            body.set("embeds", embedsNode);
        } else if (strBuilder.length() != 0) {
            body.put("content", strBuilder.toString());
        }

        RestRequest<Message> request =
                new RestRequest<Message>(webhook.getApi(), RestMethod.POST, RestEndpoint.WEBHOOK_SEND)
                .setUrlParameters(webhook.getIdAsString(), webhook.getToken());
        CompletableFuture<Message> future = new CompletableFuture<>();

        webhook.getApi().getThreadPool().getExecutorService().submit(() -> {
            try {
                MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("payload_json", body.toString());
                List<FileContainer> tempAttachments = new ArrayList<>(attachments);
                // Add the attachments required for the embed
                for (int i = 0; i < embeds.size() && i < 10; i++) {
                    tempAttachments.addAll(
                            ((EmbedBuilderDelegateImpl) embeds.get(i).getDelegate()).getRequiredAttachments());
                }

                Collections.reverse(tempAttachments);
                for (int i = 0; i < tempAttachments.size(); i++) {
                    byte[] bytes = tempAttachments.get(i).asByteArray(webhook.getApi()).join();

                    String mediaType = URLConnection
                            .guessContentTypeFromName(tempAttachments.get(i).getFileTypeOrName());
                    if (mediaType == null) {
                        mediaType = "application/octet-stream";
                    }
                    multipartBodyBuilder.addFormDataPart("file" + i, tempAttachments.get(i).getFileTypeOrName(),
                            RequestBody.create(MediaType.parse(mediaType), bytes));
                }

                request.setMultipartBody(multipartBodyBuilder.build());
                request.execute(result -> webhook.getChannel().get().getMessagesAsStream()
                        .filter(message -> message.getAuthor().isWebhook()
                                && message.getAuthor().getId() == webhook.getId())
                        .findFirst()
                        .orElseThrow(AssertionError::new)
                )
                        .whenComplete((message, throwable) -> {
                            if (throwable != null) {
                                future.completeExceptionally(throwable);
                            } else {
                                future.complete(message);
                            }
                        });
            } catch (Throwable t) {
                future.completeExceptionally(t);
            }
        });
        return future;
    }

    @Override
    public String toString() {
        return strBuilder.toString();
    }

}
