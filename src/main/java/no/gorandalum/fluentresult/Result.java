package no.gorandalum.fluentresult;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * A result object which either is in success state containing a
 * non-{@code null} success value, or in error state containing a
 * non-{@code null} error value.
 * <p>
 * A variable whose type is {@code Result} should never itself be {@code null},
 * it should always point to an {@code Result} instance.
 *
 * @param <T> the type of the success value
 * @param <E> the type of the error value
 */
public final class Result<T, E> extends BaseResult<T, E> {

    private Result(T value, E error) {
        super(value, error, Result.class);
    }

    /**
     * Returns a {@code Result} in success state containing the given
     * non-{@code null} value as success value.
     *
     * @param value the success value, which must be non-{@code null}
     * @param <T> the type of the success value
     * @param <E> the type of the error value
     * @return a {@code Result} containing the given success value
     * @throws NullPointerException if value is {@code null}
     */
    public static <T, E> Result<T, E> success(T value) {
        return new Result<>(Objects.requireNonNull(value), null);
    }

    /**
     * Returns a {@code Result} in error state containing the given
     * non-{@code null} value as error value.
     *
     * @param error the error value, which must be non-{@code null}
     * @param <T> the type of the success value
     * @param <E> the type of the error value
     * @return a {@code Result} containing the given error value
     * @throws NullPointerException if error value is {@code null}
     */
    public static <T, E> Result<T, E> error(E error) {
        return new Result<>(null, Objects.requireNonNull(error));
    }

    /**
     * If in success state, returns a {@code Result} containing the result of
     * applying the given mapping function to the success value, otherwise
     * returns the unaltered {@code Result} in error state.
     *
     * @param function the mapping function to apply to the success value, if
     * success state
     * @param <N> The type of the value returned from the mapping function
     * @return a {@code Result} containing the result of applying the mapping
     * function to the success value of this {@code Result}, if in success
     * state, otherwise the unaltered {@code Result} in error state
     * @throws NullPointerException if the given mapping function is
     * {@code null} or returns {@code null}
     */
    public <N> Result<N, E> map(Function<? super T, ? extends N> function) {
        return Implementations.map(function, Result::success, Result::error, this);
    }

    /**
     * If in success state, returns a {@code OptionalResult} containing the
     * result of applying the given mapping function to the success value,
     * otherwise returns a {@code OptionalResult} containing the error value of
     * this {@code Result}.
     *
     * @param function the mapping function to apply to the success value, if
     * success state
     * @param <N> The type of the success value which may be present in the
     * {@code Optional} returned from the mapping function
     * @return a {@code OptionalResult} containing the result of applying the
     * mapping function to the success value of this {@code Result}, if in
     * success state, otherwise a {@code OptionalResult} containing the error
     * value of this {@code Result}
     * @throws NullPointerException if the given mapping function is
     * {@code null} or returns {@code null}
     */
    public <N> OptionalResult<N, E> mapToOptional(Function<? super T, Optional<N>> function) {
        return Implementations.map(
                function,
                OptionalResult::success,
                OptionalResult::error,
                this);
    }

    /**
     * If in success state, returns a {@code BooleanResult} containing the
     * result of applying the given mapping function to the success value,
     * otherwise returns a {@code BooleanResult} containing the error value of
     * this {@code Result}.
     *
     * @param function the mapping function to apply to the success value, if
     * success state
     * @return a {@code BooleanResult} containing the result of applying the
     * mapping function to the success value of this {@code Result}, if in
     * success state, otherwise a {@code BooleanResult} containing the error
     * value of this {@code Result}
     * @throws NullPointerException if the given mapping function is
     * {@code null} or returns {@code null}
     */
    public BooleanResult<E> mapToBoolean(Function<? super T, Boolean> function) {
        return Implementations.map(
                function,
                BooleanResult::success,
                BooleanResult::error,
                this);
    }

    /**
     * If in success state, returns the {@code Result} from applying the given
     * mapping function to the success value, otherwise returns the unaltered
     * {@code Result} in error state.
     *
     * @param <N> The type of success value which may be present in the
     * {@code Result} returned by the mapping function
     * @param function the mapping function to apply to the success value, if
     * success state
     * @return the {@code Result} returned from the mapping function, if in
     * success state, otherwise the unaltered {@code Result} in error state
     * @throws NullPointerException if the given mapping function is
     * {@code null} or returns {@code null}
     */
    public <N> Result<N, E> flatMap(Function<? super T, ? extends Result<? extends N, ? extends E>> function) {
        return (Result<N, E>) Implementations.flatMap(function, this);
    }

