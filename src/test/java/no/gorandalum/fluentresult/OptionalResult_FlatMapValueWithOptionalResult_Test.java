package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class OptionalResult_FlatMapValueWithOptionalResult_Test {

    @Test
    void flatMapValueWithOptionalResult_success_successfullyMapValue() {
        OptionalResult<Integer, String> result =
                OptionalResult.<String, String>success("Success")
                        .flatMapValueWithOptionalResult(val ->
                                OptionalResult.success(val.length()));
        result.consumeEither(
                val -> assertThat(val).isEqualTo(7),
                () -> fail("Should not be empty"),
                err -> fail("Expected no error"));
    }

    @Test
    void flatMapValueWithOptionalResult_success_mapValueToEmpty() {
        OptionalResult<Integer, String> result =
                OptionalResult.<String, String>success("Success")
                        .flatMapValueWithOptionalResult(val -> OptionalResult.empty());
        result.consumeEither(
                val -> fail("Should not have value"),
                () -> {},
                err -> fail("Expected no error"));
    }

    @Test
    void flatMapValueWithOptionalResult_success_mapValueToError() {
        OptionalResult<Integer, String> result =
                OptionalResult.<String, String>success("Success")
                        .flatMapValueWithOptionalResult(val -> OptionalResult.error("Error"));
        result.consumeEither(
                val -> fail("Should not have value"),
                () -> fail("Should not be empty"),
                err -> assertThat(err).isEqualTo("Error"));
    }

    @Test
    void flatMapValueWithOptionalResult_empty_shouldRemainEmpty() {
        OptionalResult<Integer, String> result =
                OptionalResult.<String, String>empty()
                        .flatMapValueWithOptionalResult(val -> fail("Should not be run"));
        result.consumeEither(
                val -> fail("Should not have value"),
                () -> {},
                err -> fail("Expected no error"));
    }

    @Test
    void flatMapValueWithOptionalResult_error_shouldKeepOriginalError() {
        OptionalResult<Integer, String> result =
                OptionalResult.<String, String>error("OriginalError")
                        .flatMapValueWithOptionalResult(val -> fail("Should not be run"));
        result.consumeEither(
                val -> fail("Should not have value"),
                () -> fail("Should not be empty"),
                err -> assertThat(err).isEqualTo("OriginalError"));
    }

    @Test
    void flatMapValue_success_nullFunctionGivesNPE() {
        OptionalResult<String, String> result = OptionalResult.success("Success");
        assertThatThrownBy(() -> result.flatMapValueWithOptionalResult(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void flatMapValue_success_nullReturnFromFunctionGivesNPE() {
        OptionalResult<String, String> result = OptionalResult.success("Success");
        assertThatThrownBy(() -> result.flatMapValueWithOptionalResult(val -> null))
                .isInstanceOf(NullPointerException.class);
    }
}