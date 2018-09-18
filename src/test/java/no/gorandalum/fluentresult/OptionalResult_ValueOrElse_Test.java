package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OptionalResult_ValueOrElse_Test {

    @Test
    void orElse_success_shouldRespondWithSuccessValue() {
        OptionalResult<String, Object> result = OptionalResult.success("Success");
        assertThat(result.valueOrElse("Other")).isEqualTo("Success");
    }

    @Test
    void orElse_error_shouldRespondWithOtherValue() {
        OptionalResult<String, String> result = OptionalResult.error("Error");
        assertThat(result.valueOrElse("Other")).isEqualTo("Other");
    }

    @Test
    void orElse_error_shouldRespondWithNullValue() {
        OptionalResult<String, String> result = OptionalResult.error("Error");
        assertThat(result.valueOrElse(null)).isNull();
    }
}