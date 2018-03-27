package org.javacord.api.util.logging;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.util.internal.DelegateFactory;
import org.javacord.api.util.logging.internal.ExceptionLoggerDelegate;

import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * This class contains some helpers to log exceptions.
 */
public class ExceptionLogger {

    /**
     * The exception logger delegate.
     */
    private static final ExceptionLoggerDelegate delegate = DelegateFactory.getExceptionLoggerDelegate();

    private ExceptionLogger() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns a consumer that can for example be used in the {@link TextChannel#typeContinuously(Consumer)} method.
     * It unwraps {@link CompletionException CompletionExceptions},
     * {@link InvocationTargetException InvocationTargetExceptions} and {@link ExecutionException ExecutionExceptions}
     * first, and then adds a fresh {@code CompletionException} as wrapper with the stacktrace of the caller of this
     * method and logs it afterwards.
     * The rewrapped exception is only logged if the given {@code logFilter} predicate allows the exception and the
     * class of it is not in the {@code ignoredThrowableTypes}.
     *
     * @param logFilter The predicate the unwrapped exception is tested against.
     * @param ignoredThrowableTypes The throwable types that should never be logged.
     * @return A consumer which logs the given throwable.
     */
    @SafeVarargs
    public static Consumer<Throwable> getConsumer(Predicate<Throwable> logFilter,
                                                  Class<? extends Throwable>... ignoredThrowableTypes) {
        return get(logFilter, ignoredThrowableTypes)::apply;
    }

    /**
     * Returns a consumer that can for example be used in the {@link TextChannel#typeContinuously(Consumer)} method.
     * It unwraps {@link CompletionException CompletionExceptions},
     * {@link InvocationTargetException InvocationTargetExceptions} and {@link ExecutionException ExecutionExceptions}
     * first, and then adds a fresh {@code CompletionException} as wrapper with the stacktrace of the caller of this
     * method and logs it afterwards.
     * The rewrapped exception is only logged if it is not in the {@code ignoredThrowableTypes}.
     *
     * @param ignoredThrowableTypes The throwable types that should never be logged.
     * @return A consumer which logs the given throwable.
     */
    @SafeVarargs
    public static Consumer<Throwable> getConsumer(Class<? extends Throwable>... ignoredThrowableTypes) {
        return getConsumer(null, ignoredThrowableTypes);
    }

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
     * @return A function which logs the given throwable and returns {@code null}.
     */
    @SafeVarargs
    public static <T> Function<Throwable, T> get(
            Predicate<Throwable> logFilter, Class<? extends Throwable>... ignoredThrowableTypes) {
        StackTraceElement[] stackTrace = Arrays.stream(Thread.currentThread().getStackTrace())
                .filter(element -> !element.getClassName().equals(ExceptionLogger.class.getName()))
                .filter(element -> !(element.getClassName().equals(Thread.class.getName())
                                     && "getStackTrace".equals(element.getMethodName())))
                .toArray(StackTraceElement[]::new);
        Collection<Class<? extends Throwable>> ignoredThrowableTypesList = Arrays.asList(ignoredThrowableTypes);
        return delegate.get(logFilter, ignoredThrowableTypesList, stackTrace);
    }

    /**
     * Returns a function that can be used in the {@link CompletableFuture#exceptionally(Function)} method.
     * It unwraps {@link CompletionException CompletionExceptions},
     * {@link InvocationTargetException InvocationTargetExceptions} and {@link ExecutionException ExecutionExceptions}
     * first, and then adds a fresh {@code CompletionException} as wrapper with the stacktrace of the caller of this
     * method and logs it afterwards.
     * The rewrapped exception is only logged if it is not in the {@code ignoredThrowableTypes}.
     *
     * @param <T> The return type of the function.
     * @param ignoredThrowableTypes The throwable types that should never be logged.
     * @return A function which logs the given throwable and returns {@code null}.
     */
    @SafeVarargs
    public static <T> Function<Throwable, T> get(Class<? extends Throwable>... ignoredThrowableTypes) {
        return get(null, ignoredThrowableTypes);
    }

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
    public static UncaughtExceptionHandler getUncaughtExceptionHandler() {
        return delegate.getUncaughtExceptionHandler();
    }

    /**
     * Unwraps {@link CompletionException CompletionExceptions},
     * {@link InvocationTargetException InvocationTargetExceptions} and {@link ExecutionException ExecutionExceptions}
     * and returns the actual cause, or the given argument itself if it is not one of the listed exceptions types.
     * If the bottom-most exception to unwrap does not have a cause, it is returned itself instead.
     *
     * @param throwable The throwable to unwrap.
     * @return The unwrapped throwable.
     */
    public static Throwable unwrapThrowable(Throwable throwable) {
        return ExceptionLoggerDelegate.unwrapThrowable(throwable);
    }

}
