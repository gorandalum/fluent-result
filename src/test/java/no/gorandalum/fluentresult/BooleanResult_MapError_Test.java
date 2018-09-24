package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class BooleanResult_MapError_Test {

    @Test
    void mapError_success_shouldNotMap() {
        BooleanResult<String> result = BooleanResult.success(true);
        result.mapError(err -> fail("Should not run")).consumeEither(
                val -> assertThat(val).isTrue(),
                err -> fail("Should not be error")
        );
    }

    @Test
    void mapError_error_shouldMapError() {
        BooleanResult<String> result = BooleanResult.error("Error");
        result.mapError(String::length)
                .consumeEither(
                        val -> fail("Should not have value"),
                        err -> assertThat(err).isEqualTo(5)
                );
    }

    @Test
    void mapError_error_nullValueFromFunctionGivesNPE() {
        BooleanResult<String> result = BooleanResult.error("Error");
        assertThatThrownBy(() -> result.mapError(val -> null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void mapError_success_nullFunctionGivesNPE() {
        BooleanResult<String> result = BooleanResult.success(true);
        assertThatThrownBy(() -> result.mapError(null))
                .isInstanceOf(NullPointerException.class);
    }
}