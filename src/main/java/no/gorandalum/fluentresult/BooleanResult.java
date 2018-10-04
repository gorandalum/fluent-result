package no.gorandalum.fluentresult;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * A result object which either is in success state containing a
 * non-{@code null} boolean value, or in error state containing a
 * non-{@code null} error value.
 * <p>
 * A variable whose type is {@code BooleanResult} should never itself be
 * {@code null}, it should always point to an {@code BooleanResult} instance.
 *
 * @param <E> the type of the error value
 */
@SuppressWarnings("WeakerAccess")
public final class BooleanResult<E> extends BaseResult<Boolean, E> {

    /**
     * Common instance for true {@code BooleanResult}.
     */
    private static final BooleanResult<?> RESULT_TRUE =
            new BooleanResult<>(true, null);

    /**
     * Common instance for false {@code BooleanResult}.
     */
    private static final BooleanResult<?> RESULT_FALSE =
            new BooleanResult<>(false, null);

    private BooleanResult(Boolean value, E error) {
        super(value, error, BooleanResult.class);
    }

    /**
     * Returns a {@code BooleanResult} in success state containing the given
     * non-{@code null} boolean value as success value.
     *
     * @param value the boolean success value, which must be non-{@code null}
     * @param <E> the type of the error value
     * @return a {@code BooleanResult} in success state containing the given
     * boolean success value
     * @throws NullPointerException if given success value is {@code null}
     */
    public static <E> BooleanResult<E> success(boolean value) {
        @SuppressWarnings("unchecked")
        BooleanResult<E> res = (BooleanResult<E>)(value ? RESULT_TRUE : RESULT_FALSE);
        return res;
    }

    /**
     * Returns a {@code BooleanResult} in success state containing {@code true}
     * as the the boolean success value.
     *
     * @param <E> the type of the error value
     * @return a {@code BooleanResult} in success state containing {@code true}
     * as the the boolean success value.
     */
    public static <E> BooleanResult<E> successTrue() {
        @SuppressWarnings("unchecked")
        BooleanResult<E> res = (BooleanResult<E>)RESULT_TRUE;
        return res;
    }

    /**
     * Returns a {@code BooleanResult} in success state containing {@code false}
     * as the the boolean success value.
     *
     * @param <E> the type of the error value
     * @return a {@code BooleanResult} in success state containing {@code false}
     * as the the boolean success value.
     */
    public static <E> BooleanResult<E> successFalse() {
        @SuppressWarnings("unchecked")
        BooleanResult<E> res = (BooleanResult<E>)RESULT_FALSE;
        return res;
    }

    /**
     * Returns a {@code BooleanResult} in error state containing the given
     * non-{@code null} value as error value.
     *
     * @param value the error value, which must be non-{@code null}
     * @param <E> the type of the error value
     * @return a {@code BooleanResult} in error state containing the given error
     * value
     * @throws NullPointerException if given error value is {@code null}
     */
    public static <E> BooleanResult<E> error(E value) {
        return new BooleanResult<>(null, Objects.requireNonNull(value));
    }

    /**
     * If in success state, returns a {@code Result} containing the result of
     * applying the given mapping function to the boolean success value,
     * otherwise returns a {@code Result} containing the error value of this
     * {@code BooleanResult}.
     *
     * @param function the mapping function to apply to the boolean success
     * value, if success state
     * @param <N> the type of the value returned from the mapping function
     * @return a {@code Result} containing the result of applying the mapping
     * function to the boolean success value of this {@code BooleanResult}, if
     * in success state, otherwise a {@code Result} containing the error value
     * of this {@code BooleanResult}
     * @throws NullPointerException if the given mapping function is
     * {@code null} or returns {@code null}
     */
    public <N> Result<N, E> map(Function<Boolean, ? extends N> function) {
        return Implementations.map(function, Result::success, Result::error, this);
    }

