package org.javacord.core.command;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.command.ApplicationCommandInteractionData;
import org.javacord.api.command.ApplicationCommandInteractionDataOption;
import org.javacord.core.DiscordApiImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ApplicationCommandInteractionDataImpl implements ApplicationCommandInteractionData {

    private final DiscordApiImpl api;

    private final long id;
    private final String name;
    private final List<ApplicationCommandInteractionDataOption> options;

    /**
     * Class constructor.
     *
     * @param api The api instance.
     * @param jsonData The json data of the interaction data.
     */
    public ApplicationCommandInteractionDataImpl(DiscordApiImpl api, JsonNode jsonData) {
        this.api = api;

        id = jsonData.get("id").asLong();
        name = jsonData.get("name").asText();
        options = new ArrayList<>();
        if (jsonData.has("options") && jsonData.get("options").isArray()) {
            for (JsonNode optionJson : jsonData.get("options")) {
                options.add(new ApplicationCommandInteractionDataOptionImpl(optionJson));
            }
        }
    }

    @Override
    public DiscordApi getApi() {
        return api;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<ApplicationCommandInteractionDataOption> getOptions() {
        return Collections.unmodifiableList(options);
    }
}
