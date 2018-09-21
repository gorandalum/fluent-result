package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class OptionalResult_MapValueToOptional_Test {

    @Test
    void mapValueToOptional_success_successfullyMapValue() {
        OptionalResult<Integer, String> result =
                OptionalResult.<String, String>success("Success")
                        .mapValueToOptional(val -> Optional.of(val.length()));
        result.consumeEither(
                val -> assertThat(val).isEqualTo(7),
                () -> fail("Should not be empty"),
                err -> fail("Expected no error"));
    }

    @Test
    void mapValueToOptional_success_mapToEmptyOptionalGivesEmpty() {
        OptionalResult<String, String> result =
                OptionalResult.<String, String>success("Success")
                        .mapValueToOptional(val -> Optional.empty());
        result.consumeEither(
                val -> fail("Should not have value"),
                () -> {},
                err -> fail("Expected no error"));
    }

    @Test
    void mapValueToOptional_empty_shouldRemainEmpty() {
        OptionalResult<Integer, String> result =
                OptionalResult.<String, String>empty()
                        .mapValueToOptional(val -> Optional.of(val.length()));
        result.consumeEither(
                val -> fail("Should not have value"),
                () -> {},
                err -> fail("Expected no error"));
    }

    @Test
    void mapValueToOptional_error_shouldKeepOriginalError() {
        OptionalResult<Integer, String> result =
                OptionalResult.<String, String>error("OriginalError")
                        .mapValueToOptional(val -> Optional.of(val.length()));
        result.consumeEither(
                val -> fail("Should not have value"),
                () -> fail("Should not be empty"),
                err -> assertThat(err).isEqualTo("OriginalError"));
    }

    @Test
    void mapValueToOptional_empty_shouldNotRunFunctionWhenEmpty() {
        OptionalResult.empty().mapValueToOptional(
                val -> {
                    throw new RuntimeException();
                });
    }

    @Test
    void mapValueToOptional_error_shouldNotRunFunctionWhenError() {
        OptionalResult.error("OriginalError").mapValueToOptional(
                val -> {
                    throw new RuntimeException();
                });
    }

    @Test
    void mapValueToOptional_success_nullFunctionGivesNPE() {
        OptionalResult<String, String> result = OptionalResult.success("Success");
        assertThatThrownBy(() -> result.mapValueToOptional(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void mapValueToOptional_success_nullReturnFromFunctionGivesNPE() {
        OptionalResult<String, String> result = OptionalResult.success("Success");
        assertThatThrownBy(() -> result.mapValueToOptional(val -> null))
                .isInstanceOf(NullPointerException.class);
    }
}