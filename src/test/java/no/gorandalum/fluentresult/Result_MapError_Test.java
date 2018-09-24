package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class Result_MapError_Test {

    @Test
    void mapError_success_shouldNotMap() {
        Result<String, String> result = Result.success("Success");
        result.mapError(err -> fail("Should not run")).consumeEither(
                val -> assertThat(val).isEqualTo("Success"),
                err -> fail("Should not be error")
        );
    }

    @Test
    void mapError_error_shouldMapError() {
        Result<String, String> result = Result.error("Error");
        result.mapError(String::length)
                .consumeEither(
                        val -> fail("Should not have value"),
                        err -> assertThat(err).isEqualTo(5)
                );
    }

    @Test
    void mapError_error_nullValueFromFunctionGivesNPE() {
        Result<String, String> result = Result.error("Error");
        assertThatThrownBy(() -> result.mapError(val -> null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void mapError_success_nullFunctionGivesNPE() {
        Result<String, String> result = Result.success("Success");
        assertThatThrownBy(() -> result.mapError(null))
                .isInstanceOf(NullPointerException.class);
    }
}