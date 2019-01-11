package me.rexlmanu.chromcloudcore.utility.async;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public final class AsyncSession {

    private static AsyncSession instance;
    private final ListeningExecutorService executorService;

    public AsyncSession() {
        this.executorService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(8));
    }

    public void executeAsync(final Runnable runnable) {
        this.executorService.execute(runnable);
    }

    public void scheduleAsync(final Runnable runnable, final Long sleepTime, final TimeUnit timeUnit) {
        this.executorService.execute(() -> {
            try {
                timeUnit.sleep(sleepTime);
                runnable.run();
            } catch (final Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    public void scheduleAsync(final Runnable runnable, final Long initializeTime, final Long sleepTime,
                              final TimeUnit timeUnit) {
        this.executorService.execute(() -> {
            try {
                timeUnit.sleep(initializeTime);
                while (true) {
                    timeUnit.sleep(sleepTime);
                    runnable.run();
                }
            } catch (final Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    public <T> void executeAsync(final T type, final Consumer<T> consumer) {
        this.executorService.execute(() -> consumer.accept(type));
    }

    public <T> void executeAsync(final T type, final Consumer<T> consumer, final Long sleepTime,
                                 final TimeUnit timeUnit) {
        this.executorService.execute(() -> {
            try {
                timeUnit.sleep(sleepTime);
                consumer.accept(type);
            } catch (final Exception exc) {
                exc.printStackTrace();
            }
        });
    }

    public static AsyncSession getInstance() {
        return AsyncSession.instance;
    }

    static {
        AsyncSession.instance = new AsyncSession();
    }
}
