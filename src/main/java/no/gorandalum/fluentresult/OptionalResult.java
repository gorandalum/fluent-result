package no.gorandalum.fluentresult;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * A result object which either is in success state, where it may contain a
 * non-{@code null} success value or be empty, or it may be in error state
 * containing a non-{@code null} error value.
 * <p>
 * A variable whose type is {@code OptionalResult} should never itself be
 * {@code null}, it should always point to an {@code OptionalResult} instance.
 *
 * @param <T> the type of the success value
 * @param <E> the type of the error value
 */
@SuppressWarnings({"WeakerAccess", "OptionalUsedAsFieldOrParameterType"})
public final class OptionalResult<T, E> extends BaseResult<Optional<T>, E> {

    /**
     * Common instance for empty {@code OptionalResult}.
     */
    private static final OptionalResult<?, ?> RESULT_EMPTY =
            new OptionalResult<>(Optional.empty(), null);

    private OptionalResult(Optional<T> value, E error) {
        super(value, error, OptionalResult.class);
    }

    /**
     * Returns an {@code OptionalResult} in success state containing the given
     * non-{@code null} value as success value.
     *
     * @param maybeValue an {@code Optional} which may contain an success value,
     * or may be empty
     * @param <T> the type of the success value which may be present in the
     * given {@code Optional}
     * @param <E> the type of the error value
     * @return an {@code OptionalResult} in success state which either contains
     * a success value or is empty
     * @throws NullPointerException if given {@code Optional} is {@code null}
     */
    public static <T, E> OptionalResult<T, E> success(Optional<? extends T> maybeValue) {
        @SuppressWarnings("unchecked")
        Optional<T> t = (Optional<T>) Objects.requireNonNull(maybeValue);
        return t.map(OptionalResult::<T, E>success).orElse(empty());
    }

    /**
     * Returns an {@code OptionalResult} in success state containing the given
     * non-{@code null} value as success value.
     *
     * @param value the success value, which must be non-{@code null}
     * @param <T> the type of the success value
     * @param <E> the type of the error value
     * @return an {@code OptionalResult} in success state containing the given
     * success value
     * @throws NullPointerException if given success value is {@code null}
     */
    public static <T, E> OptionalResult<T, E> success(T value) {
        return new OptionalResult<>(Optional.of(value), null);
    }

    /**
     * Returns an {@code OptionalResult} in success state either containing the
     * given value as success value, or empty if the given value is null.
     *
     * @param value the success value, which may be {@code null}
     * @param <T> the type of the success value
     * @param <E> the type of the error value
     * @return an {@code OptionalResult} in success state containing the given
     * success value if not null, otherwise an empty {@code OptionalResult}
     */
    public static <T, E> OptionalResult<T, E> successNullable(T value) {
        if (value == null) {
            return empty();
        }
        return new OptionalResult<>(Optional.of(value), null);
    }

    /**
     * Returns an {@code OptionalResult} in success state, which is empty with
     * no success value.
     *
     * @param <T> the type of the success value
     * @param <E> the type of the error value
     * @return an empty {@code OptionalResult} in success state
     */
    public static <T, E> OptionalResult<T, E> empty() {
        @SuppressWarnings("unchecked")
        OptionalResult<T, E> res = (OptionalResult<T, E>) RESULT_EMPTY;
        return res;
    }

    /**
     * Returns an {@code OptionalResult} in error state containing the given
     * non-{@code null} value as error value.
     *
     * @param value the error value, which must be non-{@code null}
     * @param <T> the type of the success value
     * @param <E> the type of the error value
     * @return an {@code OptionalResult} in error state containing the given
     * error value
     * @throws NullPointerException if given error value is {@code null}
     */
    public static <T, E> OptionalResult<T, E> error(E value) {
        return new OptionalResult<>(null, Objects.requireNonNull(value));
    }

    /**
     * If in success state, returns a {@code Result} containing the result of
     * applying the given mapping function to the optional success value of this
     * {@code OptionalResult}, otherwise returns a {@code Result} containing the
     * error value of this {@code OptionalResult}.
     *
     * @param function the mapping function to apply to the optional success
     * value, if success state
     * @param <N> the type of the value returned from the mapping function
     * @return a {@code Result} containing the result of applying the mapping
     * function to the optional success value of this {@code OptionalResult}, if
     * in success state, otherwise a {@code Result} containing the error value
     * of this {@code OptionalResult}
     * @throws NullPointerException if the given mapping function is
     * {@code null} or returns {@code null}
     */
    public <N> Result<N, E> map(Function<Optional<T>, ? extends N> function) {
        return Implementations.map(function, Result::success, Result::error, this);
    }

    /**
     * If in success state, returns a {@code OptionalResult} containing the
     * result of applying the given mapping function to the optional success
     * value, otherwise returns the unaltered {@code OptionalResult} in error
     * state.
     *
     * @param function the mapping function to apply to the optional success
     * value, if success state
     * @param <N> the type of the success value which may be present in the
     * {@code Optional} returned from the mapping function
     * @return a {@code OptionalResult} containing the result of applying the
     * mapping function to the optional success value of this
     * {@code OptionalResult}, if in success state, otherwise the unaltered
     * {@code OptionalResult} in error state
     * @throws NullPointerException if the given mapping function is
     * {@code null} or returns {@code null}
     */
    public <N> OptionalResult<N, E> mapToOptional(
            Function<Optional<T>, ? extends Optional<? extends N>> function) {
        return Implementations.map(
                function, OptionalResult::success, OptionalResult::error, this);
    }

