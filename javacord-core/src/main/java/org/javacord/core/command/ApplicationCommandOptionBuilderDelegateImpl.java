package org.javacord.core.command;

import org.javacord.api.command.ApplicationCommandOption;
import org.javacord.api.command.ApplicationCommandOptionChoice;
import org.javacord.api.command.ApplicationCommandOptionType;
import org.javacord.api.command.internal.ApplicationCommandOptionBuilderDelegate;

import java.util.ArrayList;
import java.util.List;

public class ApplicationCommandOptionBuilderDelegateImpl implements ApplicationCommandOptionBuilderDelegate {

    private ApplicationCommandOptionType type;
    private String name;
    private String description;
    private boolean required;
    private List<ApplicationCommandOptionChoice> choices = new ArrayList<>();
    private List<ApplicationCommandOption> options = new ArrayList<>();

    @Override
    public void setType(ApplicationCommandOptionType type) {
        this.type = type;
    }

    @Override
    public void setName(String name) {
        this.name = name;
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
    public void addChoice(ApplicationCommandOptionChoice choice) {
        choices.add(choice);
    }

    @Override
    public void setChoices(List<ApplicationCommandOptionChoice> choices) {
        if (choices == null) {
            this.choices.clear();
        } else {
            this.choices = new ArrayList<>(choices);
        }
    }

    @Override
    public void addOption(ApplicationCommandOption option) {
        options.add(option);
    }

    @Override
    public void setOptions(List<ApplicationCommandOption> options) {
        if (options == null) {
            this.options.clear();
        } else {
            this.options = new ArrayList<>(options);
        }
    }

    @Override
    public ApplicationCommandOption build() {
        return new ApplicationCommandOptionImpl(type, name, description, required, choices, options);
    }
}
