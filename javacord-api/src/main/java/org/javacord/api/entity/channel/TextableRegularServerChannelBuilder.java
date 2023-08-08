package org.javacord.api.entity.channel;

import org.javacord.api.entity.channel.internal.TextableRegularServerChannelBuilderDelegate;

/**
 * This class is used to create new textable regular server channels.
 */
public class TextableRegularServerChannelBuilder<T> extends RegularServerChannelBuilder<T> {

    /**
     * The textable regular server channel builder delegate used by this instance.
     */
    protected final TextableRegularServerChannelBuilderDelegate delegate;

    /**
     * Creates a new textable regular server channel builder.
     *
     * @param myClass The class this builder is for.
     * @param delegate A subtype of a TextableRegularServerChannelBuilderDelegate.
     */
    protected TextableRegularServerChannelBuilder(Class<T> myClass,
                                                  TextableRegularServerChannelBuilderDelegate delegate) {
        super(myClass, delegate);
        this.delegate = delegate;
    }

    /**
     * Sets the category of the channel.
     *
     * @param category The category of the channel.
     * @return The current instance in order to chain call methods.
     */
    public TextableRegularServerChannelBuilder<T> setCategory(ChannelCategory category) {
        delegate.setCategory(category);
        return this;
    }


    /**
     * Sets the slowmode of the channel.
     *
     * @param delay The delay in seconds.
     * @return The current instance in order to chain call methods.
     */
    public TextableRegularServerChannelBuilder<T> setSlowmodeDelayInSeconds(int delay) {
        delegate.setSlowmodeDelayInSeconds(delay);
        return this;
    }
}
