package org.javacord.core.interaction;

import org.javacord.api.interaction.ApplicationCommandOptionChoice;
import org.javacord.api.interaction.internal.ApplicationCommandOptionChoiceBuilderDelegate;

public class ApplicationCommandOptionChoiceBuilderDelegateImpl
        implements ApplicationCommandOptionChoiceBuilderDelegate {

    private String name;
    private String stringValue;
    private Integer intValue;

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setValue(String value) {
        stringValue = value;
        intValue = null;
    }

    @Override
    public void setValue(int value) {
        stringValue = null;
        intValue = value;
    }

    @Override
    public ApplicationCommandOptionChoice build() {
        return new ApplicationCommandOptionChoiceImpl(name, stringValue, intValue);
    }
}
