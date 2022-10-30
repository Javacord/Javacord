package org.javacord.core.interaction;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.Attachment;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.InteractionType;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandInteractionOption;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.AttachmentImpl;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.entity.user.MemberImpl;
import org.javacord.core.entity.user.UserImpl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SlashCommandInteractionImpl extends ApplicationCommandInteractionImpl implements SlashCommandInteraction {

    private final List<SlashCommandInteractionOption> options;

    /**
     * Class constructor.
     *
     * @param api      The api instance.
     * @param channel  The channel in which the interaction happened. Can be {@code null}.
     * @param jsonData The json data of the interaction.
     */
    public SlashCommandInteractionImpl(DiscordApiImpl api, TextChannel channel, JsonNode jsonData) {
        super(api, channel, jsonData);

        JsonNode data = jsonData.get("data");
        Map<Long, User> resolvedUsers = new HashMap<>();
        Map<Long, Attachment> resolvedAttachments = new HashMap<>();
        if (data.has("resolved")) {
            JsonNode resolved = data.get("resolved");
            if (jsonData.has("guild_id")) {
                ServerImpl server = (ServerImpl) api.getServerById(jsonData.get("guild_id").asLong())
                        .orElseThrow(AssertionError::new);
                if (resolved.has("members")) {
                    resolved.get("members").fields().forEachRemaining(memberNode -> {
                        Long id = Long.parseLong(memberNode.getKey());
                        JsonNode userData = resolved.get("users").get(String.valueOf(id));
                        resolvedUsers.put(id, new UserImpl(api, userData, memberNode.getValue(), server));
                    });
                }
            }
            if (resolved.has("users")) {
                resolved.get("users").fields().forEachRemaining(userNode -> {
                    JsonNode userData = userNode.getValue();
                    Long id = Long.parseLong(userNode.getKey());
                    if (!resolvedUsers.containsKey(id)) {
                        resolvedUsers.put(id, new UserImpl(api, userData, (MemberImpl) null, null));
                    }
                });
            }

            if (resolved.has("attachments")) {
                resolved.get("attachments").fields().forEachRemaining(attachmentNode ->
                        resolvedAttachments.put(
                                Long.parseLong(attachmentNode.getKey()),
                                new AttachmentImpl(api, attachmentNode.getValue())
                        )
                );
            }
        }
        options = new ArrayList<>();
        if (data.has("options") && data.get("options").isArray()) {
            for (JsonNode optionJson : data.get("options")) {
                options.add(new SlashCommandInteractionOptionImpl(api, optionJson, resolvedUsers, resolvedAttachments));
            }
        }
    }

    @Override
    public InteractionType getType() {
        return InteractionType.APPLICATION_COMMAND;
    }

    @Override
    public List<SlashCommandInteractionOption> getOptions() {
        return Collections.unmodifiableList(options);
    }

    @Override
    public long getCommandId() {
        return commandId;
    }

    @Override
    public String getCommandIdAsString() {
        return String.valueOf(commandId);
    }

    @Override
    public String getCommandName() {
        return commandName;
    }

    // TODO: Move the implementation to the SlashCommandInteractionOptionsProvider once we upgraded to Java 9+
    // because interfaces currently do not support private methods
    @Override
    public List<SlashCommandInteractionOption> getArguments() {
        return getArgumentsRecursive(getOptions());
    }

    private List<SlashCommandInteractionOption> getArgumentsRecursive(List<SlashCommandInteractionOption> options) {
        if (options.isEmpty()) {
            return Collections.emptyList();
        } else if (options.get(0).isSubcommandOrGroup()) {
            return getArgumentsRecursive(options.get(0).getOptions());
        } else {
            return options;
        }
    }

    @Override
    public String getFullCommandName() {
        return getCommandName() + getNestedCommandNamesRecursive(getOptions());
    }

    private String getNestedCommandNamesRecursive(List<SlashCommandInteractionOption> options) {
        if (!options.isEmpty() && options.get(0).isSubcommandOrGroup()) {
            return " " + options.get(0).getName() + getNestedCommandNamesRecursive(options.get(0).getOptions());
        } else {
            return "";
        }
    }

}