    /**
     * If in success state, returns a {@code OptionalResult} containing the
     * result of applying the given mapping function to the boolean success
     * value, otherwise returns a {@code OptionalResult} containing the error
     * value of this {@code BooleanResult}.
     *
     * @param function the mapping function to apply to the boolean success
     * value, if success state
     * @param <N> the type of the success value which may be present in the
     * {@code Optional} returned from the mapping function
     * @return a {@code OptionalResult} containing the result of applying the
     * mapping function to the boolean success value of this
     * {@code BooleanResult}, if in success state, otherwise a
     * {@code OptionalResult} containing the error value of this
     * {@code BooleanResult}
     * @throws NullPointerException if the given mapping function is
     * {@code null} or returns {@code null}
     */
    public <N> OptionalResult<N, E> mapToOptional(
            Function<Boolean, ? extends Optional<? extends N>> function) {
        return Implementations.map(
                function,
                OptionalResult::success,
                OptionalResult::error,
                this);
    }

    /**
     * If in success state, returns a {@code BooleanResult} containing the
     * result of applying the given mapping function to the success value,
     * otherwise returns the unaltered {@code BooleanResult} in error state.
     *
     * @param function the mapping function to apply to the boolean success
     * value, if success state
     * @return a {@code BooleanResult} containing the result of applying the
     * mapping function to the boolean success value of this
     * {@code BooleanResult}, if in success state, otherwise the unaltered
     * {@code BooleanResult} in error state
     * @throws NullPointerException if the given mapping function is
     * {@code null} or returns {@code null}
     */
    public BooleanResult<E> mapToBoolean(
            Function<Boolean, Boolean> function) {
        return Implementations.map(
                function, BooleanResult::success, BooleanResult::error, this);
    }

    /**
     * If in error state, returns a {@code BooleanResult} containing the result
     * of applying the given mapping function to the error value, otherwise
     * returns the unaltered {@code BooleanResult} in success state.
     *
     * @param function the mapping function to apply to the error value, if
     * error state
     * @param <N> the type of the value returned from the mapping function
     * @return a {@code BooleanResult} containing the result of applying the
     * mapping function to the error value of this {@code BooleanResult}, if in
     * error state, otherwise the unaltered {@code BooleanResult} in success
     * state
     * @throws NullPointerException if the given mapping function is
     * {@code null} or returns {@code null}
     */
    public <N> BooleanResult<N> mapError(Function<? super E, ? extends N> function) {
        return Implementations.mapError(function, BooleanResult::error, this);
    }

    /**
     * If in success state, returns the {@code Result} from applying the given
     * mapping function to the boolean success value, otherwise returns a
     * {@code Result} containing the error value of this {@code BooleanResult}.
     *
     * @param <N> the type of success value which may be present in the
     * {@code Result} returned by the mapping function
     * @param function the mapping function to apply to the boolean success
     * value, if success state
     * @return the {@code Result} returned from the mapping function, if in
     * success state, otherwise a {@code Result} containing the error
     * value of this {@code BooleanResult}
     * @throws NullPointerException if the given mapping function is
     * {@code null} or returns {@code null}
     */
    public <N> Result<N, E> flatMap(
            Function<Boolean, Result<? extends N, ? extends E>> function) {
        @SuppressWarnings("unchecked")
        Result<N, E> res = (Result<N, E>) Implementations.flatMap(
                function, this, Result::error);
        return res;
    }

    /**
     * If in success state, returns the {@code OptionalResult} from applying
     * the given mapping function to the boolean success value, otherwise
     * returns a {@code OptionalResult} containing the error value of this
     * {@code BooleanResult}.
     *
     * @param <N> the type of success value which may be present in the
     * {@code OptionalResult} returned by the mapping function
     * @param function the mapping function to apply to the boolean success
     * value, if success state
     * @return the {@code OptionalResult} returned from the mapping function, if
     * in success state, otherwise a {@code OptionalResult} containing the error
     * value of this {@code BooleanResult}
     * @throws NullPointerException if the given mapping function is
     * {@code null} or returns {@code null}
     */
    public <N> OptionalResult<N, E> flatMapToOptionalResult(
            Function<Boolean, OptionalResult<? extends N, ? extends E>> function) {
        @SuppressWarnings("unchecked")
        OptionalResult<N, E> res = (OptionalResult<N, E>) Implementations.flatMap(
                function, this, OptionalResult::error);
        return res;
    }

