package org.javacord.api.util.logging.internal;

import org.javacord.api.util.logging.ExceptionLogger;

import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * This class is internally used by the {@link ExceptionLogger}.
 * You usually don't want to interact with this object.
 */
public interface ExceptionLoggerDelegate {

    /**
     * Returns a function that can be used in the {@link CompletableFuture#exceptionally(Function)} method.
     * It unwraps {@link CompletionException CompletionExceptions},
     * {@link InvocationTargetException InvocationTargetExceptions} and {@link ExecutionException ExecutionExceptions}
     * first, and then adds a fresh {@code CompletionException} as wrapper with the stacktrace of the caller of this
     * method and logs it afterwards.
     * The rewrapped exception is only logged if the given {@code logFilter} predicate allows the exception and the
     * class of it is not in the {@code ignoredThrowableTypes}.
     *
     * @param <T> The return type of the function.
     * @param logFilter The predicate the unwrapped exception is tested against.
     * @param ignoredThrowableTypes The throwable types that should never be logged.
     * @param stackTrace The stack trace pointing to the caller of this method.
     * @return A function which logs the given throwable and returns {@code null}.
     */
    <T> Function<Throwable, T> get(Predicate<Throwable> logFilter,
                                   Collection<Class<? extends Throwable>> ignoredThrowableTypes,
                                   StackTraceElement[] stackTrace);

    /**
     * Returns an uncaught exception handler that can be used in the
     * {@link Thread#setDefaultUncaughtExceptionHandler(UncaughtExceptionHandler)} and
     * {@link Thread#setUncaughtExceptionHandler(UncaughtExceptionHandler)} methods.
     * It unwraps {@link CompletionException CompletionExceptions},
     * {@link InvocationTargetException InvocationTargetExceptions} and {@link ExecutionException ExecutionExceptions}
     * first and logs it afterwards, including the thread name.
     *
     * @return An {@link UncaughtExceptionHandler} which logs the given throwable.
     */
    UncaughtExceptionHandler getUncaughtExceptionHandler();

    /**
     * Unwraps {@link CompletionException CompletionExceptions},
     * {@link InvocationTargetException InvocationTargetExceptions} and {@link ExecutionException ExecutionExceptions}
     * and returns the actual cause, or the given argument itself if it is not one of the listed exceptions types.
     * If the bottom-most exception to unwrap does not have a cause, it is returned itself instead.
     *
     * @param throwable The throwable to unwrap.
     * @return The unwrapped throwable.
     */
    static Throwable unwrapThrowable(Throwable throwable) {
        Throwable result = throwable;
        Throwable cause = result.getCause();
        while ((result instanceof CompletionException) && (cause != null)) {
            result = cause;
            cause = result.getCause();
        }
        return result instanceof CompletionException ? throwable : result;
    }

}
