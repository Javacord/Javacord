package org.javacord.core.util;

import org.javacord.api.DiscordApi;

/**
 * This class represents an object which can clean up after itself.
 * It should be used on objects that need to clean up references to itself that would prevent garbage collection or when
 * it "owns" cleanupable objects.
 * The most prominent example is, when the {@link DiscordApi#getThreadPool() thread pool} is used to schedule a
 * permanently repeated action on the object. This will prevent garbage collection as the scheduled task will have a
 * hard reference to the instance and even if this is wrapped in a {@code WeakReference}, if a lambda is used as task,
 * then the lambda has an implicit hard reference to the object. Thus, the scheduled task should be cancelled in the
 * {@link #cleanup()} method which should then be called by the owning object accordingly.
 */
public interface Cleanupable {

    /**
     * Does any cleanup that would prevent this instance from being eligible for garbage collection like cancelling
     * scheduled repeated tasks or calling {@code cleanup} on "owned" cleanupable objects.
     * This method has to be thread-safe and idempotent.
     */
    void cleanup();

}