    /**
     * If in success state, returns a {@code BooleanResult} containing the
     * result of applying the given mapping function to the optional success
     * value, otherwise returns a {@code BooleanResult} containing the error
     * value of this {@code OptionalResult}.
     *
     * @param function the mapping function to apply to the optional success
     * value, if success state
     * @return a {@code BooleanResult} containing the result of applying the
     * mapping function to the optional success value of this
     * {@code OptionalResult}, if in success state, otherwise a
     * {@code BooleanResult} containing the error value of this
     * {@code OptionalResult}
     * @throws NullPointerException if the given mapping function is
     * {@code null} or returns {@code null}
     */
    public BooleanResult<E> mapToBoolean(Function<Optional<T>, Boolean> function) {
        return Implementations.map(
                function,
                BooleanResult::success,
                BooleanResult::error,
                this);
    }

    /**
     * If in error state, returns a {@code OptionalResult} containing the result
     * of applying the given mapping function to the error value, otherwise
     * returns the unaltered {@code OptionalResult} in success state.
     *
     * @param function the mapping function to apply to the error value, if
     * error state
     * @param <N> the type of the value returned from the mapping function
     * @return a {@code OptionalResult} containing the result of applying the
     * mapping function to the error value of this {@code OptionalResult}, if in
     * error state, otherwise the unaltered {@code OptionalResult} in success
     * state
     * @throws NullPointerException if the given mapping function is
     * {@code null} or returns {@code null}
     */
    public <N> OptionalResult<T, N> mapError(Function<? super E, ? extends N> function) {
        return Implementations.mapError(function, OptionalResult::error, this);
    }

    /**
     * If in success state with a success value, returns an
     * {@code OptionalResult} containing the result of applying the given
     * mapping function to the success value, otherwise returns the unaltered
     * {@code OptionalResult} which may be empty or in error state.
     * <p>
     * If the given mapping function returns null, then the returned
     * {@code OptionalResult} will be empty.
     *
     * @param function the mapping function to apply to the success value, if
     * success state with a success value
     * @param <N> the type of the value returned from the mapping function
     * @return an {@code OptionalResult} containing the result of applying the
     * mapping function to the success value of this {@code OptionalResult}, if
     * in success state with a success value, otherwise the unaltered
     * {@code OptionalResult} which may be empty or in error state
     * @throws NullPointerException if the given mapping function is
     * {@code null}
     */
    public <N> OptionalResult<N, E> mapValue(
            Function<? super T, ? extends N> function) {
        return Implementations.map(
                maybeVal -> maybeVal.map(val -> (N) function.apply(val)),
                OptionalResult::success,
                OptionalResult::error,
                this);
    }

    /**
     * If in success state with a success value, returns an
     * {@code OptionalResult} containing the result of applying the given
     * mapping function to the success value, otherwise returns the unaltered
     * {@code OptionalResult} which may be empty or in error state.
     * <p>
     * If the given mapping function returns a non-empty {@code Optional}, then
     * the returned {@code OptionalResult} will contain the {@code Optional}
     * content as success value. If the given mapping function returns an empty
     * {@code Optional}, then the returned {@code OptionalResult} will also be
     * empty.
     *
     * @param function the mapping function to apply to the success value, if
     * success state with a success value
     * @param <N> the type of the success value which may be present in the
     * {@code Optional} returned from the mapping function
     * @return an {@code OptionalResult} containing the result of applying the
     * mapping function to the success value of this {@code OptionalResult}, if
     * in success state with a success value, otherwise the unaltered
     * {@code OptionalResult} which may be empty or in error state
     * @throws NullPointerException if the given mapping function is
     * {@code null} or returns {@code null}
     */
    public <N> OptionalResult<N, E> mapValueToOptional(
            Function<? super T, Optional<N>> function) {
        return Implementations.map(
                maybeVal -> maybeVal.flatMap(function),
                OptionalResult::success,
                OptionalResult::error,
                this);
    }

    /**
     * If in success state, returns the {@code Result} from applying the given
     * mapping function to the optional success value, otherwise returns a
     * {@code Result} containing the error value of this {@code OptionalResult}.
     *
     * @param <N> the type of success value which may be present in the
     * {@code Result} returned by the mapping function
     * @param function the mapping function to apply to the optional success
     * value, if success state
     * @return the {@code Result} returned from the mapping function, if in
     * success state, otherwise a {@code Result} containing the error value of
     * this {@code OptionalResult}
     * @throws NullPointerException if the given mapping function is
     * {@code null} or returns {@code null}
     */
    public <N> Result<N, E> flatMap(
            Function<Optional<T>, Result<? extends N, ? extends E>> function) {
        @SuppressWarnings("unchecked")
        Result<N, E> res = (Result<N, E>) Implementations.flatMap(
                function, this, Result::error);
        return res;
    }

