package org.javacord.core.interaction;

import org.javacord.api.interaction.DiscordLocale;
import org.javacord.api.interaction.SlashCommandOptionChoice;
import org.javacord.api.interaction.internal.SlashCommandOptionChoiceBuilderDelegate;

import java.util.HashMap;
import java.util.Map;

public class SlashCommandOptionChoiceBuilderDelegateImpl implements SlashCommandOptionChoiceBuilderDelegate {

    private String name;
    private Map<DiscordLocale, String> nameLocalizations = new HashMap<>();
    private String stringValue;
    private Long longValue;

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void addNameLocalization(DiscordLocale locale, String localization) {
        nameLocalizations.put(locale, localization);
    }

    @Override
    public void setValue(String value) {
        stringValue = value;
        longValue = null;
    }

    @Override
    public void setValue(long value) {
        stringValue = null;
        longValue = value;
    }

    @Override
    public SlashCommandOptionChoice build() {
        return new SlashCommandOptionChoiceImpl(name, nameLocalizations, stringValue, longValue);
    }
}
