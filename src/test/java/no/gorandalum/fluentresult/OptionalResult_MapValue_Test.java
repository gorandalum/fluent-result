package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class OptionalResult_MapValue_Test {

    @Test
    void mapValue_success_successfullyMapValue() {
        OptionalResult<Integer, String> result =
                OptionalResult.<String, String>success("Success")
                        .mapValue(String::length);
        result.consumeEither(
                val -> assertThat(val).isEqualTo(7),
                () -> fail("Should not be empty"),
                err -> fail("Expected no error"));
    }

    @Test
    void mapValue_success_mapToNullGivesEmpty() {
        OptionalResult<String, String> result =
                OptionalResult.<String, String>success("Success")
                        .mapValue(val -> null);
        result.consumeEither(
                val -> fail("Should not have value"),
                () -> {},
                err -> fail("Expected no error"));
    }

    @Test
    void mapValue_empty_shouldRemainEmpty() {
        OptionalResult<Integer, String> result =
                OptionalResult.<String, String>empty()
                        .mapValue(String::length);
        result.consumeEither(
                val -> fail("Should not have value"),
                () -> {},
                err -> fail("Expected no error"));
    }

    @Test
    void mapValue_error_shouldKeepOriginalError() {
        OptionalResult<Integer, String> result =
                OptionalResult.<String, String>error("OriginalError")
                        .mapValue(String::length);
        result.consumeEither(
                val -> fail("Should not have value"),
                () -> fail("Should not be empty"),
                err -> assertThat(err).isEqualTo("OriginalError"));
    }

    @Test
    void mapValue_empty_shouldNotRunFunctionWhenEmpty() {
        OptionalResult.empty().mapValue(
                val -> {
                    throw new RuntimeException();
                });
    }

    @Test
    void mapValue_error_shouldNotRunFunctionWhenError() {
        OptionalResult.error("OriginalError").mapValue(
                val -> {
                    throw new RuntimeException();
                });
    }

    @Test
    void mapValue_success_nullFunctionGivesNPE() {
        OptionalResult<String, String> result = OptionalResult.success("Success");
        assertThatThrownBy(() -> result.mapValue(null))
                .isInstanceOf(NullPointerException.class);
    }
}