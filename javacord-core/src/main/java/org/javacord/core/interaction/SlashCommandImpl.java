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
     * @param api  The api instance.
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
    public List<String> getFullCommandNames() {
        final List<String> names = new ArrayList<>();
        getNestedCommandNamesRecursive(getName(), getOptions(), names);
        return names;
    }

    private void getNestedCommandNamesRecursive(final String preName, final List<SlashCommandOption> options,
                                                final List<String> names) {
        if (options.isEmpty()) {
            names.add(preName);
        } else {
            for (final SlashCommandOption option : options) {
                if (option.isSubcommandOrGroup()) {
                    getNestedCommandNamesRecursive(preName + " " + option.getName(), option.getOptions(), names);
                } else {
                    names.add(preName);
                }
            }
        }

    }

    @Override
    public List<SlashCommandOption> getOptions() {
        return Collections.unmodifiableList(options);
    }

}