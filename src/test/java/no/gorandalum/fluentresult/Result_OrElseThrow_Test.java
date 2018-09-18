package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class Result_OrElseThrow_Test {

    @Test
    void orElseThrow_success_shouldRespondWithSuccessValue() {
        Result<String, String> result = Result.success("Success");
        assertThat(result.orElseThrow(IllegalArgumentException::new))
                .isEqualTo("Success");
    }

    @Test
    void orElseThrow_error_shouldThrowGivenException() {
        Result<String, String> result = Result.error("Error");
        assertThatThrownBy(() -> result.orElseThrow(IllegalArgumentException::new))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void orElseThrow_error_nullExceptionSupplierGivesNPE() {
        Result<String, String> result = Result.error("Error");
        assertThatThrownBy(() -> result.orElseThrow(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void orElseThrow_error_nullExceptionFromSupplierGivesNPE() {
        Result<String, String> result = Result.error("Error");
        assertThatThrownBy(() -> result.orElseThrow(err -> null))
                .isInstanceOf(NullPointerException.class);
    }
}