    /**
     * If in success state, returns the {@code OptionalResult} from applying
     * the given mapping function to the optional success value, otherwise
     * returns the unaltered {@code OptionalResult} in error state.
     *
     * @param <N> the type of success value which may be present in the
     * {@code OptionalResult} returned by the mapping function
     * @param function the mapping function to apply to the optional success
     * value, if success state
     * @return the {@code OptionalResult} returned from the mapping function, if
     * in success state, otherwise the unaltered {@code OptionalResult} in error
     * state
     * @throws NullPointerException if the given mapping function is
     * {@code null} or returns {@code null}
     */
    public <N> OptionalResult<N, E> flatMapToOptionalResult(
            Function<Optional<T>, OptionalResult<? extends N, ? extends E>> function) {
        @SuppressWarnings("unchecked")
        OptionalResult<N, E> res = (OptionalResult<N, E>) Implementations.flatMap(
                function, this);
        return res;
    }

    /**
     * If in success state, returns the {@code BooleanResult} from applying
     * the given mapping function to the optional success value, otherwise
     * returns a {@code BooleanResult} containing the error value of this
     * {@code OptionalResult}.
     *
     * @param function the mapping function to apply to the optional success
     * value, if success state
     * @return the {@code BooleanResult} returned from the mapping function, if
     * in success state, otherwise a {@code BooleanResult} containing the error
     * value of this {@code OptionalResult}
     * @throws NullPointerException if the given mapping function is
     * {@code null} or returns {@code null}
     */
    public BooleanResult<E> flatMapToBooleanResult(
            Function<Optional<? extends T>, BooleanResult<? extends E>> function) {
        @SuppressWarnings("unchecked")
        BooleanResult<E> res = (BooleanResult<E>) Implementations.flatMap(
                function, this, BooleanResult::error);
        return res;
    }

    /**
     * If in success state, returns the {@code VoidResult} from applying the
     * given mapping function to the optional success value, otherwise returns a
     * {@code VoidResult} containing the error value of this
     * {@code OptionalResult}.
     *
     * @param function the mapping function to apply to the optional success
     * value, if success state
     * @return the {@code VoidResult} returned from the mapping function, if in
     * success state, otherwise a {@code VoidResult} containing the error value
     * of this {@code OptionalResult}
     * @throws NullPointerException if the given mapping function is
     * {@code null} or returns {@code null}
     */
    public VoidResult<E> flatMapToVoidResult(
            Function<Optional<? extends T>, VoidResult<? extends E>> function) {
        @SuppressWarnings("unchecked")
        VoidResult<E> res = (VoidResult<E>) Implementations.flatMap(
                function, this, VoidResult::error);
        return res;
    }

    /**
     * If in success state with a success value, returns an
     * {@code OptionalResult} from applying the given mapping function to the
     * success value, otherwise returns the unaltered {@code OptionalResult}
     * which may be empty or in error state.
     * <p>
     * If the {@code Result} returned from the mapping function is in success
     * state. the returned {@code OptionalResult} will contain the success value
     * from the {@code Result}. If the {@code Result} is in error state, the
     * returned {@code OptionalResult} will contain the error value from the
     * {@code Result}.
     *
     * @param <N> the type of success value which may be present in the
     * {@code OptionalResult} after applying the mapping function
     * @param function the mapping function to apply to the success value, if
     * success state with a success value
     * @return an {@code OptionalResult} after applying the mapping function, if
     * in success state with a success value, otherwise the unaltered
     * {@code OptionalResult} which may be empty or in error state
     * @throws NullPointerException if the given mapping function is
     * {@code null} or returns {@code null}
     */
    public <N> OptionalResult<N, E> flatMapValueWithResult(
            Function<? super T, Result<? extends N, ? extends E>> function) {
        @SuppressWarnings("unchecked")
        OptionalResult<N, E> res = (OptionalResult<N, E>) Implementations.flatMap(
                maybeVal -> maybeVal
                        .map(val -> Objects.requireNonNull(function.apply(val)))
                        .map(Result::toOptionalResult)
                        .orElseGet(OptionalResult::empty),
                this);
        return res;
    }

    /**
     * If in success state with a success value, returns the
     * {@code OptionalResult} from applying the given mapping function to the
     * success value, otherwise returns the unaltered {@code OptionalResult}
     * which may be empty or in error state.
     *
     * @param <N> the type of success value which may be present in the
     * {@code OptionalResult} returned by the mapping function
     * @param function the mapping function to apply to the success value, if
     * success state with a success value
     * @return the {@code OptionalResult} returned from the mapping function, if
     * in success state with a success value, otherwise the unaltered
     * {@code OptionalResult} which may be empty or in error state
     * @throws NullPointerException if the given mapping function is
     * {@code null} or returns {@code null}
     */
    public <N> OptionalResult<N, E> flatMapValueWithOptionalResult(
            Function<? super T, OptionalResult<? extends N, ? extends E>> function) {
        @SuppressWarnings("unchecked")
        OptionalResult<N, E> res = (OptionalResult<N, E>) Implementations.flatMap(
                maybeVal -> maybeVal
                        .map(val -> Objects.requireNonNull(function.apply(val)))
                        .orElseGet(OptionalResult::empty),
                this
        );
        return res;
    }

