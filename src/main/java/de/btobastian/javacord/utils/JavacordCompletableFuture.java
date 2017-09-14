package de.btobastian.javacord.utils;

import de.btobastian.javacord.utils.logging.LoggerUtil;
import org.slf4j.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;

/**
 * Every returned {@link CompletableFuture} is a javacord future.
 */
public class JavacordCompletableFuture<T> extends CompletableFuture<T> {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(JavacordCompletableFuture.class);

    @Override
    public CompletableFuture<T> whenCompleteAsync(BiConsumer<? super T, ? super Throwable> action, Executor executor) {
        CompletableFuture<T> future = super.whenCompleteAsync(action, executor);
        future.whenComplete((res, throwable) -> {
            if (throwable != null) {
                logger.error("Error in #whenCompleteAsync(...) method!", throwable);
            }
        });
        return future;
    }

    @Override
    public CompletableFuture<T> whenCompleteAsync(BiConsumer<? super T, ? super Throwable> action) {
        CompletableFuture<T> future = super.whenCompleteAsync(action);
        future.whenComplete((res, throwable) -> {
            if (throwable != null) {
                logger.error("Error in whenCompleteAsync(...) method!", throwable);
            }
        });
        return future;
    }

    @Override
    public CompletableFuture<T> whenComplete(BiConsumer<? super T, ? super Throwable> action) {
        CompletableFuture<T> future = super.whenComplete(action);
        future.whenComplete((res, throwable) -> {
            if (throwable != null) {
                logger.error("Error in whenComplete(...) method!", throwable);
            }
        });
        return future;
    }
}
