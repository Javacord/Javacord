package org.javacord.core.entity.message;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.Attachment;
import org.javacord.api.entity.Icon;
import org.javacord.api.entity.Mentionable;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAttachment;
import org.javacord.api.entity.message.MessageDecoration;
import org.javacord.api.entity.message.Messageable;
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.ActionRowBuilder;
import org.javacord.api.entity.message.component.ComponentType;
import org.javacord.api.entity.message.component.HighLevelComponent;
import org.javacord.api.entity.message.component.LowLevelComponent;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.message.internal.MessageBuilderBaseDelegate;
import org.javacord.api.entity.message.mention.AllowedMentions;
import org.javacord.api.entity.user.User;
import org.javacord.api.entity.webhook.IncomingWebhook;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.AttachmentImpl;
import org.javacord.core.entity.message.component.ComponentImpl;
import org.javacord.core.entity.message.embed.EmbedBuilderDelegateImpl;
import org.javacord.core.entity.message.mention.AllowedMentionsImpl;
import org.javacord.core.entity.user.Member;
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
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * The implementation of {@link MessageBuilderBaseDelegate}.
 */
public class MessageBuilderBaseDelegateImpl implements MessageBuilderBaseDelegate {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(MessageBuilderBaseDelegateImpl.class);

    /**
     * The string builder used to create the message.
     */
    protected final StringBuilder strBuilder = new StringBuilder();

    /**
     * True if the content has been changed by the user.
     */
    protected boolean contentChanged = false;

    /**
     * The list of embeds of the message.
     */
    protected List<EmbedBuilder> embeds = new ArrayList<>();

    /**
     * True if embeds have been changed by the user.
     */
    protected boolean embedsChanged = false;

    /**
     * If the message should be text to speech or not.
     */
    protected boolean tts = false;

    /**
     * The nonce of the message.
     */
    protected String nonce = null;

    /**
     * All attachments which should be added to the message.
     */
    protected final List<FileContainer> newAttachments = new ArrayList<>();

    /**
     * Used to indicate that all the attachments in the message should be removed.
     */
    protected boolean removeAllAttachments = false;

    /**
     * All the attachments that should be removed from the message.
     */
    protected final List<Attachment> attachmentsToRemove = new ArrayList<>();

    /**
     * True if the attachments have been changed by the user.
     */
    protected boolean attachmentsChanged = false;

    /**
     * All components which should be added to the message.
     */
    protected final List<HighLevelComponent> components = new ArrayList<>();

    /**
     * True if the components have been changed by the user.
     */
    protected boolean componentsChanged = false;

    /**
     * The MentionsBuilder used to control mention behavior.
     */
    protected AllowedMentions allowedMentions = null;

    /**
     * The message to reply to.
     */
    protected Long replyingTo = null;

    /**
     * The stickers attached to the message.
     */
    protected Set<Long> stickerIds = new HashSet<>();

    @Override
    public void addComponents(HighLevelComponent... highLevelComponents) {
        this.components.addAll(Arrays.asList(highLevelComponents));
        componentsChanged = true;
    }

    @Override
    public void addActionRow(LowLevelComponent... lowLevelComponents) {
        this.addComponents(ActionRow.of(lowLevelComponents));
        componentsChanged = true;
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
        contentChanged = true;
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
        contentChanged = true;
    }

    @Override
    public void append(Mentionable entity) {
        strBuilder.append(entity.getMentionTag());
        contentChanged = true;
    }

    @Override
    public void append(Object object) {
        strBuilder.append(object);
        contentChanged = true;
    }

    @Override
    public void appendNamedLink(final String name, final String url) {
        strBuilder
                .append("[")
                .append(name)
                .append("]")
                .append("(")
                .append(url)
                .append(")");
        contentChanged = true;
    }

    @Override
    public void appendNewLine() {
        strBuilder.append("\n");
        contentChanged = true;
    }

    @Override
    public void setContent(String content) {
        strBuilder.setLength(0);
        strBuilder.append(content);
        contentChanged = true;
    }

    @Override
    public void removeExistingAttachment(Attachment attachment) {
        attachmentsToRemove.add(attachment);
    }

    @Override
    public void removeExistingAttachments() {
        removeAllAttachments = true;
    }

    @Override
    public void removeExistingAttachments(Collection<Attachment> attachments) {
        attachmentsToRemove.addAll(attachments);
    }