    /**
     * If in success state with a success value, returns an
     * {@code OptionalResult} from applying the given mapping function to the
     * success value, otherwise returns the unaltered {@code OptionalResult}
     * which may be empty or in error state.
     * <p>
     * If the {@code BooleanResult} returned from the mapping function is in
     * success state. the returned {@code OptionalResult} will contain the
     * boolean success value from the {@code BooleanResult}. If the
     * {@code BooleanResult} is in error state, the returned
     * {@code OptionalResult} will contain the error value from the
     * {@code BooleanResult}.
     *
     * @param function the mapping function to apply to the success value, if
     * success state with a success value
     * @return an {@code OptionalResult} after applying the mapping function, if
     * in success state with a success value, otherwise the unaltered
     * {@code OptionalResult} which may be empty or in error state
     * @throws NullPointerException if the given mapping function is
     * {@code null} or returns {@code null}
     */
    public OptionalResult<Boolean, E> flatMapValueWithBooleanResult(
            Function<? super T, BooleanResult<? extends E>> function) {
        @SuppressWarnings("unchecked")
        OptionalResult<Boolean, E> res = (OptionalResult<Boolean, E>) Implementations.flatMap(
                maybeVal -> maybeVal
                        .map(val -> Objects.requireNonNull(function.apply(val)))
                        .map(BooleanResult::toOptionalResult)
                        .orElseGet(OptionalResult::empty),
                this);
        return res;
    }

    /**
     * If in success state, applies the optional success value to the given
     * consumer, otherwise does nothing.
     *
     * @param consumer the consumer which accepts the optional success value
     * @return the original {@code OptionalResult} unaltered
     * @throws NullPointerException if the given consumer is {@code null}
     */
    public OptionalResult<T, E> consume(Consumer<Optional<T>> consumer) {
        return Implementations.consume(consumer, this);
    }

    /**
     * If in success state with a success value, applies the success value to
     * the given consumer, otherwise does nothing.
     *
     * @param consumer the consumer which accepts the success value
     * @return the original {@code OptionalResult} unaltered
     * @throws NullPointerException if the given consumer is {@code null}
     */
    public OptionalResult<T, E> consumeValue(Consumer<T> consumer) {
        Objects.requireNonNull(consumer);
        valueOpt().ifPresent(maybeVal -> maybeVal.ifPresent(consumer));
        return this;
    }

    /**
     * If in error state, applies the error value to the given consumer,
     * otherwise does nothing.
     *
     * @param errorConsumer the consumer which accepts the error value
     * @return the original {@code OptionalResult} unaltered
     * @throws NullPointerException if the given consumer is {@code null}
     */
    public OptionalResult<T, E> consumeError(Consumer<? super E> errorConsumer) {
        return Implementations.consumeError(errorConsumer, this);
    }

    /**
     * If in success state, applies the optional success value to the given
     * success consumer. If in error state, applies the error value to the given
     * error consumer.
     *
     * @param successConsumer the consumer which accepts the optional success
     * value
     * @param errorConsumer the consumer which accepts the error value
     * @return the original {@code OptionalResult} unaltered
     * @throws NullPointerException if one of the given consumers is {@code null}
     */
    public OptionalResult<T, E> consumeEither(
            Consumer<Optional<T>> successConsumer,
            Consumer<? super E> errorConsumer) {
        return Implementations.consumeEither(
                successConsumer,
                errorConsumer,
                this
        );
    }

    /**
     * If in success state with a success value, applies the success value to
     * the given value consumer. If empty, run the given empty runnable. If in
     * error state, applies the error value to the given error consumer.
     *
     * @param valueConsumer the consumer which accepts the optional success
     * value
     * @param emptyRunnable the runnable to run if empty
     * @param errorConsumer the consumer which accepts the error value
     * @return the original {@code OptionalResult} unaltered
     * @throws NullPointerException if one of the given consumers or runnable is
     * {@code null}
     */
    public OptionalResult<T, E> consumeEither(
            Consumer<? super T> valueConsumer,
            Runnable emptyRunnable,
            Consumer<? super E> errorConsumer) {
        Objects.requireNonNull(valueConsumer);
        Objects.requireNonNull(emptyRunnable);

        return Implementations.consumeEither(
                maybeVal -> {
                    if (maybeVal.isPresent()) {
                        valueConsumer.accept(maybeVal.get());
                    } else {
                        emptyRunnable.run();
                    }
                },
                errorConsumer,
                this
        );
    }

    /**
     * If in success state, applies the optional success value to the given
     * function. If the function returns a {@code VoidResult} in success state,
     * the original {@code OptionalResult} is returned unaltered. If the function
     * returns a {@code VoidResult} in error state, a {@code OptionalResult}
     * containing the error value is returned. If in error state, the original
     * {@code OptionalResult} is returned unaltered.
     *
     * @param function the function which accepts the optional success value
     * @return the original {@code OptionalResult} unaltered if the given
     * function returns success or the original {@code OptionalResult} is in
     * error state, otherwise a {@code OptionalResult} containing the error
     * value from the function result
     * @throws NullPointerException if the given function is {@code null} or
     * returns {@code null}
     */
    public OptionalResult<T, E> flatConsume(
            Function<Optional<T>, ? extends VoidResult<? extends E>> function) {
        return Implementations.flatConsume(function, OptionalResult::error, this);
    }

