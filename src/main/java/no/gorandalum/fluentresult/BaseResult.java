package no.gorandalum.fluentresult;

import java.util.Objects;
import java.util.Optional;

abstract class BaseResult<T, E> {

    private final T value;
    private final E error;
    private final Class clazz;

    BaseResult(T value, E error, Class clazz) {
        this.value = value;
        this.error = error;
        this.clazz = clazz;
    }

    T value() {
        return value;
    }

    Optional<T> valueOpt() {
        return Optional.ofNullable(value);
    }

    E error() {
        return error;
    }

    Optional<E> errorOpt() {
        return Optional.ofNullable(error);
    }

    boolean isSuccess() {
        return error == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseResult)) return false;
        BaseResult<?, ?> that = (BaseResult<?, ?>) o;
        return Objects.equals(value, that.value) &&
                Objects.equals(error, that.error) &&
                Objects.equals(clazz, that.clazz);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, error, clazz);
    }

    @Override
    public String toString() {
        return errorOpt()
                .map(err -> String.format(
                        "%s[%s: %s]", clazz.getSimpleName(), "Error", err))
                .orElseGet(() -> String.format(
                        "%s[%s: %s]", clazz.getSimpleName(), "Value", value));
    }
}

