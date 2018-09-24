package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class OptionalResult_MapError_Test {

    @Test
    void map_success_shouldNotMap() {
        OptionalResult<String, String> result = OptionalResult.success("Success");
        result.mapError(err -> fail("Should not run")).consumeEither(
                val -> assertThat(val).isEqualTo("Success"),
                () -> fail("Should not be empty"),
                err -> fail("Should not be error")
        );
    }

    @Test
    void map_empty_shouldNotMap() {
        OptionalResult<String, String> result = OptionalResult.empty();
        result.mapError(err -> fail("Should not run")).consumeEither(
                val -> fail("Should not have value"),
                () -> {},
                err -> fail("Should not be error")
        );
    }

    @Test
    void map_error_shouldMapError() {
        OptionalResult<String, String> result = OptionalResult.error("Error");
        result.mapError(String::length)
                .consumeEither(
                        val -> fail("Should not have value"),
                        err -> assertThat(err).isEqualTo(5)
                );
    }

    @Test
    void map_error_nullValueFromFunctionGivesNPE() {
        OptionalResult<String, String> result = OptionalResult.error("Error");
        assertThatThrownBy(() -> result.mapError(val -> null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void map_success_nullFunctionGivesNPE() {
        OptionalResult<String, String> result = OptionalResult.success("Success");
        assertThatThrownBy(() -> result.mapError(null))
                .isInstanceOf(NullPointerException.class);
    }
}