    /**
     * If in success state, applies the success value to the given function. If
     * the function returns a {@code VoidResult} in success state, the original
     * {@code OptionalResult} is returned unaltered. If the function returns a
     * {@code VoidResult} in error state, a {@code OptionalResult} containing
     * the error value is returned. If in empty or error state, the original
     * {@code OptionalResult} is returned unaltered.
     *
     * @param function the function which accepts the success value
     * @return the original {@code OptionalResult} unaltered if the given
     * function returns success or the original {@code OptionalResult} is in
     * empty or error state, otherwise a {@code OptionalResult} containing the
     * error value from the function result
     * @throws NullPointerException if the given function is {@code null} or
     * returns {@code null}
     */
    public OptionalResult<T, E> flatConsumeValue(
            Function<T, ? extends VoidResult<? extends E>> function) {
        Objects.requireNonNull(function);
        @SuppressWarnings("unchecked")
        OptionalResult<T, E> result = Implementations.flatConsume(
                maybeVal -> maybeVal
                        .map(val -> (VoidResult<E>) Objects.requireNonNull(function.apply(val)))
                        .orElseGet(VoidResult::success)
                , OptionalResult::error, this);
        return result;
    }

    /**
     * If in success state, runs the given runnable, otherwise does nothing.
     *
     * @param runnable the runnable to run if success state
     * @return the original {@code OptionalResult} unaltered
     * @throws NullPointerException if the given runnable is {@code null}
     */
    public OptionalResult<T, E> runIfSuccess(Runnable runnable) {
        return Implementations.runIfSuccess(runnable, this);
    }

    /**
     * If in success state with a success value, runs the given runnable,
     * otherwise does nothing.
     *
     * @param runnable the runnable to run if success state with a success value
     * @return the original {@code OptionalResult} unaltered
     * @throws NullPointerException if the given runnable is {@code null}
     */
    public OptionalResult<T, E> runIfValue(Runnable runnable) {
        Objects.requireNonNull(runnable);
        valueOpt().ifPresent(maybeVal -> maybeVal.ifPresent(val -> runnable.run()));
        return this;
    }

    /**
     * If in empty success state or error state, runs the given runnable,
     * otherwise does nothing.
     *
     * @param runnable the runnable to run if empty success state or error state
     * @return the original {@code OptionalResult} unaltered
     * @throws NullPointerException if the given runnable is {@code null}
     */
    public OptionalResult<T, E> runIfNoValue(Runnable runnable) {
        Objects.requireNonNull(runnable);
        return runIfEmpty(runnable).runIfError(runnable);
    }

    /**
     * If in empty success state with no success value, runs the given runnable,
     * otherwise does nothing.
     *
     * @param runnable the runnable to run if empty success state
     * @return the original {@code OptionalResult} unaltered
     * @throws NullPointerException if the given runnable is {@code null}
     */
    public OptionalResult<T, E> runIfEmpty(Runnable runnable) {
        Objects.requireNonNull(runnable);
        valueOpt().ifPresent(maybeVal -> {
            if (!maybeVal.isPresent()) {
                runnable.run();
            }
        });
        return this;
    }

    /**
     * If in error state, runs the given runnable, otherwise does nothing.
     *
     * @param runnable the runnable to run if error state
     * @return the original {@code OptionalResult} unaltered
     * @throws NullPointerException if the given runnable is {@code null}
     */
    public OptionalResult<T, E> runIfError(Runnable runnable) {
        return Implementations.runIfError(runnable, this);
    }

    /**
     * If in success state, runs the given success runnable. If in error state,
     * runs the given error runnable.
     *
     * @param successRunnable the runnable to run if success state
     * @param errorRunnable the runnable to run if error state
     * @return the original {@code OptionalResult} unaltered
     * @throws NullPointerException if one of the given runnables is {@code null}
     */
    public OptionalResult<T, E> runEither(Runnable successRunnable,
                                          Runnable errorRunnable) {
        return Implementations.runEither(successRunnable, errorRunnable, this);
    }

    /**
     * If in success state with a success value, runs the given value runnable.
     * If empty, runs the given empty runnable. If in error state, runs the
     * given error runnable.
     *
     * @param valueRunnable the runnable to run if success state with a success
     * value
     * @param emptyRunnable the runnable to run if empty
     * @param errorRunnable the runnable to run if error state
     * @return the original {@code OptionalResult} unaltered
     * @throws NullPointerException if one of the given runnables is {@code null}
     */
    public OptionalResult<T, E> runEither(Runnable valueRunnable,
                                          Runnable emptyRunnable,
                                          Runnable errorRunnable) {
        Objects.requireNonNull(valueRunnable);
        Objects.requireNonNull(emptyRunnable);
        return Implementations.runEither(
                () -> {
                    if (value().isPresent()) {
                        valueRunnable.run();
                    } else {
                        emptyRunnable.run();
                    }
                },
                errorRunnable, this);
    }

    /**
     * Runs the given runnable, no matter the state.
     *
     * @param runnable the runnable to run
     * @return the original {@code OptionalResult} unaltered
     * @throws NullPointerException if the given runnable is {@code null}
     * @deprecated use {@link #runAlways} instead for clarity
     */
    @Deprecated
    public OptionalResult<T, E> run(Runnable runnable) {
        return runAlways(runnable);
    }

