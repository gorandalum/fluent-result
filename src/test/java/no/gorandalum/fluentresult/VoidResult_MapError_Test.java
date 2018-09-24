package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class VoidResult_MapError_Test {

    @Test
    void mapError_success_shouldNotMap() {
        VoidResult<String> result = VoidResult.success();
        result.mapError(err -> fail("Should not run")).consumeEither(
                () -> {},
                err -> fail("Should not be error")
        );
    }

    @Test
    void mapError_error_shouldMapError() {
        VoidResult<String> result = VoidResult.error("Error");
        result.mapError(String::length)
                .consumeEither(
                        () -> fail("Should not have value"),
                        err -> assertThat(err).isEqualTo(5)
                );
    }

    @Test
    void mapError_error_nullValueFromFunctionGivesNPE() {
        VoidResult<String> result = VoidResult.error("Error");
        assertThatThrownBy(() -> result.mapError(val -> null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void mapError_success_nullFunctionGivesNPE() {
        VoidResult<String> result = VoidResult.success();
        assertThatThrownBy(() -> result.mapError(null))
                .isInstanceOf(NullPointerException.class);
    }
}