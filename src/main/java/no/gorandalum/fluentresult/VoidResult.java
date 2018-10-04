package no.gorandalum.fluentresult;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A result object which either is in success state with no value, or in error
 * state containing a non-{@code null} error value.
 * <p>
 * A variable whose type is {@code VoidResult} should never itself be
 * {@code null}, it should always point to an {@code VoidResult} instance.
 *
 * @param <E> the type of the error value
 */
@SuppressWarnings("WeakerAccess")
public final class VoidResult<E> extends BaseResult<Void, E> {

    /**
     * Common instance for success {@code VoidResult}.
     */
    private static final VoidResult<?> RESULT_SUCCESS =
            new VoidResult<>(null);

    private VoidResult(E error) {
        super(null, error, VoidResult.class);
    }

    /**
     * Returns a {@code VoidResult} in success state.
     *
     * @param <E> the type of the error value
     * @return a {@code VoidResult} in success state
     */
    public static <E> VoidResult<E> success() {
        @SuppressWarnings("unchecked")
        VoidResult<E> res = (VoidResult<E>)RESULT_SUCCESS;
        return res;
    }

    /**
     * Returns a {@code VoidResult} in error state containing the given
     * non-{@code null} value as error value.
     *
     * @param value the error value, which must be non-{@code null}
     * @param <E> the type of the error value
     * @return a {@code VoidResult} in error state containing the given error
     * value
     * @throws NullPointerException if given error value is {@code null}
     */
    public static <E> VoidResult<E> error(E value) {
        return new VoidResult<>(Objects.requireNonNull(value));
    }

    /**
     * If in error state, returns a {@code VoidResult} containing the result of
     * applying the given mapping function to the error value, otherwise returns
     * the unaltered {@code VoidResult} in success state.
     *
     * @param function the mapping function to apply to the error value, if
     * error state
     * @param <N> the type of the value returned from the mapping function
     * @return a {@code VoidResult} containing the result of applying the
     * mapping function to the error value of this {@code VoidResult}, if in
     * error state, otherwise the unaltered {@code VoidResult} in success state
     * @throws NullPointerException if the given mapping function is
     * {@code null} or returns {@code null}
     */
    public <N> VoidResult<N> mapError(Function<? super E, ? extends N> function) {
        return Implementations.mapError(function, VoidResult::error, this);
    }

    /**
     * If in success state, returns a {@code Result} containing the value
     * provided by the given supplier, otherwise returns a {@code Result}
     * containing the error value of this {@code VoidResult}.
     *
     * @param supplier the supplier to provide the value if success state, may
     * not be {@code null}
     * @param <N> the type of the value provided by the supplier
     * @return a {@code Result} containing the value provided by the given
     * supplier, if in success state, otherwise a {@code Result} containing the
     * error value of this {@code VoidResult}
     * @throws NullPointerException if the given supplier is {@code null} or
     * returns {@code null}
     */
    public <N> Result<N, E> replace(Supplier<? extends N> supplier) {
        return Implementations.map(
                val -> supplier.get(),
                Result::success,
                Result::error,
                this);
    }

    /**
     * If in success state, returns a {@code OptionalResult} containing the
     * optional value provided by given supplier, otherwise returns a
     * {@code OptionalResult} containing the error value of this
     * {@code VoidResult}.
     *
     * @param supplier the supplier to provide the optional value if success
     * state, may not be {@code null}
     * @param <N> the type of the value which may be present in the
     * {@code Optional} provided by the supplier
     * @return a {@code OptionalResult} containing the optional value provided
     * by the given supplier, if in success state, otherwise a
     * {@code OptionalResult} containing the error value of this
     * {@code VoidResult}
     * @throws NullPointerException if the given supplier is {@code null} or
     * returns {@code null}
     */
    public <N> OptionalResult<N, E> replaceWithOptional(
            Supplier<Optional<? extends N>> supplier) {
        return Implementations.map(
                val -> supplier.get(),
                OptionalResult::success,
                OptionalResult::error,
                this);
    }