    /**
     * If in success state, returns the {@code OptionalResult} from applying
     * the given mapping function to the success value, otherwise returns a
     * {@code OptionalResult} containing the error value of this {@code Result}.
     *
     * @param <N> The type of success value which may be present in the
     * {@code OptionalResult} returned by the mapping function
     * @param function the mapping function to apply to the success value, if
     * success state
     * @return the {@code OptionalResult} returned from the mapping function, if
     * in success state, otherwise a {@code OptionalResult} containing the error
     * value of this {@code Result}
     * @throws NullPointerException if the given mapping function is
     * {@code null} or returns {@code null}
     */
    public <N> OptionalResult<N, E> flatMapToOptionalResult(
            Function<? super T, OptionalResult<N, E>> function) {
        return Implementations.flatMap(function, this, OptionalResult::error);
    }

    /**
     * If in success state, returns the {@code BooleanResult} from applying
     * the given mapping function to the success value, otherwise returns a
     * {@code BooleanResult} containing the error value of this {@code Result}.
     *
     * @param function the mapping function to apply to the success value, if
     * success state
     * @return the {@code BooleanResult} returned from the mapping function, if
     * in success state, otherwise a {@code BooleanResult} containing the error
     * value of this {@code Result}
     * @throws NullPointerException if the given mapping function is
     * {@code null} or returns {@code null}
     */
    public BooleanResult<E> flatMapToBooleanResult(
            Function<? super T, BooleanResult<E>> function) {
        return Implementations.flatMap(function, this, BooleanResult::error);
    }

    /**
     * If in success state, returns the {@code VoidResult} from applying the
     * given mapping function to the success value, otherwise returns a
     * {@code VoidResult} containing the error value of this {@code Result}.
     *
     * @param function the mapping function to apply to the success value, if
     * success state
     * @return the {@code VoidResult} returned from the mapping function, if in
     * success state, otherwise a {@code VoidResult} containing the error value
     * of this {@code Result}
     * @throws NullPointerException if the given mapping function is
     * {@code null} or returns {@code null}
     */
    public VoidResult<E> flatMapToVoidResult(
            Function<? super T, VoidResult<E>> function) {
        return Implementations.flatMap(function, this, VoidResult::error);
    }

    /**
     * If in success state, applies the success value to the given consumer,
     * otherwise does nothing.
     *
     * @param consumer the consumer which accepts the current success value
     * @return the original {@code Result} unaltered
     * @throws NullPointerException if the given consumer is {@code null}
     */
    public Result<T, E> consume(Consumer<? super T> consumer) {
        return Implementations.consume(consumer, this);
    }

    /**
     * If in error state, applies the error value to the given consumer,
     * otherwise does nothing.
     *
     * @param errorConsumer the consumer which accepts the current error value
     * @return the original {@code Result} unaltered
     * @throws NullPointerException if the given consumer is {@code null}
     */
    public Result<T, E> consumeError(Consumer<? super E> errorConsumer) {
        return Implementations.consumeError(errorConsumer, this);
    }

    /**
     * If in success state, applies the success value to the given value
     * consumer. If in error state, applies the error value to the given error
     * consumer.
     *
     * @param valueConsumer the consumer which accepts the current success value
     * @param errorConsumer the consumer which accepts the current error value
     * @return the original {@code Result} unaltered
     * @throws NullPointerException if one of the given consumers is {@code null}
     */
    public Result<T, E> consumeEither(Consumer<? super T> valueConsumer,
                                      Consumer<? super E> errorConsumer) {
        return Implementations.consumeEither(valueConsumer, errorConsumer, this);
    }

    /**
     * If in success state, runs the given runnable, otherwise does nothing.
     *
     * @param runnable the runnable to run if success state
     * @return the original {@code Result} unaltered
     * @throws NullPointerException if the given runnable is {@code null}
     */
    public Result<T, E> runIfSuccess(Runnable runnable) {
        return Implementations.runIfSuccess(runnable, this);
    }

    /**
     * If in error state, runs the given runnable, otherwise does nothing.
     *
     * @param runnable the runnable to run if error state
     * @return the original {@code Result} unaltered
     * @throws NullPointerException if the given runnable is {@code null}
     */
    public Result<T, E> runIfError(Runnable runnable) {
        return Implementations.runIfError(runnable, this);
    }

    /**
     * If in success state, runs the given success runnable. If in error state,
     * runs the given error runnable.
     *
     * @param successRunnable the runnable to run if success state
     * @param errorRunnable the runnable to run if error state
     * @return the original {@code Result} unaltered
     * @throws NullPointerException if one of the given runnables is {@code null}
     */
    public Result<T, E> runEither(Runnable successRunnable, Runnable errorRunnable) {
        return Implementations.runEither(successRunnable, errorRunnable, this);
    }

