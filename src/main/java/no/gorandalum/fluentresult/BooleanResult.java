package no.gorandalum.fluentresult;

import no.gorandalum.fluentresult.internal.Implementations;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class BooleanResult<E> extends BaseResult<Boolean, E> {

    private BooleanResult(Boolean value, E error) {
        super(value, error, BooleanResult.class);
    }

    public static <E> BooleanResult<E> success(boolean value) {
        return new BooleanResult<>(value, null);
    }

    public static <E> BooleanResult<E> successTrue() {
        return new BooleanResult<>(true, null);
    }

    public static <E> BooleanResult<E> successFalse() {
        return new BooleanResult<>(false, null);
    }

    public static <E> BooleanResult<E> error(E error) {
        return new BooleanResult<>(null, Objects.requireNonNull(error));
    }

    public BooleanResult<E> mapToBoolean(
            Function<Boolean, Boolean> function) {
        return Implementations.map(
                function, BooleanResult::success, BooleanResult::error, this);
    }

    public <N> Result<N, E> map(Function<Boolean, ? extends N> function) {
        return Implementations.map(function, Result::success, Result::error, this);
    }

    public <N> OptionalResult<N, E> mapToOptional(
            Function<Boolean, ? extends Optional<? extends N>> function) {
        return (OptionalResult<N, E>) Implementations.map(
                function,
                OptionalResult::success,
                OptionalResult::error,
                this);
    }

    public BooleanResult<E> flatMapToBoolean(
            Function<Boolean, BooleanResult<? extends E>> function) {
        return (BooleanResult<E>) Implementations.flatMap(function, this);
    }

    public <N> Result<N, E> flatMap(
            Function<Boolean, Result<? extends N, ? extends E>> function) {
        return (Result<N, E>) Implementations.flatMap(function, this, Result::error);
    }

    public <N> OptionalResult<N, E> flatMapToOptionalResult(
            Function<Boolean, OptionalResult<? extends N, ? extends E>> function) {
        return (OptionalResult<N, E>) Implementations.flatMap(function, this, OptionalResult::error);
    }

    public <N> VoidResult<E> flatMapToVoidResult(
            Function<Boolean, VoidResult<? extends E>> function) {
        return (VoidResult<E>) Implementations.flatMap(function, this, VoidResult::error);
    }

    public BooleanResult<E> consume(Consumer<Boolean> consumer) {
        return Implementations.consume(consumer, this);
    }

    public BooleanResult<E> consumeError(Consumer<? super E> errorConsumer) {
        return Implementations.consumeError(errorConsumer, this);
    }

    public BooleanResult<E> consumeEither(
            Consumer<Boolean> valueConsumer,
            Consumer<? super E> errorConsumer) {
        Objects.requireNonNull(valueConsumer);
        Objects.requireNonNull(errorConsumer);

        return Implementations.consumeEither(
                valueConsumer,
                errorConsumer,
                this);
    }

    public BooleanResult<E> consumeEither(
            Runnable trueRunnable,
            Runnable falseRunnable,
            Consumer<? super E> errorConsumer) {
        Objects.requireNonNull(trueRunnable);
        Objects.requireNonNull(falseRunnable);

        return Implementations.consumeEither(
                val -> {
                    if (val) {
                        trueRunnable.run();
                    } else {
                        falseRunnable.run();
                    }

                },
                errorConsumer,
                this);
    }

    public BooleanResult<E> runIfSuccess(Runnable runnable) {
        return Implementations.runIfSuccess(runnable, this);
    }

    public BooleanResult<E> runIfTrue(Runnable runnable) {
        Objects.requireNonNull(runnable);
        return Implementations.runIfSuccess(
                () -> {
                    if (value()) runnable.run();
                },
                this);
    }

    public BooleanResult<E> runIfFalse(Runnable runnable) {
        Objects.requireNonNull(runnable);
        return Implementations.runIfSuccess(
                () -> {
                    if (!value()) runnable.run();
                },
                this);
    }

    public BooleanResult<E> runIfError(Runnable runnable) {
        return Implementations.runIfError(runnable, this);
    }

    public BooleanResult<E> runEither(Runnable successRunnable, Runnable errorRunnable) {
        return Implementations.runEither(successRunnable, errorRunnable, this);
    }

    public BooleanResult<E> runEither(Runnable trueRunnable,
                                      Runnable falseRunnable,
                                      Runnable errorRunnable) {
        Objects.requireNonNull(trueRunnable);
        Objects.requireNonNull(falseRunnable);
        return Implementations.runEither(
                () -> {
                    if (value()) {
                        trueRunnable.run();
                    } else {
                        falseRunnable.run();
                    }
                },
                errorRunnable, this);
    }

    public BooleanResult<E> run(Runnable runnable) {
        return Implementations.run(runnable, this);
    }

    public BooleanResult<E> verify(Predicate<Boolean> predicate,
                                   Supplier<? extends E> errorSupplier) {
        return Implementations.verify(
                predicate,
                errorSupplier,
                BooleanResult::error,
                this);
    }

    public <N> N merge(Function<Boolean, ? extends N> valueFunction,
                       Function<? super E, ? extends N> errorFunction) {
        return Implementations.merge(valueFunction, errorFunction, this);
    }

    public <N> N merge(Supplier<? extends N> trueSupplier,
                       Supplier<? extends N> falseSupplier,
                       Function<? super E, ? extends N> errorFunction) {
        Objects.requireNonNull(trueSupplier);
        Objects.requireNonNull(falseSupplier);
        Objects.requireNonNull(errorFunction);
        return Implementations.merge(
                val -> val ? trueSupplier.get() : falseSupplier.get(),
                errorFunction,
                this);
    }

    public Boolean orElse(Boolean other) {
        return Implementations.orElse(other, this);
    }

    public boolean orElseTrue() {
        return Implementations.orElse(true, this);
    }

    public boolean orElseFalse() {
        return Implementations.orElse(false, this);
    }

    public Boolean orElseGet(Function<? super E, Boolean> function) {
        return Implementations.orElseGet(function, this);
    }

    public <X extends Throwable> Boolean orElseThrow(
            Function<? super E, ? extends X> function) throws X {
        return Implementations.orElseThrow(function, this);
    }

    public OptionalResult<Boolean, E> toOptionalResult() {
        return errorOpt()
                .map(OptionalResult::<Boolean, E>error)
                .orElseGet(() -> OptionalResult.success(value()));
    }

    public VoidResult<E> toVoidResult() {
        return errorOpt()
                .map(VoidResult::error)
                .orElseGet(VoidResult::success);
    }
}