    /**
     * If in success state, returns the {@code BooleanResult} from applying the
     * given mapping function to the boolean success value, otherwise returns
     * the unaltered {@code BooleanResult} in error state.
     *
     * @param function the mapping function to apply to the boolean success
     * value, if success state
     * @return the {@code BooleanResult} returned from the mapping function, if
     * in success state, otherwise the unaltered {@code BooleanResult} in error
     * state
     * @throws NullPointerException if the given mapping function is
     * {@code null} or returns {@code null}
     */
    public BooleanResult<E> flatMapToBooleanResult(
            Function<Boolean, BooleanResult<? extends E>> function) {
        @SuppressWarnings("unchecked")
        BooleanResult<E> res = (BooleanResult<E>) Implementations.flatMap(function, this);
        return res;
    }

    /**
     * If in success state, returns the {@code VoidResult} from applying the
     * given mapping function to the boolean success value, otherwise returns a
     * {@code VoidResult} containing the error value of this
     * {@code BooleanResult}.
     *
     * @param function the mapping function to apply to the boolean success
     * value, if success state
     * @return the {@code VoidResult} returned from the mapping function, if in
     * success state, otherwise a {@code VoidResult} containing the error value
     * of this {@code BooleanResult}
     * @throws NullPointerException if the given mapping function is
     * {@code null} or returns {@code null}
     */
    public VoidResult<E> flatMapToVoidResult(
            Function<Boolean, VoidResult<? extends E>> function) {
        @SuppressWarnings("unchecked")
        VoidResult<E> res = (VoidResult<E>) Implementations.flatMap(
                function, this, VoidResult::error);
        return res;
    }

    /**
     * If in success state, applies the boolean success value to the given
     * consumer, otherwise does nothing.
     *
     * @param consumer the consumer which accepts the boolean success value
     * @return the original {@code BooleanResult} unaltered
     * @throws NullPointerException if the given consumer is {@code null}
     */
    public BooleanResult<E> consume(Consumer<Boolean> consumer) {
        return Implementations.consume(consumer, this);
    }

    /**
     * If in error state, applies the error value to the given consumer,
     * otherwise does nothing.
     *
     * @param errorConsumer the consumer which accepts the error value
     * @return the original {@code BooleanResult} unaltered
     * @throws NullPointerException if the given consumer is {@code null}
     */
    public BooleanResult<E> consumeError(Consumer<? super E> errorConsumer) {
        return Implementations.consumeError(errorConsumer, this);
    }

    /**
     * If in success state, applies the boolean success value to the given value
     * consumer. If in error state, applies the error value to the given error
     * consumer.
     *
     * @param valueConsumer the consumer which accepts the boolean success value
     * @param errorConsumer the consumer which accepts the error value
     * @return the original {@code BooleanResult} unaltered
     * @throws NullPointerException if one of the given consumers is {@code null}
     */
    public BooleanResult<E> consumeEither(
            Consumer<Boolean> valueConsumer,
            Consumer<? super E> errorConsumer) {
        return Implementations.consumeEither(valueConsumer, errorConsumer, this);
    }

    /**
     * If in success state with a success value of {@code true}, runs the given
     * true-runnable. If in success state with a success value of {@code false},
     * runs the given false-runnable. If in error state, applies the error value
     * to the given error consumer.
     *
     * @param trueRunnable the runnable to run if a success value of
     * {@code true}
     * @param falseRunnable the runnable to run if a success value of
     * {@code false}
     * @param errorConsumer the consumer which accepts the error value
     * @return the original {@code BooleanResult} unaltered
     * @throws NullPointerException if one of the given runnables or consumer is
     * {@code null}
     */
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

    /**
     * If in success state, runs the given runnable, otherwise does nothing.
     *
     * @param runnable the runnable to run if success state
     * @return the original {@code BooleanResult} unaltered
     * @throws NullPointerException if the given runnable is {@code null}
     */
    public BooleanResult<E> runIfSuccess(Runnable runnable) {
        return Implementations.runIfSuccess(runnable, this);
    }

    /**
     * If in success state with a success value of {@code true}, runs the given
     * runnable, otherwise does nothing.
     *
     * @param runnable the runnable to run if a success value of {@code true}
     * @return the original {@code BooleanResult} unaltered
     * @throws NullPointerException if the given runnable is {@code null}
     */
    public BooleanResult<E> runIfTrue(Runnable runnable) {
        Objects.requireNonNull(runnable);
        return Implementations.runIfSuccess(
                () -> {
                    if (value()) runnable.run();
                },
                this);
    }

