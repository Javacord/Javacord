package org.javacord.api.entity.channel;

import org.javacord.api.entity.channel.internal.TextableRegularServerChannelUpdaterDelegate;
import org.javacord.api.util.internal.DelegateFactory;

public class TextableRegularServerChannelUpdater<T extends TextableRegularServerChannelUpdater<T>>
        extends RegularServerChannelUpdater<T> {

    /**
     * The channel delegate used by this instance.
     */
    protected final TextableRegularServerChannelUpdaterDelegate textableRegularServerChannelUpdaterDelegate;

    /**
     * Creates a new textable regular server channel updater.
     *
     * @param delegate A subtype of a RegularServerChannelUpdaterDelegate.
     */
    protected TextableRegularServerChannelUpdater(TextableRegularServerChannelUpdaterDelegate delegate) {
        super(delegate);
        this.textableRegularServerChannelUpdaterDelegate = delegate;
    }

    /**
     * Creates a new textable regular server channel updater.
     *
     * @param channel The channel to update.
     */
    public TextableRegularServerChannelUpdater(TextableRegularServerChannel channel) {
        this(DelegateFactory.createTextableRegularServerChannelUpdaterDelegate(channel));
    }

    /**
     * Queues the category to be updated.
     *
     * @param category The new category of the channel.
     * @return The current instance in order to chain call methods.
     */
    public TextableRegularServerChannelUpdater<T> setCategory(ChannelCategory category) {
        textableRegularServerChannelUpdaterDelegate.setCategory(category);
        return this;
    }

    /**
     * Queues the category to be removed.
     *
     * @return The current instance in order to chain call methods.
     */
    public TextableRegularServerChannelUpdater<T> removeCategory() {
        textableRegularServerChannelUpdaterDelegate.removeCategory();
        return this;
    }

    /**
     * Queues the nsfw to be updated.
     *
     * @param nsfw The new nsfw flag of the channel.
     * @return The current instance in order to chain call methods.
     */
    public TextableRegularServerChannelUpdater<T> setNsfw(boolean nsfw) {
        textableRegularServerChannelUpdaterDelegate.setNsfw(nsfw);
        return this;
    }

    /**
     * Set the delay for slowmode.
     *
     * @param delay The delay in seconds.
     * @return The current instance in order to chain call methods.
     */
    public TextableRegularServerChannelUpdater<T> setSlowmodeDelayInSeconds(int delay) {
        textableRegularServerChannelUpdaterDelegate.setSlowmodeDelayInSeconds(delay);
        return this;
    }

    /**
     * Unset the slowmode.
     *
     * @return The current instance in order to chain call methods.
     */
    public TextableRegularServerChannelUpdater<T> unsetSlowmode() {
        return this.setSlowmodeDelayInSeconds(0);
    }
}

