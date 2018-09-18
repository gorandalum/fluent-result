package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OptionalResult_ValueOrElseGet_Test {

    @Test
    void orElseGet_success_shouldRespondWithSuccessValue() {
        OptionalResult<String, String> result = OptionalResult.success("Success");
        assertThat(result.valueOrElseGet(() -> "Other")).isEqualTo("Success");
    }

    @Test
    void orElse_error_shouldRespondWithOtherValue() {
        OptionalResult<String, String> result = OptionalResult.error("Error");
        assertThat(result.valueOrElseGet(() -> "Other")).isEqualTo("Other");
    }

    @Test
    void orElse_error_shouldRespondWithNullValue() {
        OptionalResult<String, String> result = OptionalResult.error("Error");
        assertThat(result.valueOrElseGet(() -> null)).isNull();
    }

    @Test
    void orElse_success_nullFunctionGivesNPE() {
        OptionalResult<String, Object> result = OptionalResult.success("Success");
        assertThatThrownBy(() -> result.valueOrElseGet(null))
                .isInstanceOf(NullPointerException.class);
    }
}