package no.gorandalum.fluentresult;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

class Result_OrElse_Test {

    @Test
    void orElse_success_shouldRespondWithSuccessValue() {
        Result<String, Object> result = Result.success("Success");
        assertThat(result.orElse("Other")).isEqualTo("Success");
    }

    @Test
    void orElse_error_shouldRespondWithOtherValue() {
        Result<String, String> result = Result.error("Error");
        assertThat(result.orElse("Other")).isEqualTo("Other");
    }

    @Test
    void orElse_error_shouldRespondWithNullValue() {
        Result<String, String> result = Result.error("Error");
        assertThat(result.orElse(null)).isNull();
    }
}