    @Override
    public void addEmbed(EmbedBuilder embed) {
        if (embed != null) {
            embeds.add(embed);
            embedsChanged = true;
        }
    }

    /**
     * Fill the builder's values with a message.
     *
     * @param message The message to copy.
     */
    @Override
    public void copy(Message message) {
        this.getStringBuilder().append(message.getContent());

        message.getEmbeds().forEach(embed -> addEmbed(embed.toBuilder()));

        for (MessageAttachment attachment : message.getAttachments()) {
            // Since spoiler status is encoded in the file name, it is copied automatically.
            this.addAttachment(attachment.getUrl(), attachment.getDescription().orElse(null));
        }

        for (HighLevelComponent component : message.getComponents()) {
            if (component.getType() == ComponentType.ACTION_ROW) {
                ActionRowBuilder builder = new ActionRowBuilder();
                builder.copy((ActionRow) component);
                this.addComponents(builder.build());
            }
        }

        contentChanged = false;
        componentsChanged = false;
        attachmentsChanged = false;
        embedsChanged = false;
    }

    @Override
    public void removeAllEmbeds() {
        embeds.clear();
        embedsChanged = true;
    }

    @Override
    public void addEmbeds(List<EmbedBuilder> embeds) {
        embeds.forEach(this::addEmbed);
    }

    @Override
    public void removeEmbed(EmbedBuilder embed) {
        this.embeds.remove(embed);
        embedsChanged = true;
    }

    @Override
    public void removeEmbeds(EmbedBuilder... embeds) {
        this.embeds.removeAll(Arrays.asList(embeds));
        embedsChanged = true;
    }

    @Override
    public void removeComponent(int index) {
        components.remove(index);
        componentsChanged = true;
    }

    @Override
    public void removeComponent(HighLevelComponent component) {
        components.remove(component);
        componentsChanged = true;
    }

    @Override
    public void removeAllComponents() {
        components.clear();
        componentsChanged = true;
    }

    @Override
    public void setTts(boolean tts) {
        this.tts = tts;
    }

    @Override
    public void addAttachment(BufferedImage image, String fileName, String description) {
        if (image == null || fileName == null) {
            throw new IllegalArgumentException("image and fileName cannot be null!");
        }
        newAttachments.add(new FileContainer(image, fileName, description));
        attachmentsChanged = true;
    }

    @Override
    public void addAttachment(File file, String description) {
        if (file == null) {
            throw new IllegalArgumentException("file cannot be null!");
        }
        newAttachments.add(new FileContainer(file, description));
        attachmentsChanged = true;
    }

    @Override
    public void addAttachment(Icon icon, String description) {
        if (icon == null) {
            throw new IllegalArgumentException("icon cannot be null!");
        }
        newAttachments.add(new FileContainer(icon, description));
        attachmentsChanged = true;
    }

    @Override
    public void addAttachment(URL url, String description) {
        if (url == null) {
            throw new IllegalArgumentException("url cannot be null!");
        }
        newAttachments.add(new FileContainer(url, description));
        attachmentsChanged = true;
    }

    @Override
    public void addAttachment(byte[] bytes, String fileName, String description) {
        if (bytes == null || fileName == null) {
            throw new IllegalArgumentException("bytes and fileName cannot be null!");
        }
        newAttachments.add(new FileContainer(bytes, fileName, description));
        attachmentsChanged = true;
    }

    @Override
    public void addAttachment(InputStream stream, String fileName, String description) {
        if (stream == null || fileName == null) {
            throw new IllegalArgumentException("stream and fileName cannot be null!");
        }
        newAttachments.add(new FileContainer(stream, fileName, description));
        attachmentsChanged = true;
    }

    @Override
    public void addAttachmentAsSpoiler(File file, String description) {
        if (file == null) {
            throw new IllegalArgumentException("file cannot be null!");
        }
        newAttachments.add(new FileContainer(file, true, description));
        attachmentsChanged = true;
    }

    @Override
    public void addAttachmentAsSpoiler(Icon icon, String description) {
        if (icon == null) {
            throw new IllegalArgumentException("icon cannot be null!");
        }
        newAttachments.add(new FileContainer(icon, true, description));
        attachmentsChanged = true;
    }

