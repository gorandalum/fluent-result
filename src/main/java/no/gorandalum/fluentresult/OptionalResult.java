package no.gorandalum.fluentresult;

import no.gorandalum.fluentresult.internal.Implementations;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class OptionalResult<T, E> extends BaseResult<Optional<T>, E> {

    private OptionalResult(Optional<T> value, E error) {
        super(value, error, OptionalResult.class);
    }

    public static <T, E> OptionalResult<T, E> success(Optional<? extends T> maybeValue) {
        return new OptionalResult<>((Optional<T>)Objects.requireNonNull(maybeValue), null);
    }

    public static <T, E> OptionalResult<T, E> success(T value) {
        return new OptionalResult<>(Optional.of(value), null);
    }

    public static <T, E> OptionalResult<T, E> successNullable(T value) {
        return new OptionalResult<>(Optional.ofNullable(value), null);
    }

    public static <T, E> OptionalResult<T, E> empty() {
        return new OptionalResult<>(Optional.empty(), null);
    }

    public static <T, E> OptionalResult<T, E> error(E error) {
        return new OptionalResult<>(null, Objects.requireNonNull(error));
    }

    public <N> OptionalResult<N, E> mapToOptional(
            Function<Optional<T>, ? extends Optional<? extends N>> function) {
        return (OptionalResult<N, E>) Implementations.map(
                function, OptionalResult::success, OptionalResult::error, this);
    }

    public <N> Result<N, E> map(Function<Optional<T>, ? extends N> function) {
        return Implementations.map(function, Result::success, Result::error, this);
    }

    public <N> BooleanResult<E> mapToBoolean(Function<Optional<T>, Boolean> function) {
        return Implementations.map(
                function,
                BooleanResult::success,
                BooleanResult::error,
                this);
    }

    public <N> OptionalResult<N, E> mapValue(
            Function<? super T, ? extends N> function) {
        return Implementations.map(
                maybeVal -> maybeVal.map(val -> (N) function.apply(val)),
                OptionalResult::success,
                OptionalResult::error,
                this);
    }

    public <N> OptionalResult<N, E> mapValueToOptional(
            Function<? super T, Optional<? extends N>> function) {
        return Implementations.map(
                maybeVal -> maybeVal.flatMap(function),
                OptionalResult::success,
                OptionalResult::error,
                this);
    }

    public <N> OptionalResult<N, E> flatMapToOptionalResult(
            Function<Optional<T>, OptionalResult<? extends N, ? extends E>> function) {
        return (OptionalResult<N, E>)Implementations.flatMap(function, this);
    }

    public <N> Result<N, E> flatMap(
            Function<Optional<T>, Result<? extends N, ? extends E>> function) {
        return (Result<N, E>)Implementations.flatMap(function, this, Result::error);
    }

    public BooleanResult<E> flatMapToBooleanResult(
            Function<Optional<? extends T>, BooleanResult<? extends E>> function) {
        return (BooleanResult<E>)Implementations.flatMap(function, this, BooleanResult::error);
    }

    public VoidResult<E> flatMapToVoidResult(
            Function<Optional<? extends T>, VoidResult<? extends E>> function) {
        return (VoidResult<E>)Implementations.flatMap(function, this, VoidResult::error);
    }

    public <N> OptionalResult<N, E> flatMapValueWithOptionalResult(
            Function<? super T, OptionalResult<? extends N, ? extends E>> function) {
        return (OptionalResult<N, E>)Implementations.flatMap(
                maybeVal -> maybeVal
                        .map(val -> Objects.requireNonNull(function.apply(val)))
                        .orElseGet(OptionalResult::empty),
                this
        );
    }

    public <N> OptionalResult<N, E> flatMapValueWithResult(
            Function<? super T, Result<? extends N, ? extends E>> function) {
        return (OptionalResult<N, E>)Implementations.flatMap(
                maybeVal -> maybeVal
                        .map(val -> Objects.requireNonNull(function.apply(val)))
                        .map(Result::toOptionalResult)
                        .orElseGet(OptionalResult::empty),
                this);
    }

    public OptionalResult<Boolean, E> flatMapValueWithBooleanResult(
            Function<? super T, BooleanResult<? extends E>> function) {
        return (OptionalResult<Boolean, E>)Implementations.flatMap(
                maybeVal -> maybeVal
                        .map(val -> Objects.requireNonNull(function.apply(val)))
                        .map(BooleanResult::toOptionalResult)
                        .orElseGet(OptionalResult::empty),
                this);
    }

    public OptionalResult<T, E> consume(Consumer<Optional<T>> consumer) {
        return Implementations.consume(consumer, this);
    }

    public OptionalResult<T, E> consumeValue(Consumer<T> consumer) {
        Objects.requireNonNull(consumer);
        valueOpt().ifPresent(maybeVal -> maybeVal.ifPresent(consumer));
        return this;
    }

    public OptionalResult<T, E> consumeError(Consumer<? super E> errorConsumer) {
        return Implementations.consumeError(errorConsumer, this);
    }

    public OptionalResult<T, E> consumeEither(
            Consumer<Optional<T>> consumer,
            Consumer<? super E> errorConsumer) {
        Objects.requireNonNull(consumer);
        Objects.requireNonNull(errorConsumer);

        return Implementations.consumeEither(
                consumer,
                errorConsumer,
                this
        );
    }

    public OptionalResult<T, E> consumeEither(
            Consumer<? super T> valueConsumer,
            Runnable emptyRunnable,
            Consumer<? super E> errorConsumer) {
        Objects.requireNonNull(valueConsumer);
        Objects.requireNonNull(emptyRunnable);
        Objects.requireNonNull(errorConsumer);

        return Implementations.consumeEither(
                maybeVal -> maybeVal.ifPresentOrElse(valueConsumer, emptyRunnable),
                errorConsumer,
                this
        );
    }

    public OptionalResult<T, E> runIfSuccess(Runnable runnable) {
        return Implementations.runIfSuccess(runnable, this);
    }

    public OptionalResult<T, E> runIfValue(Runnable runnable) {
        Objects.requireNonNull(runnable);
        valueOpt().ifPresent(maybeVal -> maybeVal.ifPresent(val -> runnable.run()));
        return this;
    }

    public OptionalResult<T, E> runIfEmpty(Runnable runnable) {
        Objects.requireNonNull(runnable);
        valueOpt().ifPresent(maybeVal -> maybeVal.ifPresentOrElse(val -> {}, runnable));
        return this;
    }

    public OptionalResult<T, E> runIfError(Runnable runnable) {
        return Implementations.runIfError(runnable, this);
    }

    public OptionalResult<T, E> runEither(Runnable successRunnable, Runnable errorRunnable) {
        return Implementations.runEither(successRunnable, errorRunnable, this);
    }

    public OptionalResult<T, E> runEither(Runnable valueRunnable,
                                          Runnable emptyRunnable,
                                          Runnable errorRunnable) {
        Objects.requireNonNull(valueRunnable);
        Objects.requireNonNull(emptyRunnable);
        return Implementations.runEither(
                () -> value().ifPresentOrElse(val -> valueRunnable.run(), emptyRunnable),
                errorRunnable, this);
    }

    public OptionalResult<T, E> run(Runnable runnable) {
        return Implementations.run(runnable, this);
    }

    public OptionalResult<T, E> verify(Predicate<Optional<T>> predicate,
                                       Supplier<? extends E> errorSupplier) {
        return Implementations.verify(
                predicate,
                errorSupplier,
                OptionalResult::error,
                this);
    }

    public OptionalResult<T, E> verifyValue(Predicate<? super T> verificator,
                                            Supplier<? extends E> errorSupplier) {
        return Implementations.verify(
                maybeValue -> maybeValue.map(verificator::test).orElse(true),
                errorSupplier,
                OptionalResult::error,
                this);
    }

    public <N> N merge(Function<Optional<T>, ? extends N> valueFunction,
                       Function<? super E, ? extends N> errorFunction) {
        return Implementations.merge(valueFunction, errorFunction, this);
    }

    public <N> N merge(Function<? super T, ? extends N> valueFunction,
                       Supplier<? extends N> emptySupplier,
                       Function<? super E, ? extends N> errorFunction) {
        Objects.requireNonNull(valueFunction);
        Objects.requireNonNull(emptySupplier);
        Objects.requireNonNull(errorFunction);
        return Implementations.merge(
                maybeVal -> maybeVal.isPresent() ?
                        valueFunction.apply(maybeVal.get()) :
                        emptySupplier.get(),
                errorFunction,
                this);
    }

    public Optional<T> orElse(Optional<T> other) {
        return Implementations.orElse(other, this);
    }

    public T valueOrElse(T other) {
        return valueOpt().flatMap(Function.identity()).orElse(other);
    }

    public Optional<T> orElseGet(Function<? super E, ? extends Optional<T>> function) {
        return Implementations.orElseGet(function, this);
    }

    public T valueOrElseGet(Supplier<? extends T> supplier) {
        Objects.requireNonNull(supplier);
        return valueOpt().flatMap(Function.identity()).orElseGet(supplier);
    }

    public <X extends Throwable> Optional<T> orElseThrow(
            Function<? super E, ? extends X> function) throws X {
        return Implementations.orElseThrow(function, this);
    }

    public <X extends Throwable> T valueOrElseThrow(
            Supplier<? extends X> supplier) throws X {
        Objects.requireNonNull(supplier);
        return valueOpt().flatMap(Function.identity()).orElseThrow(supplier);
    }

    public VoidResult<E> toVoidResult() {
        return errorOpt()
                .map(VoidResult::error)
                .orElseGet(VoidResult::success);
    }

    public Result<T, E> toResult(Supplier<? extends E> errorSupplier) {
        Objects.requireNonNull(errorSupplier);
        return merge(Result::success, () -> Result.error(errorSupplier.get()), Result::error);
    }


}

