package org.javacord.core.interaction;

import org.javacord.api.entity.channel.ChannelType;
import org.javacord.api.interaction.DiscordLocale;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionChoice;
import org.javacord.api.interaction.SlashCommandOptionType;
import org.javacord.api.interaction.internal.SlashCommandOptionBuilderDelegate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SlashCommandOptionBuilderDelegateImpl implements SlashCommandOptionBuilderDelegate {

    private SlashCommandOptionType type;
    private String name;
    private Map<DiscordLocale, String> nameLocalizations = new HashMap<>();
    private String description;
    private Map<DiscordLocale, String> descriptionLocalizations = new HashMap<>();
    private boolean required = false;
    private boolean autocompletable = false;
    private List<SlashCommandOptionChoice> choices = new ArrayList<>();
    private List<SlashCommandOption> options = new ArrayList<>();
    private Set<ChannelType> channelTypes = new HashSet<>();
    private Long longMinValue;
    private Long longMaxValue;
    private Double decimalMinValue;
    private Double decimalMaxValue;

    private Long minLength;
    private Long maxLength;

    @Override
    public void setType(SlashCommandOptionType type) {
        this.type = type;
    }

    @Override
    public void setName(String name) {
        this.name = name.toLowerCase();
    }

    @Override
    public void addNameLocalization(DiscordLocale locale, String localization) {
        nameLocalizations.put(locale, localization);
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void addDescriptionLocalization(DiscordLocale locale, String localization) {
        descriptionLocalizations.put(locale, localization);
    }

    @Override
    public void setRequired(boolean required) {
        this.required = required;
    }

    @Override
    public void setAutocompletable(boolean autocompletable) {
        this.autocompletable = autocompletable;
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
    public void addChannelType(ChannelType channelType) {
        channelTypes.add(channelType);
    }

    @Override
    public void setChannelTypes(Collection<ChannelType> channelTypes) {
        if (channelTypes == null) {
            this.channelTypes.clear();
        } else {
            this.channelTypes = new HashSet<>(channelTypes);
        }
    }

    @Override
    public void setLongMinValue(long longMinValue) {
        this.longMinValue = longMinValue;
    }

    @Override
    public void setLongMaxValue(long longMaxValue) {
        this.longMaxValue = longMaxValue;
    }

    @Override
    public void setDecimalMinValue(double decimalMinValue) {
        this.decimalMinValue = decimalMinValue;
    }

    @Override
    public void setDecimalMaxValue(double decimalMaxValue) {
        this.decimalMaxValue = decimalMaxValue;
    }

    @Override
    public void setMinLength(long minLength) {
        this.minLength = minLength;
    }

    @Override
    public void setMaxLength(long maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public SlashCommandOption build() {
        return new SlashCommandOptionImpl(type, name, nameLocalizations, description, descriptionLocalizations,
                required, autocompletable, choices, options, channelTypes, longMinValue, longMaxValue,
                decimalMinValue, decimalMaxValue, minLength, maxLength);
    }
}
