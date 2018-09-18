package no.gorandalum.fluentresult;

import no.gorandalum.fluentresult.internal.Implementations;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class Result<T, E> extends BaseResult<T, E> {

    private Result(T value, E error) {
        super(value, error, Result.class);
    }

    public static <T, E> Result<T, E> success(T value) {
        return new Result<>(Objects.requireNonNull(value), null);
    }

    public static <T, E> Result<T, E> error(E error) {
        return new Result<>(null, Objects.requireNonNull(error));
    }

    public <N> Result<N, E> map(Function<? super T, ? extends N> function) {
        return Implementations.map(function, Result::success, Result::error, this);
    }

    public <N> OptionalResult<N, E> mapToOptional(
            Function<? super T, Optional<N>> function) {
        return Implementations.map(
                function,
                OptionalResult::success,
                OptionalResult::error,
                this);
    }

    public BooleanResult<E> mapToBoolean(Function<? super T, Boolean> function) {
        return Implementations.map(
                function,
                BooleanResult::success,
                BooleanResult::error,
                this);
    }

    public <N> Result<N, E> flatMap(Function<? super T, ? extends Result<? extends N, ? extends E>> function) {
        return (Result<N, E>)Implementations.flatMap(function, this);
    }

    public <N> OptionalResult<N, E> flatMapToOptional(
            Function<? super T, OptionalResult<N, E>> function) {
        return Implementations.flatMap(function, this, OptionalResult::error);
    }

    public <N> BooleanResult<E> flatMapToBoolean(
            Function<? super T, BooleanResult<E>> function) {
        return Implementations.flatMap(function, this, BooleanResult::error);
    }

    public <N> VoidResult<E> flatMapToVoid(
            Function<? super T, VoidResult<E>> function) {
        return Implementations.flatMap(function, this, VoidResult::error);
    }

    public Result<T, E> consume(Consumer<? super T> consumer) {
        return Implementations.consume(consumer, this);
    }

    public Result<T, E> consumeError(Consumer<? super E> errorConsumer) {
        return Implementations.consumeError(errorConsumer, this);
    }

    public Result<T, E> consumeEither(Consumer<? super T> valueConsumer,
                                      Consumer<? super E> errorConsumer) {
        return Implementations.consumeEither(valueConsumer, errorConsumer, this);
    }

    public Result<T, E> runIfSuccess(Runnable runnable) {
        return Implementations.runIfSuccess(runnable, this);
    }

    public Result<T, E> runIfError(Runnable runnable) {
        return Implementations.runIfError(runnable, this);
    }

    public Result<T, E> runEither(Runnable successRunnable, Runnable errorRunnable) {
        return Implementations.runEither(successRunnable, errorRunnable, this);
    }

    public Result<T, E> run(Runnable runnable) {
        return Implementations.run(runnable, this);
    }

    public Result<T, E> verify(Predicate<? super T> predicate,
                               Supplier<? extends E> errorSupplier) {
        return Implementations.verify(predicate, errorSupplier, Result::error, this);
    }

    public <N> N merge(Function<? super T, ? extends N> valueFunction,
                       Function<? super E, ? extends N> errorFunction) {
        return Implementations.merge(valueFunction, errorFunction, this);
    }

    public T orElse(T other) {
        return Implementations.orElse(other, this);
    }

    public T orElseGet(Function<? super E, ? extends T> function) {
        return Implementations.orElseGet(function, this);
    }

    public <X extends Throwable> T orElseThrow(
            Function<? super E, ? extends X> function) throws X {
        return Implementations.orElseThrow(function, this);
    }

    public OptionalResult<T, E> toOptionalResult() {
        return errorOpt()
                .map(OptionalResult::<T, E>error)
                .orElseGet(() -> OptionalResult.success(value()));
    }

    public VoidResult<E> toVoidResult() {
        return errorOpt()
                .map(VoidResult::error)
                .orElseGet(VoidResult::success);
    }
}

