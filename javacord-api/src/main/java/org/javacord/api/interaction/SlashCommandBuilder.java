package org.javacord.api.interaction;

import org.javacord.api.interaction.internal.SlashCommandBuilderDelegate;
import org.javacord.api.util.internal.DelegateFactory;
import java.util.List;

/**
 * This class is used to create new slash commands.
 */
public class SlashCommandBuilder
        extends ApplicationCommandBuilder<SlashCommand, SlashCommandBuilderDelegate, SlashCommandBuilder> {

    private final SlashCommandBuilderDelegate delegate;

    /**
     * Creates a new slash command builder.
     */
    public SlashCommandBuilder() {
        super(DelegateFactory.createSlashCommandBuilderDelegate());
        delegate = super.getDelegate();
    }

    /**
     * Sets the description of the slash command.
     *
     * @param description The name.
     * @return The current instance in order to chain call methods.
     */
    public SlashCommandBuilder setDescription(String description) {
        delegate.setDescription(description);
        return this;
    }

    /**
     * Adds a slash command option to the slash command.
     *
     * @param option The option.
     * @return The current instance in order to chain call methods.
     */
    public SlashCommandBuilder addOption(SlashCommandOption option) {
        delegate.addOption(option);
        return this;
    }

    /**
     * Sets the slash commands for the slash command.
     *
     * @param options The options.
     * @return The current instance in order to chain call methods.
     */
    public SlashCommandBuilder setOptions(List<SlashCommandOption> options) {
        delegate.setOptions(options);
        return this;
    }

}
