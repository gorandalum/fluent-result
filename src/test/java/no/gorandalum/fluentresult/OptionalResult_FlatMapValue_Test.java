package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class OptionalResult_FlatMapValue_Test {

    @Test
    void flatMapValue_success_successfullyMapValue() {
        OptionalResult<Integer, String> result =
                OptionalResult.<String, String>success("Success")
                        .flatMapValue(val -> Optional.of(val.length()));
        result.consumeEither(
                val -> assertThat(val).isEqualTo(7),
                () -> fail("Should not be empty"),
                err -> fail("Expected no error"));
    }

    @Test
    void flatMapValue_success_mapToEmptyOptionalGivesEmpty() {
        OptionalResult<String, String> result =
                OptionalResult.<String, String>success("Success")
                        .flatMapValue(val -> Optional.empty());
        result.consumeEither(
                val -> fail("Should not have value"),
                () -> {},
                err -> fail("Expected no error"));
    }

    @Test
    void flatMapValue_empty_shouldRemainEmpty() {
        OptionalResult<Integer, String> result =
                OptionalResult.<String, String>empty()
                        .flatMapValue(val -> Optional.of(val.length()));
        result.consumeEither(
                val -> fail("Should not have value"),
                () -> {},
                err -> fail("Expected no error"));
    }

    @Test
    void flatMapValue_error_shouldKeepOriginalError() {
        OptionalResult<Integer, String> result =
                OptionalResult.<String, String>error("OriginalError")
                        .flatMapValue(val -> Optional.of(val.length()));
        result.consumeEither(
                val -> fail("Should not have value"),
                () -> fail("Should not be empty"),
                err -> assertThat(err).isEqualTo("OriginalError"));
    }

    @Test
    void flatMapValue_empty_shouldNotRunFunctionWhenEmpty() {
        OptionalResult.empty().flatMapValue(
                val -> {
                    throw new RuntimeException();
                });
    }

    @Test
    void flatMapValue_error_shouldNotRunFunctionWhenError() {
        OptionalResult.error("OriginalError").flatMapValue(
                val -> {
                    throw new RuntimeException();
                });
    }

    @Test
    void flatMapValue_success_nullFunctionGivesNPE() {
        OptionalResult<String, String> result = OptionalResult.success("Success");
        assertThatThrownBy(() -> result.flatMapValue(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void flatMapValue_success_nullReturnFromFunctionGivesNPE() {
        OptionalResult<String, String> result = OptionalResult.success("Success");
        assertThatThrownBy(() -> result.flatMapValue(val -> null))
                .isInstanceOf(NullPointerException.class);
    }
}