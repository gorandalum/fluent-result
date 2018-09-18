package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OptionalResult_ValueOrElseThrow_Test {

    @Test
    void orElseThrow_success_shouldRespondWithSuccessValue() {
        OptionalResult<String, String> result = OptionalResult.success("Success");
        assertThat(result.valueOrElseThrow(IllegalArgumentException::new))
                .isEqualTo("Success");
    }

    @Test
    void orElseThrow_error_shouldThrowGivenException() {
        OptionalResult<String, String> result = OptionalResult.error("Error");
        assertThatThrownBy(() -> result.valueOrElseThrow(IllegalArgumentException::new))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void orElseThrow_error_nullExceptionSupplierGivesNPE() {
        OptionalResult<String, String> result = OptionalResult.error("Error");
        assertThatThrownBy(() -> result.valueOrElseThrow(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void orElseThrow_error_nullExceptionFromSupplierGivesNPE() {
        OptionalResult<String, String> result = OptionalResult.error("Error");
        assertThatThrownBy(() -> result.valueOrElseThrow(() -> null))
                .isInstanceOf(NullPointerException.class);
    }
}