    /**
     * Runs the given runnable, no matter the state.
     *
     * @param runnable the runnable to run
     * @return the original {@code OptionalResult} unaltered
     * @throws NullPointerException if the given runnable is {@code null}
     */
    public OptionalResult<T, E> runAlways(Runnable runnable) {
        return Implementations.runAlways(runnable, this);
    }

    /**
     * If in success state, runs the given supplier. If the supplier returns a
     * {@code VoidResult} in success state, the original {@code OptionalResult}
     * is returned unaltered. If the supplier returns a {@code VoidResult} in
     * error state, a {@code OptionalResult} containing the error value is
     * returned. If in error state, the original {@code OptionalResult} is
     * returned unaltered.
     *
     * @param supplier the supplier to run
     * @return the original {@code OptionalResult} unaltered if the given
     * supplier returns success or the original {@code OptionalResult} is in
     * error state, otherwise a {@code OptionalResult} containing the error
     * value from the supplier result
     * @throws NullPointerException if the given supplier is {@code null} or
     * returns {@code null}
     */
    public OptionalResult<T, E> flatRunIfSuccess(
            Supplier<? extends VoidResult<? extends E>> supplier) {
        return Implementations.flatRunIfSuccess(supplier, OptionalResult::error, this);
    }

    /**
     * If in success state with a success value, runs the given supplier. If the
     * supplier returns a {@code VoidResult} in success state, the original
     * {@code OptionalResult} is returned unaltered. If the supplier returns a
     * {@code VoidResult} in error state, a {@code OptionalResult} containing
     * the error value is returned. If in error state or empty, the original
     * {@code OptionalResult} is returned unaltered.
     *
     * @param supplier the supplier to run
     * @return the original {@code OptionalResult} unaltered if the given
     * supplier returns success or the original {@code OptionalResult} is in
     * empty or error state, otherwise a {@code OptionalResult} containing the
     * error value from the supplier result
     * @throws NullPointerException if the given supplier is {@code null} or
     * returns {@code null}
     */
    public OptionalResult<T, E> flatRunIfValue(
            Supplier<? extends VoidResult<? extends E>> supplier) {
        Objects.requireNonNull(supplier);
        return Implementations.flatRunIfSuccess(
                () -> {
                    if (value() != null && value().isPresent()) {
                        return supplier.get();
                    } else {
                        return VoidResult.success();
                    }
                },
                OptionalResult::error,
                this);
    }

    /**
     * If in success state, verifies the optional success value of this
     * {@code OptionalResult} by testing it with the given predicate. If the
     * predicate evaluates to false, a new {@code OptionalResult} is returned
     * containing the error value provided by the given error supplier. If the
     * predicate evaluates to true, or the {@code OptionalResult} already was
     * in error state, the original {@code OptionalResult} is returned
     * unaltered.
     *
     * @param predicate the predicate used to verify the optional success value,
     * if success state
     * @param errorSupplier supplier providing the error if predicate evaluates
     * to false
     * @return the original {@code OptionalResult} unaltered, unless the
     * predicate evaluates to false, then a new {@code OptionalResult} in error
     * state is returned containing the supplied error value
     * @throws NullPointerException if the given predicate is {@code null} or
     * returns {@code null}, or the given error supplier is {@code null} or
     * returns {@code null}
     */
    public OptionalResult<T, E> verify(Predicate<Optional<T>> predicate,
                                       Supplier<? extends E> errorSupplier) {
        return Implementations.verify(
                predicate,
                errorSupplier,
                OptionalResult::error,
                this);
    }

    /**
     * If in success state, verifies the success value of this
     * {@code OptionalResult} by mapping it to a {@code VoidResult}. If the
     * returned {@code VoidResult} is in error state, a new
     * {@code OptionalResult} is returned containing the error value of the
     * {@code VoidResult}. If the {@code VoidResult} is in success state, or the
     * {@code OptionalResult} already was in error state, the original
     * {@code OptionalResult} is returned unaltered.
     *
     * @param function the function applied to the success value, if success
     * state
     * @return the original {@code OptionalResult} unaltered, unless the
     * {@code VoidResult} returned by the mapping function is in error state,
     * then a new {@code OptionalResult} in error state is returned containing
     * the error value from the {@code VoidResult}
     * @throws NullPointerException if the given function is {@code null} or
     * returns {@code null}
     */
    public OptionalResult<T, E> verify(
            Function<Optional<T>, ? extends VoidResult<? extends E>> function) {
        return Implementations.flatConsume(function, OptionalResult::error, this);
    }

    /**
     * If in success state with a success value, verifies the success value of
     * this {@code OptionalResult} by testing it with the given predicate. If
     * the predicate evaluates to false, a new {@code OptionalResult} is returned
     * containing the error value provided by the given error supplier. If the
     * predicate evaluates to true, or the {@code OptionalResult} already was
     * empty or in error state, the original {@code OptionalResult} is returned
     * unaltered.
     *
     * @param predicate the predicate used to verify the success value, if
     * success state with a success value
     * @param errorSupplier supplier providing the error if predicate evaluates
     * to false
     * @return the original {@code OptionalResult} unaltered, unless the
     * predicate evaluates to false, then a new {@code OptionalResult} in error
     * state is returned containing the supplied error value
     * @throws NullPointerException if the given predicate is {@code null} or
     * returns {@code null}, or the given error supplier is {@code null} or
     * returns {@code null}
     */
    public OptionalResult<T, E> verifyValue(Predicate<? super T> predicate,
                                            Supplier<? extends E> errorSupplier) {
        return Implementations.verify(
                maybeValue -> maybeValue.map(predicate::test).orElse(true),
                errorSupplier,
                OptionalResult::error,
                this);
    }

