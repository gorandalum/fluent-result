package no.gorandalum.fluentresult;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

final class Implementations {

    private Implementations() {}

    static <T, E, R extends BaseResult<T, E>> T orElse(T other, R instance) {
        return instance.errorOpt().map(err -> other).orElse(instance.value());
    }

    static <T, E, R extends BaseResult<T, E>> T orElseGet(
            Function<? super E, ? extends T> function, R instance) {
        Objects.requireNonNull(function);
        return instance.errorOpt().map(err -> (T) function.apply(err)).orElse(instance.value());
    }

    static <T, E, R extends BaseResult<T, E>, X extends Throwable> T orElseThrow(
            Function<? super E, ? extends X> function, R instance) throws X {
        Objects.requireNonNull(function);
        if (instance.error() == null) {
            return instance.value();
        }
        throw function.apply(instance.error());
    }

    static <T, E, N, R extends BaseResult<T, E>> N fold(
            Function<? super T, ? extends N> valueFunction,
            Function<? super E, ? extends N> errorFunction,
            R instance) {
        Objects.requireNonNull(valueFunction);
        Objects.requireNonNull(errorFunction);
        return instance.isSuccess() ?
                valueFunction.apply(instance.value()) : errorFunction.apply(instance.error());
    }

    static <T, E, R extends BaseResult<T, E>> R consume(Consumer<? super T> consumer,
                                                        R instance) {
        Objects.requireNonNull(consumer);
        if (instance.isSuccess()) {
            consumer.accept(instance.value());
        }
        return instance;
    }

    static <T, E, R extends BaseResult<T, E>> R consumeError(Consumer<? super E> consumer,
                                                             R instance) {
        Objects.requireNonNull(consumer);
        if (!instance.isSuccess()) {
            consumer.accept(instance.error());
        }
        return instance;
    }

    static <T, E, R extends BaseResult<T, E>> R consumeEither(Consumer<? super T> valueConsumer,
                                                              Consumer<? super E> errorConsumer,
                                                              R instance) {
        Objects.requireNonNull(valueConsumer);
        Objects.requireNonNull(errorConsumer);
        if (instance.isSuccess()) {
            valueConsumer.accept(instance.value());
        } else {
            errorConsumer.accept(instance.error());
        }
        return instance;
    }

    static <T, E, R extends BaseResult<T, E>> R runIfSuccess(Runnable runnable,
                                                             R instance) {
        Objects.requireNonNull(runnable);
        if (instance.isSuccess()) {
            runnable.run();
        }
        return instance;
    }

    static <T, E, R extends BaseResult<T, E>> R runIfError(Runnable runnable,
                                                           R instance) {
        Objects.requireNonNull(runnable);
        instance.errorOpt().ifPresent(err -> runnable.run());
        return instance;
    }

    static <T, E, R extends BaseResult<T, E>> R runEither(Runnable successRunnable,
                                                          Runnable errorRunnable,
                                                          R instance) {
        Objects.requireNonNull(successRunnable);
        Objects.requireNonNull(errorRunnable);
        if (instance.isSuccess()) {
            successRunnable.run();
        } else {
            errorRunnable.run();
        }
        return instance;
    }

    static <T, E, R extends BaseResult<T, E>> R runAlways(Runnable runnable, R instance) {
        Objects.requireNonNull(runnable);
        runnable.run();
        return instance;
    }



    static <T, E, R extends BaseResult<T, E>> R flatRunIfSuccess(
            Supplier<? extends BaseResult<Void, ? extends E>> supplier,
            Function<E, R> errorConstructor,
            R instance) {
        Objects.requireNonNull(supplier);
        return instance.errorOpt()
                .map(err -> instance)
                .orElseGet(() -> {
                    BaseResult<Void, ? extends E> res = supplier.get();
                    return res.isSuccess() ? instance : errorConstructor.apply(res.error());
                });
    }

    static <T, E, R extends BaseResult<T, E>> R verify(Predicate<? super T> predicate,
                                                       Supplier<? extends E> errorSupplier,
                                                       Function<E, R> errorConstructor,
                                                       R instance) {
        Objects.requireNonNull(predicate);
        Objects.requireNonNull(errorSupplier);
        return instance.errorOpt()
                .map(err -> instance)
                .orElseGet(() -> predicate.test(instance.value()) ?
                        instance : errorConstructor.apply(errorSupplier.get()));
    }

    static <T, E, R extends BaseResult<T, E>> R flatConsume(
            Function<? super T, ? extends BaseResult<Void, ? extends E>> function,
            Function<E, R> errorConstructor,
            R instance) {
        Objects.requireNonNull(function);
        return instance.errorOpt()
                .map(err -> instance)
                .orElseGet(() -> {
                    BaseResult<Void, ? extends E> res = function.apply(instance.value());
                    return res.isSuccess() ? instance : errorConstructor.apply(res.error());
                });
    }

    static <T, E, N, NR, R extends BaseResult<T, E>> NR map(
            Function<? super T, ? extends N> function,
            Function<? super N, NR> successConstructor,
            Function<? super E, NR> errorConstructor,
            R instance) {
        Objects.requireNonNull(function);
        return instance.isSuccess() ?
                successConstructor.apply(function.apply(instance.value())) :
                errorConstructor.apply(instance.error());
    }

    static <T, E, N, NR, R extends BaseResult<T, E>> NR mapError(
            Function<? super E, ? extends N> function,
            Function<? super N, NR> errorConstructor,
            R instance) {
        Objects.requireNonNull(function);
        if (instance.isSuccess()) {
            @SuppressWarnings("unchecked")
            NR res = (NR) instance;
            return res;
        } else {
            return errorConstructor.apply(function.apply(instance.error()));
        }
    }

    static <T, E, N, NR extends BaseResult<? extends N, ? extends E>, R extends BaseResult<T, E>> NR flatMap(
            Function<? super T, ? extends NR> function, R instance) {
        Objects.requireNonNull(function);
        @SuppressWarnings("unchecked")
        NR res = (NR) instance;
        return flatMap(function, instance, err -> res);
    }

    static <T, E, N, NR extends BaseResult<? extends N, ? extends E>, R extends BaseResult<T, E>> NR flatMap(
            Function<? super T, ? extends NR> function, R instance,
            Function<? super E, ? extends NR> errorConstructor) {
        Objects.requireNonNull(function);
        return instance.isSuccess() ?
                Objects.requireNonNull(function.apply(instance.value())) :
                errorConstructor.apply(instance.error());
    }

    static <T, E, N, NR extends BaseResult<? extends N, ? extends E>, R extends BaseResult<T, E>> NR recover(
            Function<? super E, ? extends NR> function,
            R instance) {
        Objects.requireNonNull(function);

        if (instance.isSuccess()) {
            @SuppressWarnings("unchecked")
            NR res = (NR) instance;
            return res;
        } else {
            return function.apply(instance.error());
        }
    }
}

