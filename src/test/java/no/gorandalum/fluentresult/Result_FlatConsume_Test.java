package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class Result_FlatConsume_Test {

    @Test
    void flatConsume_success_shouldReturnUnalteredInstance() {
        Result.success("Success")
                .flatConsume(val -> VoidResult.success())
                .consumeEither(
                        val -> assertThat(val).isEqualTo("Success"),
                        err -> fail("Should not be error")
                );
    }

    @Test
    void flatConsume_success_shouldReturnErrorFromVoidResult() {
        Result.success("Success")
                .flatConsume(val -> VoidResult.error("Error"))
                .consumeEither(
                        val -> fail("Should not have value"),
                        err -> assertThat(err).isEqualTo("Error")
                );
    }

    @Test
    void flatConsume_error_shouldNotRunSupplier() {
        Result.error("Error")
                .flatConsume(val -> { throw new RuntimeException(); })
                .consumeEither(
                        val -> fail("Should not have value"),
                        err -> assertThat(err).isEqualTo("Error")
                );
    }

    @Test
    void flatConsume_success_nullFunctionGivesNPE() {
        Result<String, String> result = Result.success("Success");
        assertThatThrownBy(() -> result.flatConsume(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void flatConsume_success_nullValueFromFunctionGivesNPE() {
        Result<String, String> result = Result.success("Success");
        assertThatThrownBy(() -> result.flatConsume(val -> null))
                .isInstanceOf(NullPointerException.class);
    }
}