    /**
     * If in success state, returns a {@code BooleanResult} containing the
     * boolean value provided by the given supplier, otherwise returns a
     * {@code BooleanResult} containing the error value of this
     * {@code VoidResult}.
     *
     * @param supplier the supplier to provide the boolean value if success
     * state, may not be {@code null}
     * @return a {@code BooleanResult} containing the boolean value provided by
     * the given supplier, if in success state, otherwise a
     * {@code BooleanResult} containing the error value of this
     * {@code VoidResult}
     * @throws NullPointerException if the given supplier is {@code null} or
     * returns {@code null}
     */
    public BooleanResult<E> replaceWithBoolean(Supplier<Boolean> supplier) {
        return Implementations.map(
                val -> supplier.get(),
                BooleanResult::success,
                BooleanResult::error,
                this);
    }

    /**
     * If in success state, returns the {@code Result} provided by the given
     * supplier, otherwise returns a {@code Result} containing the error value
     * of this {@code VoidResult}.
     *
     * @param <N> the type of success value which may be present in the
     * {@code Result} provided by the supplier
     * @param supplier the supplier to provide the {@code Result}, if success
     * state
     * @return the {@code Result} provided by the supplier, if in success state,
     * otherwise a {@code Result} containing the error value of this
     * {@code VoidResult}
     * @throws NullPointerException if the given supplier is {@code null} or
     * returns {@code null}
     */
    public <N> Result<N, E> flatReplace(
            Supplier<Result<? extends N, ? extends E>> supplier) {
        @SuppressWarnings("unchecked")
        Result<N, E> res = (Result<N, E>) Implementations.flatMap(
                val -> supplier.get(),
                this,
                Result::error);
        return res;
    }

    /**
     * If in success state, returns the {@code OptionalResult} provided by the
     * given supplier, otherwise returns an {@code OptionalResult} containing
     * the error value of this {@code VoidResult}.
     *
     * @param <N> the type of success value which may be present in the
     * {@code OptionalResult} provided by the supplier
     * @param supplier the supplier to provide the {@code OptionalResult}, if
     * success state
     * @return the {@code OptionalResult} provided by the supplier, if in
     * success state, otherwise an {@code OptionalResult} containing the error
     * value of this {@code VoidResult}
     * @throws NullPointerException if the given supplier is {@code null} or
     * returns {@code null}
     */
    public <N> OptionalResult<N, E> flatReplaceToOptionalResult(
            Supplier<OptionalResult<? extends N, ? extends E>> supplier) {
        @SuppressWarnings("unchecked")
        OptionalResult<N, E> res = (OptionalResult<N, E>) Implementations.flatMap(
                val -> supplier.get(),
                this,
                OptionalResult::error);
        return res;
    }

    /**
     * If in success state, returns the {@code BooleanResult} provided by the
     * given supplier, otherwise returns a {@code BooleanResult} containing the
     * error value of this {@code VoidResult}.
     *
     * @param supplier the supplier to provide the {@code BooleanResult}, if
     * success state
     * @return the {@code BooleanResult} provided by the supplier, if in
     * success state, otherwise a {@code BooleanResult} containing the error
     * value of this {@code VoidResult}
     * @throws NullPointerException if the given supplier is {@code null} or
     * returns {@code null}
     */
    public BooleanResult<E> flatReplaceToBooleanResult(
            Supplier<BooleanResult<? extends E>> supplier) {
        @SuppressWarnings("unchecked")
        BooleanResult<E> res = (BooleanResult<E>) Implementations.flatMap(
                val -> supplier.get(),
                this,
                BooleanResult::error);
        return res;
    }

    /**
     * If in success state, returns the {@code VoidResult} provided by the given
     * supplier, otherwise returns the unaltered {@code VoidResult} in error
     * state.
     *
     * @param supplier the supplier to provide the {@code VoidResult}, if
     * success state
     * @return the {@code VoidResult} provided by the supplier, if in success
     * state, otherwise the unaltered {@code VoidResult} in error state
     * @throws NullPointerException if the given mapping function is
     * {@code null} or returns {@code null}
     */
    public VoidResult<E> flatReplaceToVoidResult(Supplier<VoidResult<? extends E>> supplier) {
        @SuppressWarnings("unchecked")
        VoidResult<E> res = (VoidResult<E>) Implementations.flatMap(
                val -> supplier.get(), this);
        return res;
    }

