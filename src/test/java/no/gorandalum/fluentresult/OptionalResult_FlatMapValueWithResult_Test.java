package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class OptionalResult_FlatMapValueWithResult_Test {

    @Test
    void flatMapValueWithResult_success_successfullyMapValue() {
        OptionalResult<Integer, String> result =
                OptionalResult.<String, String>success("Success")
                        .flatMapValueWithResult(val -> Result.success(val.length()));
        result.consumeEither(
                val -> assertThat(val).isEqualTo(7),
                () -> fail("Should not be empty"),
                err -> fail("Expected no error"));
    }

    @Test
    void flatMapValueWithResult_success_mapValueToError() {
        OptionalResult<Integer, String> result =
                OptionalResult.<String, String>success("Success")
                        .flatMapValueWithResult(val -> Result.error("Error"));
        result.consumeEither(
                val -> fail("Should not have value"),
                () -> fail("Should not be empty"),
                err -> assertThat(err).isEqualTo("Error"));
    }

    @Test
    void flatMapValueWithResult_empty_shouldRemainEmpty() {
        OptionalResult<Integer, String> result =
                OptionalResult.<String, String>empty()
                        .flatMapValueWithResult(val -> fail("Should not be run"));
        result.consumeEither(
                val -> fail("Should not have value"),
                () -> {},
                err -> fail("Expected no error"));
    }

    @Test
    void flatMapValueWithResult_error_shouldKeepOriginalError() {
        OptionalResult<Integer, String> result =
                OptionalResult.<String, String>error("OriginalError")
                        .flatMapValueWithResult(val -> fail("Should not be run"));
        result.consumeEither(
                val -> fail("Should not have value"),
                () -> fail("Should not be empty"),
                err -> assertThat(err).isEqualTo("OriginalError"));
    }

    @Test
    void flatMapValue_success_nullFunctionGivesNPE() {
        OptionalResult<String, String> result = OptionalResult.success("Success");
        assertThatThrownBy(() -> result.flatMapValueWithResult(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void flatMapValue_success_nullReturnFromFunctionGivesNPE() {
        OptionalResult<String, String> result = OptionalResult.success("Success");
        assertThatThrownBy(() -> result.flatMapValueWithResult(val -> null))
                .isInstanceOf(NullPointerException.class);
    }
}