    /**
     * Runs the given runnable, no matter the state.
     *
     * @param runnable the runnable to run
     * @return the original {@code Result} unaltered
     * @throws NullPointerException if the given runnable is {@code null}
     */
    public Result<T, E> run(Runnable runnable) {
        return Implementations.run(runnable, this);
    }

    /**
     * If in success state, verifies the success value of this {@code Result} by
     * testing it with the given predicate. If the predicate evaluates to false,
     * a new {@code Result} is returned containing error value provided by the
     * given error supplier. If the predicate evaluates to true, or the
     * {@code Result} already was in error state, the original {@code Result} is
     * returned unaltered.
     *
     * @param predicate the predicate used to verify the current success value,
     * if success state
     * @return the original {@code Result} unaltered, unless the predicate
     * evalutates to false, then a new {@code Result} in error state is returned
     * containing the supplied error value
     * @throws NullPointerException if the given predicate is {@code null} or
     * the given error supplier is null
     */
    public Result<T, E> verify(Predicate<? super T> predicate,
                               Supplier<? extends E> errorSupplier) {
        return Implementations.verify(predicate, errorSupplier, Result::error, this);
    }

    /**
     * Retrieve a value from the {@code Result} by merging the current states.
     * If in success state, return the value of applying the value function to
     * the success value. If in error state, return the value of applying the
     * error function to the error value.
     *
     * @param <N> The type of retrieved value
     * @param valueFunction the mapping function to apply to the success value,
     * if success state
     * @param errorFunction the mapping function to apply to the error value, if
     * error state
     * @return the original {@code Result} unaltered, unless the predicate
     * evalutates to false, then a new {@code Result} in error state is returned
     * containing the supplied error value
     * @throws NullPointerException if the given predicate is {@code null} or
     * the given error supplier is null
     */
    public <N> N merge(Function<? super T, ? extends N> valueFunction,
                       Function<? super E, ? extends N> errorFunction) {
        return Implementations.merge(valueFunction, errorFunction, this);
    }

    /**
     * If in success state, returns the success value, otherwise returns
     * {@code other}.
     *
     * @param other the value to be returned, if not in success state, may be
     * {@code null}
     * @return the success value, if success state, otherwise {@code other}
     */
    public T orElse(T other) {
        return Implementations.orElse(other, this);
    }

    /**
     * If in success state, returns the success value, otherwise returns the
     * value returned from the given function.
     *
     * @param function the mapping function to apply to the error value, if not
     * in success state
     * @return the success value, if success state, otherwise the result
     * returned from the given function
     * @throws NullPointerException if the given function is {@code null}
     */
    public T orElseGet(Function<? super E, ? extends T> function) {
        return Implementations.orElseGet(function, this);
    }

    /**
     * If in success state, returns the success value, otherwise throws the
     * exception returned by the given function.
     *
     * @param <X> Type of the exception to be thrown
     * @param function the mapping function producing an exception by applying
     * the error value, if not in success state
     * @return the success value, if success state
     * @throws X if in error state
     * @throws NullPointerException if the given function is {@code null}
     */
    public <X extends Throwable> T orElseThrow(
            Function<? super E, ? extends X> function) throws X {
        return Implementations.orElseThrow(function, this);
    }

    /**
     * Transforms the current {@code Result} to an {@code OptionalResult}. If
     * in success state, the {@code OptionalResult} will be in success state
     * containing the success value from this {@code Result}. If in error state,
     * the {@code OptionalResult} will be in error state containing the error
     * value from this {@code Result}.
     * <p>
     * The returned {@code OptionalResult} will never be empty.
     *
     * @return an {@code OptionalResult} in success state containing the success
     * value from this {@code Result} or in error state containing the error
     * value from this {@code Result}
     */
    public OptionalResult<T, E> toOptionalResult() {
        return errorOpt()
                .map(OptionalResult::<T, E>error)
                .orElseGet(() -> OptionalResult.success(value()));
    }

    /**
     * Transforms the current {@code Result} to a {@code VoidResult}. If in
     * success state, the {@code VoidResult} will be in success state. If in
     * error state, the {@code VoidResult} will be in error state containing the
     * error value from this {@code Result}.
     *
     * @return a {@code VoidResult} either in success state or in error state
     * containing the error value from this {@code Result}
     */
    public VoidResult<E> toVoidResult() {
        return errorOpt()
                .map(VoidResult::error)
                .orElseGet(VoidResult::success);
    }
}

