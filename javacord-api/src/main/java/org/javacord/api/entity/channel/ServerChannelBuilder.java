package org.javacord.api.entity.channel;

import org.javacord.api.entity.channel.internal.ServerChannelBuilderDelegate;

/**
 * This class is used to create new server channels.
 */
public class ServerChannelBuilder<T> {

    protected final Class<T> myClass;

    /**
     * The server channel delegate used by this instance.
     */
    protected final ServerChannelBuilderDelegate delegate;

    /**
     * Creates a new server channel builder.
     */
    protected ServerChannelBuilder(Class<T> myClass, ServerChannelBuilderDelegate delegate) {
        this.myClass = myClass;
        this.delegate = delegate;
    }

    /**
     * Sets the reason for this creation. This reason will be visible in the audit log entry(s).
     *
     * @param reason The reason for this update.
     * @return The current instance in order to chain call methods.
     */
    public T setAuditLogReason(String reason) {
        delegate.setAuditLogReason(reason);
        return myClass.cast(this);
    }

    /**
     * Sets the name of the channel.
     *
     * @param name The name of the channel.
     * @return The current instance in order to chain call methods.
     */
    public T setName(String name) {
        delegate.setName(name);
        return myClass.cast(this);

    }

}
