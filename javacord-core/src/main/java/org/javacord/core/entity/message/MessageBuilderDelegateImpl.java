package org.javacord.core.entity.message;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.Icon;
import org.javacord.api.entity.Mentionable;
import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageDecoration;
import org.javacord.api.entity.message.Messageable;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.message.internal.MessageBuilderDelegate;
import org.javacord.api.entity.user.User;
import org.javacord.api.entity.webhook.Webhook;
import org.javacord.api.entity.webhook.WebhookMessageBuilder;
import org.javacord.api.util.logging.ExceptionLogger;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.message.embed.EmbedBuilderDelegateImpl;
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
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * The implementation of {@link MessageBuilderDelegate}.
 */
public class MessageBuilderDelegateImpl implements MessageBuilderDelegate {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(MessageBuilderDelegateImpl.class);

    /**
     * The webhook sending the message.
     */
    private Webhook webhook = null;

    /**
     * The receiver of the message.
     */
    private Messageable messageable = null;

    /**
     * The string builder used to create the message.
     */
    private final StringBuilder strBuilder = new StringBuilder();

    /**
     * The embed of the message. Might be <code>null</code>.
     */
    private EmbedBuilder embed = null;

    /**
     * If the message should be text to speech or not.
     */
    private boolean tts = false;

    /**
     * The displayName of the webhook message.
     */
    private String displayName = null;

    /**
     * URL to the displayed avatar of the webhook message.
     */
    private URL avatarUrl;

    /**
     * The nonce of the message.
     */
    private String nonce = null;

    /**
     * A list with all attachments which should be added to the message.
     */
    private final List<FileContainer> attachments = new ArrayList<>();

    @Override
    public void setWebhook(Webhook webhook) {
        this.webhook = webhook;
    }

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
    public void setEmbed(EmbedBuilder embed) {
        this.embed = embed;
    }

    @Override
    public void setTts(boolean tts) {
        this.tts = tts;
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
    public void setNonce(String nonce) {
        this.nonce = nonce;
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
    public StringBuilder getStringBuilder() {
        return strBuilder;
    }

    @Override
    public CompletableFuture<Message> send(User user) {
        return send((Messageable) user);
    }

    @Override
    public CompletableFuture<Message> send(Messageable messageable) {
        if (messageable == null) {
            throw new IllegalStateException("Cannot send message without knowing the receiver");
        }
        if (messageable instanceof TextChannel) {
            return send((TextChannel) messageable);
        } else if (messageable instanceof User) {
            return ((User) messageable).openPrivateChannel().thenCompose(this::send);
        }
        throw new IllegalStateException("Messageable of unknown type");
    }

    @Override
    public CompletableFuture<Message> send(TextChannel channel) {
        if (webhook == null) {
            return sendAsUser(channel);
        } else {
            return sendAsWebhook(channel);
        }
    }

    public static void main(String[] args) {
        DiscordApi join = new DiscordApiBuilder()
                .setToken("NDc3NjEzMjYwMTkxODI1OTMw.XWe7HA.WfyObUqdeRbwQMgYQDyGOBOoJu0")
                .login()
                .join();

        ServerTextChannel channel = join.getChannelById(412663733064695832L)
                .flatMap(Channel::asServerTextChannel)
                .get();

        join.getServerById(412663733064695829L)
                .ifPresent(srv -> srv.getWebhooks()
                        .thenApply(list -> list.get(0))
                        .thenApply(WebhookMessageBuilder::new)
                        .thenCompose(msg -> msg.setContent("abc").setDisplayName("def").send(channel))
                        .thenAccept(System.out::println)
                        .exceptionally(ExceptionLogger.get())
                );
    }

    private CompletableFuture<Message> sendAsWebhook(TextChannel channel) {
        // cheap implementation
        // https://discordapp.com/developers/docs/resources/webhook#execute-webhook-jsonform-params

        String str = toString();

        ObjectNode data = JsonNodeFactory.instance.objectNode()
                .put("tts", this.tts);

        if (displayName != null) {
            data.put("username", displayName);
        }

        if (avatarUrl != null) {
            data.put("avatar_url", avatarUrl.toExternalForm());
        }

        if (embed != null && strBuilder.length() == 0) {
            ((EmbedBuilderDelegateImpl) embed.getDelegate()).toJsonNode(data.putObject("embed"));
        } else if (embed == null && strBuilder.length() != 0) {
            data.put("content", strBuilder.toString());
        } else {
            logger.warn("Currently, WebHook message implementation only supports " +
                    "one of textual content OR embed content.");
        }

        CompletableFuture<Message> future = new CompletableFuture<>();
        return new RestRequest<Message>(channel.getApi(), RestMethod.POST, RestEndpoint.WEBHOOK_SEND)
                .setUrlParameters(webhook.getIdAsString(), webhook.getToken().orElseThrow(AssertionError::new))
                .setBody(data)
                .execute(result -> channel.getMessagesAsStream()
                        // TODO someone with more time on their hand NEEDS to fix this
                        .filter(message -> message.getAuthor()
                        .asWebhook()
                        .map(futHook -> futHook.thenApply(webhook::equals))
                        .map(CompletableFuture::join)
                        .orElse(false))
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
    }

    private CompletableFuture<Message> sendAsUser(TextChannel channel) {
        ObjectNode body = JsonNodeFactory.instance.objectNode()
                .put("content", toString() == null ? "" : toString())
                .put("tts", tts);
        body.putArray("mentions");
        if (embed != null) {
            ((EmbedBuilderDelegateImpl) embed.getDelegate()).toJsonNode(body.putObject("embed"));
        }
        if (nonce != null) {
            body.put("nonce", nonce);
        }

        RestRequest<Message> request = new RestRequest<Message>(channel.getApi(), RestMethod.POST, RestEndpoint.MESSAGE)
                .setUrlParameters(channel.getIdAsString());
        if (!attachments.isEmpty() || (embed != null && embed.requiresAttachments())) {
            CompletableFuture<Message> future = new CompletableFuture<>();
            // We access files etc. so this should be async
            channel.getApi().getThreadPool().getExecutorService().submit(() -> {
                try {
                    MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("payload_json", body.toString());
                    List<FileContainer> tempAttachments = new ArrayList<>(attachments);
                    // Add the attachments required for the embed
                    if (embed != null) {
                        tempAttachments.addAll(
                                ((EmbedBuilderDelegateImpl) embed.getDelegate()).getRequiredAttachments());
                    }
                    Collections.reverse(tempAttachments);
                    for (int i = 0; i < tempAttachments.size(); i++) {
                        byte[] bytes = tempAttachments.get(i).asByteArray(channel.getApi()).join();

                        String mediaType = URLConnection
                                .guessContentTypeFromName(tempAttachments.get(i).getFileTypeOrName());
                        if (mediaType == null) {
                            mediaType = "application/octet-stream";
                        }
                        multipartBodyBuilder.addFormDataPart("file" + i, tempAttachments.get(i).getFileTypeOrName(),
                                RequestBody.create(MediaType.parse(mediaType), bytes));
                    }

                    request.setMultipartBody(multipartBodyBuilder.build());
                    request.execute(result -> ((DiscordApiImpl) channel.getApi())
                            .getOrCreateMessage(channel, result.getJsonBody()))
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
        } else {
            request.setBody(body);
            return request.execute(result -> ((DiscordApiImpl) channel.getApi())
                    .getOrCreateMessage(channel, result.getJsonBody()));
        }
    }

    @Override
    public String toString() {
        return strBuilder.toString();
    }

}