    /**
     * If in success state with a success value of {@code false}, runs the given
     * runnable, otherwise does nothing.
     *
     * @param runnable the runnable to run if a success value of {@code false}
     * @return the original {@code BooleanResult} unaltered
     * @throws NullPointerException if the given runnable is {@code null}
     */
    public BooleanResult<E> runIfFalse(Runnable runnable) {
        Objects.requireNonNull(runnable);
        return Implementations.runIfSuccess(
                () -> {
                    if (!value()) runnable.run();
                },
                this);
    }

    /**
     * If in error state, runs the given runnable, otherwise does nothing.
     *
     * @param runnable the runnable to run if error state
     * @return the original {@code BooleanResult} unaltered
     * @throws NullPointerException if the given runnable is {@code null}
     */
    public BooleanResult<E> runIfError(Runnable runnable) {
        return Implementations.runIfError(runnable, this);
    }

    /**
     * If in success state, runs the given success runnable. If in error state,
     * runs the given error runnable.
     *
     * @param successRunnable the runnable to run if success state
     * @param errorRunnable the runnable to run if error state
     * @return the original {@code BooleanResult} unaltered
     * @throws NullPointerException if one of the given runnables is {@code null}
     */
    public BooleanResult<E> runEither(Runnable successRunnable, Runnable errorRunnable) {
        return Implementations.runEither(successRunnable, errorRunnable, this);
    }

    /**
     * If in success state with a success value of {@code true}, runs the given
     * true-runnable. If in success state with a success value of {@code false},
     * runs the given false-runnable. If in error state, runs the given
     * error-runnable.
     *
     * @param trueRunnable the runnable to run if a success value of
     * {@code true}
     * @param falseRunnable the runnable to run if a success value of
     * {@code false}
     * @param errorRunnable the runnable to run if error state
     * @return the original {@code BooleanResult} unaltered
     * @throws NullPointerException if one of the given runnables is {@code null}
     */
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

    /**
     * Runs the given runnable, no matter the state.
     *
     * @param runnable the runnable to run
     * @return the original {@code BooleanResult} unaltered
     * @throws NullPointerException if the given runnable is {@code null}
     */
    public BooleanResult<E> run(Runnable runnable) {
        return Implementations.run(runnable, this);
    }

    /**
     * If in success state, verifies the boolean success value of this
     * {@code BooleanResult} by testing it with the given predicate. If the
     * predicate evaluates to false, a new {@code BooleanResult} is returned
     * containing the error value provided by the given error supplier. If the
     * predicate evaluates to true, or the {@code BooleanResult} already was in
     * error state, the original {@code BooleanResult} is returned unaltered.
     *
     * @param predicate the predicate used to verify the boolean success value,
     * if success state
     * @param errorSupplier supplier providing the error if predicate evaluates
     * to false
     * @return the original {@code BooleanResult} unaltered, unless the predicate
     * evaluates to false, then a new {@code BooleanResult} in error state is returned
     * containing the supplied error value
     * @throws NullPointerException if the given predicate is {@code null} or
     * returns {@code null}, or the given error supplier is {@code null} or
     * returns {@code null}
     */
    public BooleanResult<E> verify(Predicate<Boolean> predicate,
                                   Supplier<? extends E> errorSupplier) {
        return Implementations.verify(
                predicate,
                errorSupplier,
                BooleanResult::error,
                this);
    }

    /**
     * Retrieve a value from this {@code BooleanResult} by folding the states.
     * If in success state, return the value of applying the value function to
     * the boolean success value. If in error state, return the value of
     * applying the error function to the error value.
     *
     * @param <N> the type of the retrieved value
     * @param valueFunction the mapping function to apply to the boolean success
     * value, if success state, may return {@code null}
     * @param errorFunction the mapping function to apply to the error value, if
     * error state, may return {@code null}
     * @return the folded value mapped from either the success value or error
     * value, may be {@code null}
     * @throws NullPointerException if one of the given functions is
     * {@code null}
     */
    public <N> N fold(Function<Boolean, ? extends N> valueFunction,
                      Function<? super E, ? extends N> errorFunction) {
        return Implementations.fold(valueFunction, errorFunction, this);
    }

