package org.javacord.core.interaction;

import org.javacord.api.interaction.SlashCommandOptionChoice;
import org.javacord.api.interaction.internal.SlashCommandOptionChoiceBuilderDelegate;

public class SlashCommandOptionChoiceBuilderDelegateImpl implements SlashCommandOptionChoiceBuilderDelegate {

    private String name;
    private String stringValue;
    private Long longValue;

    @Override
    public void setName(String name) {
        this.name = name;
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
        return new SlashCommandOptionChoiceImpl(name, stringValue, longValue);
    }
}
