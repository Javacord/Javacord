package org.javacord.core.interaction;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.interaction.ApplicationCommandType;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.internal.SlashCommandBuilderDelegate;
import org.javacord.core.DiscordApiImpl;
import java.util.ArrayList;
import java.util.List;

public class SlashCommandBuilderDelegateImpl extends ApplicationCommandBuilderDelegateImpl<SlashCommand>
        implements SlashCommandBuilderDelegate {

    private String description;
    private List<SlashCommandOption> options = new ArrayList<>();

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void addOption(SlashCommandOption option) {
        options.add(option);
    }

    @Override
    public void setOptions(List<SlashCommandOption> options) {
        if (options == null) {
            this.options.clear();
        } else {
            this.options = new ArrayList<>(options);
        }
    }

    /**
     * Gets the JSON body for this slash command.
     *
     * @return The JSON of this slash command.
     */
    public ObjectNode getJsonBodyForApplicationCommand() {
        ObjectNode jsonBody = super.getJsonBodyForApplicationCommand();
        jsonBody.put("description", description);

        if (!options.isEmpty()) {
            ArrayNode jsonOptions = jsonBody.putArray("options");
            options.stream()
                    .map(SlashCommandOptionImpl.class::cast)
                    .map(SlashCommandOptionImpl::toJsonNode)
                    .forEach(jsonOptions::add);
        }

        jsonBody.put("type", ApplicationCommandType.SLASH.getValue());

        return jsonBody;
    }

    @Override
    public SlashCommand createInstance(DiscordApiImpl api, JsonNode jsonNode) {
        return new SlashCommandImpl(api, jsonNode);
    }
}