    /**
     * If in non-empty success state, verifies the success value of this
     * {@code OptionalResult} by mapping it to a {@code VoidResult}. If the
     * returned {@code VoidResult} is in error state, a new
     * {@code OptionalResult} is returned containing the error value of the
     * {@code VoidResult}. If the {@code VoidResult} is in success state, or the
     * {@code OptionalResult} already was empty or in error state, the original
     * {@code OptionalResult} is returned unaltered.
     *
     * @param function the function applied to the success value, if non-empty
     * success state
     * @return the original {@code OptionalResult} unaltered, unless the
     * {@code VoidResult} returned by the mapping function is in error state,
     * then a new {@code OptionalResult} in error state is returned containing
     * the error value from the {@code VoidResult}
     * @throws NullPointerException if the given function is {@code null} or
     * returns {@code null}
     */
    public OptionalResult<T, E> verifyValue(
            Function<? super T, ? extends VoidResult<? extends E>> function) {
        @SuppressWarnings("unchecked")
        OptionalResult<T, E> result = Implementations.flatConsume(
                maybeValue -> maybeValue
                        .map(val -> (VoidResult<E>) Objects.requireNonNull(function.apply(val)))
                        .orElseGet(VoidResult::success),
                OptionalResult::error,
                this);
        return result;
    }

    /**
     * Retrieve a value from this {@code OptionalResult} by folding the states.
     * If in success state, return the value of applying the success function to
     * the optional success value. If in error state, return the value of
     * applying the error function to the error value.
     *
     * @param <N> the type of the retrieved value
     * @param successFunction the mapping function to apply to the optional
     * success value, if success state, may return {@code null}
     * @param errorFunction the mapping function to apply to the error value, if
     * error state, may return {@code null}
     * @return the folded value mapped from either the success value or error
     * value, may be {@code null}
     * @throws NullPointerException if one of the given functions is
     * {@code null}
     */
    public <N> N fold(Function<Optional<T>, ? extends N> successFunction,
                      Function<? super E, ? extends N> errorFunction) {
        return Implementations.fold(successFunction, errorFunction, this);
    }

    /**
     * Retrieve a value from this {@code OptionalResult} by folding the states.
     * If in success state with a success value, return the value of applying
     * the value function to the success value. If empty, return the value
     * provided by the empty-supplier. If in error state, return the value of
     * applying the error function to the error value.
     *
     * @param <N> the type of the retrieved value
     * @param valueFunction the mapping function to apply to the success value,
     * if success state, may return {@code null}
     * @param emptySupplier the supplier to provide the value if empty, may
     * return {@code null}
     * @param errorFunction the mapping function to apply to the error value, if
     * error state, may return {@code null}
     * @return the folded value mapped from either the success value or error
     * value, may be {@code null}
     * @throws NullPointerException if one of the given functions or the
     * supplier is {@code null}
     */
    public <N> N fold(Function<? super T, ? extends N> valueFunction,
                      Supplier<? extends N> emptySupplier,
                      Function<? super E, ? extends N> errorFunction) {
        Objects.requireNonNull(valueFunction);
        Objects.requireNonNull(emptySupplier);
        Objects.requireNonNull(errorFunction);
        return Implementations.fold(
                maybeVal -> maybeVal.isPresent() ?
                        valueFunction.apply(maybeVal.get()) :
                        emptySupplier.get(),
                errorFunction,
                this);
    }

    /**
     * If in success state, returns the optional success value, otherwise
     * returns {@code other}.
     *
     * @param other the value to be returned, if not in success state, may not
     * be {@code null}
     * @return the optional success value, if success state, otherwise
     * {@code other}
     * @throws NullPointerException if the other value is {@code null}
     */
    public Optional<T> orElse(Optional<T> other) {
        return Implementations.orElse(other, this);
    }

    /**
     * If in success state with a success value, returns the success value,
     * otherwise returns {@code other}.
     *
     * @param other the value to be returned, if not in success state, may be
     * {@code null}
     * @return the success value, if success state with a success value,
     * otherwise {@code other}
     */
    public T valueOrElse(T other) {
        return valueOpt().flatMap(Function.identity()).orElse(other);
    }

    /**
     * If in success state, returns the optional success value, otherwise
     * returns the value returned from the given function.
     *
     * @param function the mapping function to apply to the error value, if not
     * in success state, it may not return {@code null}
     * @return the optional success value, if success state, otherwise the
     * result returned from the given function
     * @throws NullPointerException if the given function is {@code null} or
     * returns {@code null}
     */
    public Optional<T> orElseGet(Function<? super E, ? extends Optional<T>> function) {
        return Implementations.orElseGet(function, this);
    }

