package me.blueslime.minedis.utils.task;

import me.blueslime.minedis.Minedis;
import me.blueslime.minedis.api.MinedisAPI;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TaskExecutor {
    public static <T> void execute(final CompletableFuture<T> future, final BiConsumer<T, Throwable> consumer) {
        future.whenComplete((value, exception) -> {
            Minedis minedis = MinedisAPI.get().getPlugin();

            if (minedis == null) {
                return;
            }

            consumer.accept(value, exception);
        });
    }

    public static <T> Collector<CompletableFuture<T>, ?, CompletableFuture<List<T>>> collect() {
        return Collectors.collectingAndThen(Collectors.toList(), TaskExecutor::fromCollection);
    }


    public static <T> CompletableFuture<List<T>> fromStream(final Stream<CompletableFuture<T>> futures) {
        return fromCollection(futures.collect(Collectors.toList()));
    }

    public static <T> CompletableFuture<List<T>> fromCollection(final Collection<CompletableFuture<T>> futures) {
        return CompletableFuture.allOf(
            futures.toArray(
                    new CompletableFuture[0]
            )
        ).thenApplyAsync(
            $ -> awaitCompletion(futures)
        );
    }

    private static <T> List<T> awaitCompletion(final Collection<CompletableFuture<T>> futures) {
        return futures.stream().map(CompletableFuture::join).collect(Collectors.toList());
    }
}
