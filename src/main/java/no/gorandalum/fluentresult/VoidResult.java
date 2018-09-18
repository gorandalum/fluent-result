package no.gorandalum.fluentresult;

import no.gorandalum.fluentresult.internal.Implementations;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class VoidResult<E> extends BaseResult<Void, E> {

    private VoidResult(E error) {
        super(null, error, VoidResult.class);
    }

    public static <E> VoidResult<E> success() {
        return new VoidResult<>(null);
    }

    public static <E> VoidResult<E> error(E error) {
        return new VoidResult<>(Objects.requireNonNull(error));
    }

    public <N> Result<N, E> replace(Supplier<? extends N> supplier) {
        return Implementations.map(
                val -> supplier.get(),
                Result::success,
                Result::error,
                this);
    }

    public <N> OptionalResult<N, E> replaceWithOptional(
            Supplier<Optional<? extends N>> supplier) {
        return Implementations.map(
                val -> (Optional<N>) supplier.get(),
                OptionalResult::success,
                OptionalResult::error,
                this);
    }

    public BooleanResult<E> replaceWithBoolean(Supplier<Boolean> supplier) {
        return Implementations.map(
                val -> supplier.get(),
                BooleanResult::success,
                BooleanResult::error,
                this);
    }

    public VoidResult<E> flatReplace(Supplier<VoidResult<? extends E>> supplier) {
        return (VoidResult<E>) Implementations.flatMap(val -> supplier.get(), this);
    }

    public <N> Result<N, E> flatReplaceToResult(
            Supplier<Result<? extends N, ? extends E>> supplier) {
        return (Result<N, E>) Implementations.flatMap(
                val -> supplier.get(),
                this,
                Result::error);
    }

    public <N> OptionalResult<N, E> flatReplaceToOptionalResult(
            Supplier<OptionalResult<? extends N, ? extends E>> supplier) {
        return (OptionalResult<N, E>) Implementations.flatMap(
                val -> supplier.get(),
                this,
                OptionalResult::error);
    }

    public BooleanResult<E> flatReplaceToBooleanResult(
            Supplier<BooleanResult<? extends E>> supplier) {
        return (BooleanResult<E>) Implementations.flatMap(
                val -> supplier.get(),
                this,
                BooleanResult::error);
    }

    public VoidResult<E> consumeError(Consumer<? super E> errorConsumer) {
        return Implementations.consumeError(errorConsumer, this);
    }

    public VoidResult<E> consumeEither(
            Runnable successRunnable,
            Consumer<? super E> errorConsumer) {
        Objects.requireNonNull(successRunnable);
        return Implementations.consumeEither(
                val -> successRunnable.run(),
                errorConsumer,
                this
        );
    }

    public VoidResult<E> runIfSuccess(Runnable runnable) {
        return Implementations.runIfSuccess(runnable, this);
    }

    public VoidResult<E> runIfError(Runnable runnable) {
        return Implementations.runIfError(runnable, this);
    }

    public VoidResult<E> runEither(Runnable successRunnable, Runnable errorRunnable) {
        return Implementations.runEither(successRunnable, errorRunnable, this);
    }

    public VoidResult<E> run(Runnable runnable) {
        return Implementations.run(runnable, this);
    }

    public <N> N merge(Supplier<? extends N> valueSupplier,
                       Function<? super E, ? extends N> errorFunction) {
        Objects.requireNonNull(valueSupplier);
        Objects.requireNonNull(errorFunction);
        return Implementations.merge(
                val -> valueSupplier.get(),
                errorFunction,
                this);
    }

    public <X extends Throwable> void orElseThrow(
            Function<? super E, ? extends X> function) throws X {
        Implementations.orElseThrow(function, this);
    }

    public <N> OptionalResult<N, E> toOptionalResult() {
        return errorOpt()
                .map(OptionalResult::<N, E>error)
                .orElseGet(OptionalResult::empty);
    }
}