    /**
     * If in success state with a success value, returns the success value,
     * otherwise returns the value returned from the given function.
     *
     * @param supplier the supplier providing the return value, if not in
     * success state with a success value, it may return {@code null}
     * @return the success value, if success state with a success value,
     * otherwise the result returned from the given function
     * @throws NullPointerException if the given function is {@code null}
     */
    public T valueOrElseGet(Supplier<? extends T> supplier) {
        Objects.requireNonNull(supplier);
        return valueOpt().flatMap(Function.identity()).orElseGet(supplier);
    }

    /**
     * If in success state, returns the optional success value, otherwise throws
     * the exception returned by the given function.
     *
     * @param <X> type of the exception to be thrown
     * @param function the mapping function producing an exception by applying
     * the error value, if not in success state
     * @return the optional success value, if success state
     * @throws X if in error state
     * @throws NullPointerException if the given function is {@code null} or
     * returns {@code null}
     */
    public <X extends Throwable> Optional<T> orElseThrow(
            Function<? super E, ? extends X> function) throws X {
        return Implementations.orElseThrow(function, this);
    }

    /**
     * If in success state with a success value, returns the success value,
     * otherwise throws the exception returned by the given function.
     *
     * @param <X> type of the exception to be thrown
     * @param supplier the supplier providing the return value, if not in
     * success state with a success value
     * @return the success value, if success state with success value
     * @throws X if empty or in error state
     * @throws NullPointerException if the given function is {@code null} or
     * returns {@code null}
     */
    public <X extends Throwable> T valueOrElseThrow(
            Supplier<? extends X> supplier) throws X {
        Objects.requireNonNull(supplier);
        return valueOpt().flatMap(Function.identity()).orElseThrow(supplier);
    }

    /**
     * Transforms this {@code OptionalResult} to a {@code Result}. If in
     * non-empty succes state, the {@code Result} will be in success state
     * containing the success value of this {@code OptionalResult}. If empty
     * success state, the {@code Result} will be in error state, containing the
     * error value supplied by the given error supplier. If in error state, the
     * {@code Result} will be in error state containing the error value of this
     * {@code OptionalResult}.
     *
     * @param errorSupplier supplier providing the error value if empty success
     * state
     * @return a {@code Result} either in success state containing the success
     * value from this {@code OptionalResult}, or in error state containing
     * either the error value from the given error supplier or the error value
     * present in this {@code OptionalResult}
     * @throws NullPointerException if the given supplier is {@code null} or
     * returns {@code null}
     */
    public Result<T, E> toResult(Supplier<? extends E> errorSupplier) {
        Objects.requireNonNull(errorSupplier);
        return fold(Result::success, () -> Result.error(errorSupplier.get()), Result::error);
    }

    /**
     * Transforms this {@code OptionalResult} to a {@code VoidResult}. If in
     * success state, the {@code VoidResult} will be in success state. If in
     * error state, the {@code VoidResult} will be in error state containing the
     * error value from this {@code OptionalResult}.
     *
     * @return a {@code VoidResult} either in success state or in error state
     * containing the error value from this {@code OptionalResult}
     */
    public VoidResult<E> toVoidResult() {
        return errorOpt()
                .map(VoidResult::error)
                .orElseGet(VoidResult::success);
    }

    /**
     * Handle the given {@code Callable}. If the {@code Callable} executes
     * successfully, the {@code OptionalResult} will be in success state
     * containing the returned value. If the {@code Callable} throws an
     * exception, the {@code OptionalResult} will be in error state containing
     * the thrown exception.
     *
     * @param callable the {@code Callable} to handle
     * @param <T> type of the return value of the {@code Callable}
     * @return a {@code OptionalResult} either in success state containing the
     * value from the {@code Callable}, or in error state containing the
     * exception thrown by the {@code Callable}
     * @throws NullPointerException if the given callable is {@code null} or
     * returns {@code null}
     */
    public static <T> OptionalResult<T, Exception> handle(Callable<Optional<T>> callable) {
        Objects.requireNonNull(callable);
        final Optional<T> value;
        try {
            value = callable.call();
        } catch (Exception e) {
            return OptionalResult.error(e);
        }
        return OptionalResult.success(value);
    }

    /**
     * Handle the given {@code Callable}. If the {@code Callable} executes
     * successfully, the {@code OptionalResult} will be in success state
     * containing the returned value. If the {@code Callable} throws an
     * exception, the {@code OptionalResult} will be in error state containing
     * the result after mapping the exception with the given exception mapper
     * function.
     *
     * @param callable the {@code Callable} to handle
     * @param <T> type of the return value of the {@code Callable}
     * @param <E> type of the error value after mapping a thrown exception
     * @return a {@code OptionalResult} either in success state containing the
     * value from the {@code Callable}, or in error state containing the result
     * after mapping the exception thrown by the {@code Callable}
     * @throws NullPointerException if the given callable is {@code null} or
     * returns {@code null}, or if the given exception mapper function is
     * {@code null} or returns {@code null}
     */
    public static <T, E> OptionalResult<T, E> handle(Callable<Optional<T>> callable,
                                                     Function<Exception, E> exceptionMapper) {
        Objects.requireNonNull(exceptionMapper);
        return handle(callable).mapError(exceptionMapper);
    }
}

