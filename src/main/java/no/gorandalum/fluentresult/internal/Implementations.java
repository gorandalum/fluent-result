package no.gorandalum.fluentresult.internal;

import no.gorandalum.fluentresult.BaseResult;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class Implementations {

    private Implementations() {}

    public static <T, E, R extends BaseResult<T, E>> T orElse(T other, R instance) {
        return instance.errorOpt().map(err -> other).orElse(instance.value());
    }

    public static <T, E, R extends BaseResult<T, E>> T orElseGet(
            Function<? super E, ? extends T> function, R instance) {
        Objects.requireNonNull(function);
        return instance.errorOpt().map(err -> (T) function.apply(err)).orElse(instance.value());
    }

    public static <T, E, R extends BaseResult<T, E>, X extends Throwable> T orElseThrow(
            Function<? super E, ? extends X> function, R instance) throws X {
        Objects.requireNonNull(function);
        if (instance.error() == null) {
            return instance.value();
        }
        throw function.apply(instance.error());
    }

    public static <T, E, N, R extends BaseResult<T, E>> N merge(
            Function<? super T, ? extends N> valueFunction,
            Function<? super E, ? extends N> errorFunction,
            R instance) {
        Objects.requireNonNull(valueFunction);
        Objects.requireNonNull(errorFunction);
        return instance.isSuccess() ?
                valueFunction.apply(instance.value()) : errorFunction.apply(instance.error());
    }

    public static <T, E, R extends BaseResult<T, E>> R consume(Consumer<? super T> consumer,
                                                        R instance) {
        Objects.requireNonNull(consumer);
        if (instance.isSuccess()) {
            consumer.accept(instance.value());
        }
        return instance;
    }

    public static <T, E, R extends BaseResult<T, E>> R consumeError(Consumer<? super E> consumer,
                                                             R instance) {
        Objects.requireNonNull(consumer);
        if (!instance.isSuccess()) {
            consumer.accept(instance.error());
        }
        return instance;
    }

    public static <T, E, R extends BaseResult<T, E>> R consumeEither(Consumer<? super T> valueConsumer,
                                                              Consumer<? super E> errorConsumer,
                                                              R instance) {
        Objects.requireNonNull(valueConsumer);
        Objects.requireNonNull(errorConsumer);
        instance.errorOpt().ifPresentOrElse(
                errorConsumer,
                () -> valueConsumer.accept(instance.value())
        );
        return instance;
    }

    public static <T, E, R extends BaseResult<T, E>> R runIfSuccess(Runnable runnable,
                                                             R instance) {
        Objects.requireNonNull(runnable);
        instance.errorOpt().ifPresentOrElse(
                err -> {},
                runnable);
        return instance;
    }

    public static <T, E, R extends BaseResult<T, E>> R runIfError(Runnable runnable,
                                                           R instance) {
        Objects.requireNonNull(runnable);
        instance.errorOpt().ifPresent(err -> runnable.run());
        return instance;
    }

    public static <T, E, R extends BaseResult<T, E>> R runEither(Runnable successRunnable,
                                                          Runnable errorRunnable,
                                                          R instance) {
        Objects.requireNonNull(successRunnable);
        Objects.requireNonNull(errorRunnable);
        instance.errorOpt().ifPresentOrElse(
                err -> errorRunnable.run(),
                successRunnable
        );
        return instance;
    }

    public static <T, E, R extends BaseResult<T, E>> R run(Runnable runnable, R instance) {
        Objects.requireNonNull(runnable);
        runnable.run();
        return instance;
    }

    public static <T, E, R extends BaseResult<T, E>> R verify(Predicate<? super T> verificator,
                                                       Supplier<? extends E> errorSupplier,
                                                       Function<E, R> errorConstructor,
                                                       R instance) {
        Objects.requireNonNull(verificator);
        Objects.requireNonNull(errorSupplier);
        return instance.errorOpt()
                .map(err -> instance)
                .orElseGet(() -> verificator.test(instance.value()) ?
                        instance : errorConstructor.apply(errorSupplier.get()));
    }

    public static <T, E, N, NR, R extends BaseResult<T, E>> NR map(
            Function<? super T, ? extends N> function,
            Function<? super N, NR> successConstructor,
            Function<? super E, NR> errorConstructor,
            R instance) {
        Objects.requireNonNull(function);
        return instance.isSuccess() ?
                successConstructor.apply(function.apply(instance.value())) :
                errorConstructor.apply(instance.error());
    }

    public static <T, E, N, NR extends BaseResult<? extends N, ? extends E>, R extends BaseResult<T, E>> NR flatMap(
            Function<? super T, ? extends NR> function, R instance) {
        Objects.requireNonNull(function);
        return flatMap(function, instance, err -> (NR)instance);
    }

    public static <T, E, N, NR extends BaseResult<? extends N, ? extends E>, R extends BaseResult<T, E>> NR flatMap(
            Function<? super T, ? extends NR> function, R instance, Function<? super E, ? extends NR> errorConstructor) {
        Objects.requireNonNull(function);
        return instance.isSuccess() ?
                Objects.requireNonNull(function.apply(instance.value())) :
                errorConstructor.apply(instance.error());
    }
}