    /**
     * Retrieve a value from this {@code BooleanResult} by folding the states.
     * If in success state with a success value of {@code true}, return the
     * value provided by the true-supplier. If in success state with a success
     * value of {@code false}, return the value provided by the false-supplier.
     * If in error state, return the value of applying the error function to
     * the error value.
     *
     * @param <N> the type of the retrieved value
     * @param trueSupplier the supplier to provide the value if a success value
     * of {@code true}, may return {@code null}
     * @param falseSupplier the supplier to provide the value if a success value
     * of {@code false}, may return {@code null}
     * @param errorFunction the mapping function to apply to the error value, if
     * error state, may return {@code null}
     * @return the folded value mapped from either the success value or error
     * value, may be {@code null}
     * @throws NullPointerException if one of the given functions or the
     * supplier is {@code null}
     */
    public <N> N fold(Supplier<? extends N> trueSupplier,
                      Supplier<? extends N> falseSupplier,
                      Function<? super E, ? extends N> errorFunction) {
        Objects.requireNonNull(trueSupplier);
        Objects.requireNonNull(falseSupplier);
        Objects.requireNonNull(errorFunction);
        return Implementations.fold(
                val -> val ? trueSupplier.get() : falseSupplier.get(),
                errorFunction,
                this);
    }

    /**
     * If in success state, returns the boolean success value, otherwise returns
     * {@code other}.
     *
     * @param other the value to be returned, if not in success state, may be
     * {@code null}
     * @return the boolean success value, if success state, otherwise
     * {@code other}
     */
    public Boolean orElse(Boolean other) {
        return Implementations.orElse(other, this);
    }

    /**
     * If in success state, returns the boolean success value, otherwise returns
     * {@code true}.
     *
     * @return the boolean success value, if success state, otherwise
     * {@code true}
     */
    public boolean orElseTrue() {
        return Implementations.orElse(true, this);
    }

    /**
     * If in success state, returns the boolean success value, otherwise returns
     * {@code false}.
     *
     * @return the boolean success value, if success state, otherwise
     * {@code false}
     */
    public boolean orElseFalse() {
        return Implementations.orElse(false, this);
    }

    /**
     * If in success state, returns the boolean success value, otherwise returns
     * the value returned from the given function.
     *
     * @param function the mapping function to apply to the error value, if not
     * in success state, it may return {@code null}
     * @return the boolean success value, if success state, otherwise the result
     * returned from the given function
     * @throws NullPointerException if the given function is {@code null}
     */
    public Boolean orElseGet(Function<? super E, Boolean> function) {
        return Implementations.orElseGet(function, this);
    }

    /**
     * If in success state, returns the boolean success value, otherwise throws
     * the exception returned by the given function.
     *
     * @param <X> type of the exception to be thrown
     * @param function the mapping function producing an exception by applying
     * the error value, if not in success state
     * @return the boolean success value, if success state
     * @throws X if in error state
     * @throws NullPointerException if the given function is {@code null} or
     * returns {@code null}
     */
    public <X extends Throwable> Boolean orElseThrow(
            Function<? super E, ? extends X> function) throws X {
        return Implementations.orElseThrow(function, this);
    }

    /**
     * Transforms this {@code BooleanResult} to an {@code OptionalResult}. If in
     * success state, the {@code OptionalResult} will be in success state
     * containing the boolean success value from this {@code BooleanResult}. If
     * in error state, the {@code OptionalResult} will be in error state
     * containing the error value from this {@code BooleanResult}.
     * <p>
     * The returned {@code OptionalResult} will never be empty.
     *
     * @return an {@code OptionalResult} in success state containing the boolean
     * success value from this {@code BooleanResult} or in error state
     * containing the error value from this {@code BooleanResult}
     */
    public OptionalResult<Boolean, E> toOptionalResult() {
        return errorOpt()
                .map(OptionalResult::<Boolean, E>error)
                .orElseGet(() -> OptionalResult.success(value()));
    }

    /**
     * Transforms this {@code BooleanResult} to a {@code VoidResult}. If in
     * success state, the {@code VoidResult} will be in success state. If in
     * error state, the {@code VoidResult} will be in error state containing the
     * error value from this {@code BooleanResult}.
     *
     * @return a {@code VoidResult} either in success state or in error state
     * containing the error value from this {@code BooleanResult}
     */
    public VoidResult<E> toVoidResult() {
        return errorOpt()
                .map(VoidResult::error)
                .orElseGet(VoidResult::success);
    }
}

