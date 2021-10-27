package org.javacord.api.interaction;

import org.javacord.api.interaction.internal.SlashCommandUpdaterDelegate;
import org.javacord.api.util.internal.DelegateFactory;
import java.util.List;

public class SlashCommandUpdater
        extends ApplicationCommandUpdater<SlashCommand, SlashCommandUpdaterDelegate, SlashCommandUpdater> {

    /**
     * The account delegate used by this instance.
     */
    private final SlashCommandUpdaterDelegate delegate;

    /**
     * Creates a new slash command updater.
     *
     * @param commandId The slash command id which should be updated.
     */
    public SlashCommandUpdater(long commandId) {
        super(DelegateFactory.createSlashCommandUpdaterDelegate(commandId));
        delegate = super.getDelegate();
    }

    /**
     * Sets the new description of the slash command.
     *
     * @param description The description to set.
     * @return The current instance in order to chain call methods.
     */
    public SlashCommandUpdater setDescription(String description) {
        delegate.setDescription(description);
        return this;
    }

    /**
     * Sets the new slash command options.
     *
     * @param slashCommandOptions The slash command options to set.
     * @return The current instance in order to chain call methods.
     */
    public SlashCommandUpdater setSlashCommandOptions(List<SlashCommandOption> slashCommandOptions) {
        delegate.setOptions(slashCommandOptions);
        return this;
    }

}
