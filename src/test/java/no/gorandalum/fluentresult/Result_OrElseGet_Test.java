package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class Result_OrElseGet_Test {

    @Test
    void orElseGet_success_shouldRespondWithSuccessValue() {
        Result<String, String> result = Result.success("Success");
        assertThat(result.orElseGet(err -> "Other")).isEqualTo("Success");
    }

    @Test
    void orElse_error_shouldRespondWithOtherValue() {
        Result<String, String> result = Result.error("Error");
        assertThat(result.orElseGet(err -> "Other")).isEqualTo("Other");
    }

    @Test
    void orElse_error_shouldRespondWithNullValue() {
        Result<String, String> result = Result.error("Error");
        assertThat(result.orElseGet(err -> null)).isNull();
    }

    @Test
    void orElse_success_nullFunctionGivesNPE() {
        Result<String, Object> result = Result.success("Success");
        assertThatThrownBy(() -> result.orElseGet(null))
                .isInstanceOf(NullPointerException.class);
    }
}