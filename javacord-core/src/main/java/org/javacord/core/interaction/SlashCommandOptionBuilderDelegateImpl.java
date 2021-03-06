package org.javacord.core.interaction;

import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionChoice;
import org.javacord.api.interaction.SlashCommandOptionType;
import org.javacord.api.interaction.internal.SlashCommandOptionBuilderDelegate;

import java.util.ArrayList;
import java.util.List;

public class SlashCommandOptionBuilderDelegateImpl implements SlashCommandOptionBuilderDelegate {

    private SlashCommandOptionType type;
    private String name;
    private String description;
    private boolean required;
    private List<SlashCommandOptionChoice> choices = new ArrayList<>();
    private List<SlashCommandOption> options = new ArrayList<>();

    @Override
    public void setType(SlashCommandOptionType type) {
        this.type = type;
    }

    @Override
    public void setName(String name) {
        this.name = name.toLowerCase();
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void setRequired(boolean required) {
        this.required = required;
    }

    @Override
    public void addChoice(SlashCommandOptionChoice choice) {
        choices.add(choice);
    }

    @Override
    public void setChoices(List<SlashCommandOptionChoice> choices) {
        if (choices == null) {
            this.choices.clear();
        } else {
            this.choices = new ArrayList<>(choices);
        }
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

    @Override
    public SlashCommandOption build() {
        return new SlashCommandOptionImpl(type, name, description, required, choices, options);
    }
}
