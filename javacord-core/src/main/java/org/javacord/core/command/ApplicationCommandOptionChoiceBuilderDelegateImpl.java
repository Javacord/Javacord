package org.javacord.core.command;

import org.javacord.api.command.ApplicationCommandOptionChoice;
import org.javacord.api.command.internal.ApplicationCommandOptionChoiceBuilderDelegate;

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