    /**
     * If in error state, applies the error value to the given consumer,
     * otherwise does nothing.
     *
     * @param errorConsumer the consumer which accepts the error value
     * @return the original {@code VoidResult} unaltered
     * @throws NullPointerException if the given consumer is {@code null}
     */
    public VoidResult<E> consumeError(Consumer<? super E> errorConsumer) {
        return Implementations.consumeError(errorConsumer, this);
    }

    /**
     * If in success state, runs the success-runnable. If in error state,
     * applies the error value to the given error-consumer.
     *
     * @param successRunnable the runnable to run if success state
     * @param errorConsumer the consumer which accepts the error value
     * @return the original {@code VoidResult} unaltered
     * @throws NullPointerException if either the given runnable or consumer is
     * {@code null}
     */
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

    /**
     * If in success state, runs the given runnable, otherwise does nothing.
     *
     * @param runnable the runnable to run if success state
     * @return the original {@code VoidResult} unaltered
     * @throws NullPointerException if the given runnable is {@code null}
     */
    public VoidResult<E> runIfSuccess(Runnable runnable) {
        return Implementations.runIfSuccess(runnable, this);
    }

    /**
     * If in error state, runs the given runnable, otherwise does nothing.
     *
     * @param runnable the runnable to run if error state
     * @return the original {@code VoidResult} unaltered
     * @throws NullPointerException if the given runnable is {@code null}
     */
    public VoidResult<E> runIfError(Runnable runnable) {
        return Implementations.runIfError(runnable, this);
    }

    /**
     * If in success state, runs the given success runnable. If in error state,
     * runs the given error runnable.
     *
     * @param successRunnable the runnable to run if success state
     * @param errorRunnable the runnable to run if error state
     * @return the original {@code VoidResult} unaltered
     * @throws NullPointerException if one of the given runnables is {@code null}
     */
    public VoidResult<E> runEither(Runnable successRunnable, Runnable errorRunnable) {
        return Implementations.runEither(successRunnable, errorRunnable, this);
    }

    /**
     * Runs the given runnable, no matter the state.
     *
     * @param runnable the runnable to run
     * @return the original {@code VoidResult} unaltered
     * @throws NullPointerException if the given runnable is {@code null}
     */
    public VoidResult<E> run(Runnable runnable) {
        return Implementations.run(runnable, this);
    }

    /**
     * Retrieve a value from this {@code VoidResult} by folding the states. If
     * in success state, return the value provided by the value-supplier. If in
     * error state, return the value of applying the error-function to the error
     * value.
     *
     * @param <N> the type of retrieved value
     * @param valueSupplier supplier to provide the value, if success state, may
     * return {@code null}
     * @param errorFunction the mapping function to apply to the error value, if
     * error state, may return {@code null}
     * @return the folded value mapped from either the success value or error
     * value, may be {@code null}
     * @throws NullPointerException if either the given supplier or function is
     * {@code null}
     */
    public <N> N fold(Supplier<? extends N> valueSupplier,
                      Function<? super E, ? extends N> errorFunction) {
        Objects.requireNonNull(valueSupplier);
        Objects.requireNonNull(errorFunction);
        return Implementations.fold(
                val -> valueSupplier.get(),
                errorFunction,
                this);
    }

    /**
     * If in success state, does nothing, otherwise throws the exception returned
     * by the given function.
     *
     * @param <X> type of the exception to be thrown
     * @param function the mapping function producing an exception by applying
     * the error value, if not in success state
     * @throws X if in error state
     * @throws NullPointerException if the given function is {@code null} or
     * returns {@code null}
     */
    public <X extends Throwable> void orElseThrow(
            Function<? super E, ? extends X> function) throws X {
        Implementations.orElseThrow(function, this);
    }

    /**
     * Transforms this {@code VoidResult} to an {@code OptionalResult}. If in
     * success state, the {@code OptionalResult} will be in empty success state.
     * If in error state, the {@code OptionalResult} will be in error state
     * containing the error value from this {@code VoidResult}.
     * <p>
     * The returned {@code OptionalResult} will never have a value.
     *
     * @param <N> the type of the success value in the returned
     * {@code OptionalResult}, inferred from the variable
     * @return an {@code OptionalResult} in empty success state or in error
     * state containing the error value from this {@code VoidResult}
     */
    public <N> OptionalResult<N, E> toOptionalResult() {
        return errorOpt()
                .map(OptionalResult::<N, E>error)
                .orElseGet(OptionalResult::empty);
    }
}

