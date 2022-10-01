package org.javacord.core.interaction;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.interaction.ApplicationCommandType;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.core.DiscordApiImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SlashCommandImpl extends ApplicationCommandImpl implements SlashCommand {

    private final List<SlashCommandOption> options;

    /**
     * Class constructor.
     *
     * @param api The api instance.
     * @param data The JSON data.
     */
    public SlashCommandImpl(DiscordApiImpl api, JsonNode data) {
        super(api, data);
        options = new ArrayList<>();
        if (data.has("options")) {
            for (JsonNode optionJson : data.get("options")) {
                options.add(new SlashCommandOptionImpl(optionJson));
            }
        }
    }

    @Override
    public ApplicationCommandType getType() {
        return ApplicationCommandType.SLASH;
    }

    @Override
    public String getFullCommandName() {
        return getName() + getNestedCommandNamesRecursive(getOptions());
    }

    private String getNestedCommandNamesRecursive(List<SlashCommandOption> options) {
        if (!options.isEmpty() && options.get(0).isSubcommandOrGroup()) {
            return " " + options.get(0).getName() + getNestedCommandNamesRecursive(options.get(0).getOptions());
        } else {
            return "";
        }
    }

    @Override
    public List<SlashCommandOption> getOptions() {
        return Collections.unmodifiableList(options);
    }

}