    @Override
    public void addAttachmentAsSpoiler(URL url, String description) {
        if (url == null) {
            throw new IllegalArgumentException("url cannot be null!");
        }
        newAttachments.add(new FileContainer(url, true, description));
        attachmentsChanged = true;
    }

    @Override
    public void setAllowedMentions(AllowedMentions allowedMentions) {
        if (allowedMentions == null) {
            throw new IllegalArgumentException("mention cannot be null!");
        }
        this.allowedMentions = allowedMentions;
    }

    @Override
    public void replyTo(long messageId) {
        replyingTo = messageId;
    }

    @Override
    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    @Override
    public void addSticker(long stickerId) {
        this.stickerIds.add(stickerId);
    }

    @Override
    public void addStickers(Collection<Long> stickerIds) {
        this.stickerIds.addAll(stickerIds);
    }

    @Override
    public StringBuilder getStringBuilder() {
        return strBuilder;
    }

    ////////////////////////////////////////////////////////////////////////////////
    // Send methods
    ////////////////////////////////////////////////////////////////////////////////

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
        } else if (messageable instanceof Member) {
            return send(((Member) messageable).getUser());
        } else if (messageable instanceof IncomingWebhook) {
            return send((IncomingWebhook) messageable);
        }
        throw new IllegalStateException("Messageable of unknown type");
    }

    @Override
    public CompletableFuture<Message> send(TextChannel channel) {
        ObjectNode body = JsonNodeFactory.instance.objectNode()
                .put("content", toString() == null ? "" : toString())
                .put("tts", tts);
        body.putArray("mentions");

        prepareAllowedMentions(body);

        prepareEmbeds(body, false);

        prepareComponents(body, false);

        if (nonce != null) {
            body.put("nonce", nonce);
        }

        if (!stickerIds.isEmpty()) {
            ArrayNode stickersNode = JsonNodeFactory.instance.objectNode().arrayNode();
            for (long stickerId : stickerIds) {
                stickersNode.add(stickerId);
            }
            body.set("sticker_ids", stickersNode);
        }

        if (replyingTo != null) {
            body.putObject("message_reference").put("message_id", replyingTo);
        }

        RestRequest<Message> request = new RestRequest<Message>(channel.getApi(), RestMethod.POST, RestEndpoint.MESSAGE)
                .setUrlParameters(channel.getIdAsString());
        return checkForAttachmentsAndExecuteRequest(channel, body, request, false);
    }

    @Override
    public CompletableFuture<Message> send(IncomingWebhook webhook) {
        return send(webhook.getIdAsString(), webhook.getToken(),
                null, null, true, webhook.getApi());
    }

    /**
     * Send a message to an incoming webhook.
     *
     * @param webhookId    The id of the webhook to send the message to
     * @param webhookToken The token of the webhook to send the message to
     * @param displayName  The display name the webhook should use
     * @param avatarUrl    The avatar the webhook should use
     * @param wait         If the completable future will be completed
     * @param api          The api instance needed to send and return the message
     * @return The sent message
     */
    protected CompletableFuture<Message> send(String webhookId, String webhookToken, String displayName, URL avatarUrl,
                                              boolean wait, DiscordApi api) {
        ObjectNode body = JsonNodeFactory.instance.objectNode();
        prepareCommonWebhookMessageBodyParts(body);

        if (displayName != null) {
            body.put("username", displayName);
        }
        if (avatarUrl != null) {
            body.put("avatar_url", avatarUrl.toExternalForm());
        }

        prepareComponents(body);

        if (strBuilder.length() != 0) {
            body.put("content", strBuilder.toString());
        }

        RestRequest<Message> request =
                new RestRequest<Message>(api, RestMethod.POST, RestEndpoint.WEBHOOK_SEND)
                        .addQueryParameter("wait", Boolean.toString(wait))
                        .setUrlParameters(webhookId, webhookToken)
                        .consumeGlobalRatelimit(false)
                        .includeAuthorizationHeader(false);
        CompletableFuture<Message> future = new CompletableFuture<>();
        if (!newAttachments.isEmpty() || embeds.stream().anyMatch(EmbedBuilder::requiresAttachments)) {
            // We access files etc. so this should be async
            api.getThreadPool().getExecutorService().submit(() -> {
                try {
                    List<FileContainer> tempAttachments = new ArrayList<>(newAttachments);
                    // Add the attachments required for the embeds
                    for (EmbedBuilder embed : embeds) {
                        tempAttachments.addAll(
                                ((EmbedBuilderDelegateImpl) embed.getDelegate()).getRequiredAttachments());
                    }

                    addMultipartBodyToRequest(request, body, tempAttachments, api);

                    executeWebhookRest(request, wait, future, api);
                } catch (Throwable t) {
                    future.completeExceptionally(t);
                }
            });
        } else {
            request.setBody(body);
            executeWebhookRest(request, wait, future, api);
        }
        return future;
    }

    @Override
    public CompletableFuture<Message> sendWithWebhook(DiscordApi api, String webhookId, String webhookToken) {
        return send(webhookId, webhookToken, null, null, true, api);
    }

    /**
     * Method which executes the webhook rest request.
     *
     * @param request The rest request to execute
     * @param wait    If discord sends us a response
     * @param future  The future to complete
     * @param api     The api instance needed to create the message
     */
    private static void executeWebhookRest(RestRequest<Message> request, boolean wait,
                                           CompletableFuture<Message> future, DiscordApi api) {
        CompletableFuture<Message> tmpFuture;
        if (wait) {
            tmpFuture = request.execute(result -> {
                JsonNode body = result.getJsonBody();
                TextChannel channel = api.getTextChannelById(body.get("channel_id").asText()).orElseThrow(() ->
                        new IllegalStateException("Cannot return a message when the channel isn't cached!")
                );
                return ((DiscordApiImpl) api).getOrCreateMessage(channel, body);
            });
        } else {
            tmpFuture = request.execute(result -> null);
        }
        tmpFuture.whenComplete((message, throwable) -> {
            if (throwable != null) {
                future.completeExceptionally(throwable);
            } else {
                future.complete(message);
            }
        });
    }

    @Override
    public CompletableFuture<Message> edit(Message message, boolean updateAll) {
        ObjectNode body = JsonNodeFactory.instance.objectNode();

        if (updateAll || contentChanged) {
            body.put("content", strBuilder.toString());
        }

        prepareAllowedMentions(body);

        prepareEmbeds(body, updateAll || embedsChanged);

        prepareComponents(body, updateAll || componentsChanged);

        prepareAttachments(message.getAttachments(), body, updateAll || removeAllAttachments);

        RestRequest<Message> request = new RestRequest<Message>(message.getApi(),
                RestMethod.PATCH, RestEndpoint.MESSAGE)
                .setUrlParameters(Long.toUnsignedString(message.getChannel().getId()),
                        Long.toUnsignedString(message.getId()));

        if (updateAll || attachmentsChanged) {
            return checkForAttachmentsAndExecuteRequest(message.getChannel(), body, request, true);
        } else {
            return executeRequestWithoutNewAttachments(message.getChannel(), body, request);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////
    // Internal MessageBuilder utility methods
    ////////////////////////////////////////////////////////////////////////////////

    private CompletableFuture<Message> checkForAttachmentsAndExecuteRequest(TextChannel channel,
                                                                            ObjectNode body,
                                                                            RestRequest<Message> request,
                                                                            boolean clearAttachmentsIfAppropriate) {
        if (newAttachments.isEmpty() && embeds.stream().noneMatch(EmbedBuilder::requiresAttachments)) {
            return executeRequestWithoutNewAttachments(channel, body, request);
        }

        CompletableFuture<Message> future = new CompletableFuture<>();
        // We access files etc. so this should be async
        channel.getApi().getThreadPool().getExecutorService().submit(() -> {
            try {
                List<FileContainer> tempAttachments = new ArrayList<>(newAttachments);
                // Add the attachments required for the embeds
                for (EmbedBuilder embed : embeds) {
                    tempAttachments.addAll(
                            ((EmbedBuilderDelegateImpl) embed.getDelegate()).getRequiredAttachments());
                }

                addMultipartBodyToRequest(request, body, tempAttachments, channel.getApi());

                request.execute(result -> ((DiscordApiImpl) channel.getApi())
                                .getOrCreateMessage(channel, result.getJsonBody()))
                        .whenComplete((newMessage, throwable) -> {
                            if (throwable != null) {
                                future.completeExceptionally(throwable);
                            } else {
                                future.complete(newMessage);
                            }
                        });
            } catch (Throwable t) {
                future.completeExceptionally(t);
            }
        });
        return future;
    }

    private CompletableFuture<Message> executeRequestWithoutNewAttachments(TextChannel channel,
                                                                           ObjectNode body,
                                                                           RestRequest<Message> request) {
        request.setBody(body);
        return request.execute(
                result -> ((DiscordApiImpl) channel.getApi()).getOrCreateMessage(channel, result.getJsonBody()));
    }

    private void prepareAllowedMentions(ObjectNode body) {
        if (allowedMentions != null) {
            ((AllowedMentionsImpl) allowedMentions).toJsonNode(body.putObject("allowed_mentions"));
        }
    }

    private void prepareAttachments(List<MessageAttachment> attachmentsList, ObjectNode body,
                                    boolean removeOrUpdateAll) {
        ArrayNode attachments = body.putArray("attachments");

        if (removeOrUpdateAll) {
            attachments.add(JsonNodeFactory.instance.objectNode());
        } else if (!attachmentsToRemove.isEmpty()) {
            for (Attachment attachment : attachmentsList) {
                if (!attachmentsToRemove.contains(attachment)) {
                    attachments.add(((AttachmentImpl) attachment).toJsonNode());
                }
            }
        } else {
            for (Attachment attachment : attachmentsList) {
                attachments.add(((AttachmentImpl) attachment).toJsonNode());
            }
        }
    }

    /**
     * Method which creates and adds a MultipartBody to a RestRequest.
     *
     * @param request     The RestRequest to add the MultipartBody to
     * @param body        The body to use as base for the MultipartBody
     * @param attachments The List of FileContainers to add as attachments
     * @param api         The api instance needed to add the attachments
     */
    protected void addMultipartBodyToRequest(RestRequest<?> request, ObjectNode body,
                                             List<FileContainer> attachments, DiscordApi api) {
        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        Collections.reverse(attachments);
        for (int i = 0; i < attachments.size(); i++) {
            FileContainer fileContainer = attachments.get(i);
            byte[] bytes = fileContainer.asByteArray(api).join();
            String mediaType = URLConnection
                    .guessContentTypeFromName(fileContainer.getFileTypeOrName());
            if (mediaType == null) {
                mediaType = "application/octet-stream";
            }
            multipartBodyBuilder.addFormDataPart("files[" + i + "]", fileContainer.getFileTypeOrName(),
                    RequestBody.create(bytes, MediaType.parse(mediaType)));

            if (fileContainer.getDescription() != null) {
                ArrayNode attachmentJson = body.withArray("attachments");
                ObjectNode newFileAttachment = JsonNodeFactory.instance.objectNode();
                newFileAttachment.put("id", i);
                newFileAttachment.put("description", fileContainer.getDescription());
                attachmentJson.add(newFileAttachment);
            }
        }
        multipartBodyBuilder.addFormDataPart("payload_json", body.toString());
        request.setMultipartBody(multipartBodyBuilder.build());
    }

    protected void prepareCommonWebhookMessageBodyParts(ObjectNode body) {
        body.put("tts", this.tts);
        if (strBuilder.length() != 0) {
            body.put("content", strBuilder.toString());
        }
        prepareAllowedMentions(body);
        prepareEmbeds(body, false);
    }

    private void prepareEmbeds(ObjectNode body, boolean evenIfEmpty) {
        if (!embeds.isEmpty() || evenIfEmpty) {
            ArrayNode embedsNode = JsonNodeFactory.instance.objectNode().arrayNode();
            for (EmbedBuilder embed : embeds) {
                embedsNode.add(((EmbedBuilderDelegateImpl) embed.getDelegate()).toJsonNode());
            }
            body.set("embeds", embedsNode);
        }
    }

    @Override
    public String toString() {
        return strBuilder.toString();
    }

    protected void prepareComponents(ObjectNode body) {
        prepareComponents(body, false);
    }

    protected void prepareComponents(ObjectNode body, boolean evenIfEmpty) {
        if (evenIfEmpty || !components.isEmpty()) {
            ArrayNode componentsNode = JsonNodeFactory.instance.objectNode().arrayNode();
            components.forEach(
                    highLevelComponent -> componentsNode.add(((ComponentImpl) highLevelComponent).toJsonNode()));
            body.set("components", componentsNode);
        }
    }
}
