package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class Result_ToString_Test {

    @Test
    void toString_success_shouldReturnCorrectStringRepresentation() {
        Result<String, String> result = Result.success("Success");
        assertThat(result.toString()).isEqualTo("Result[Value: Success]");
    }

    @Test
    void toString_error_shouldReturnCorrectStringRepresentation() {
        Result<String, String> result = Result.error("Fault");
        assertThat(result.toString()).isEqualTo("Result[Error: Fault]");
    }
}