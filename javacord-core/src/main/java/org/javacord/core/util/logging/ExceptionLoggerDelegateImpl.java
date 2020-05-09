package org.javacord.core.util.logging;

import org.apache.logging.log4j.Logger;
import org.javacord.api.util.logging.internal.ExceptionLoggerDelegate;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Collection;
import java.util.concurrent.CompletionException;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * The implementation of {@link ExceptionLoggerDelegate}.
 */
public class ExceptionLoggerDelegateImpl implements ExceptionLoggerDelegate {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(ExceptionLoggerDelegateImpl.class);

    @Override
    public <T> Function<Throwable, T> get(Predicate<Throwable> logFilter,
                                          Collection<Class<? extends Throwable>> ignoredThrowableTypes,
                                          StackTraceElement[] stackTrace) {
        return throwable -> {
            Throwable unwrappedThrowable = ExceptionLoggerDelegate.unwrapThrowable(throwable);
            if (ignoredThrowableTypes.contains(unwrappedThrowable.getClass())
                    || ((logFilter != null) && !logFilter.test(unwrappedThrowable))) {
                logger.debug("Suppressed exception {}", throwable.getMessage());
            } else {
                Throwable enrichedThrowable = new CompletionException(unwrappedThrowable.getMessage(),
                                                                      unwrappedThrowable);
                enrichedThrowable.setStackTrace(stackTrace);
                logger.error("Caught unhandled exception!", enrichedThrowable);
            }
            return null;
        };
    }

    @Override
    public UncaughtExceptionHandler getUncaughtExceptionHandler() {
        return (thread, throwable) -> logger.error(
                "Caught unhandled exception on thread '{}'!",
                thread.getName(),
                ExceptionLoggerDelegate.unwrapThrowable(throwable));
    }

}
