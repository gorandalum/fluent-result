package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class OptionalResult_FlatConsumeValue_Test {

    @Test
    void flatConsumeValue_success_shouldReturnUnalteredInstance() {
       OptionalResult.success("Success")
                .flatConsumeValue(val -> VoidResult.success())
                .consumeEither(
                        val -> assertThat(val).isEqualTo("Success"),
                        () -> fail("Should not be empty"),
                        err -> fail("Should not be error")
                );
    }

    @Test
    void flatConsumeValue_success_shouldReturnErrorFromVoidResult() {
        OptionalResult.success("Success")
                .flatConsumeValue(val -> VoidResult.error("Error"))
                .consumeEither(
                        val -> fail("Should not have value"),
                        () -> fail("Should not be empty"),
                        err -> assertThat(err).isEqualTo("Error")
                );
    }

    @Test
    void flatConsumeValue_empty_shouldNotRunSupplier() {
        OptionalResult.empty()
                .flatConsumeValue(val -> { throw new RuntimeException(); })
                .consumeEither(
                        val -> fail("Should not be success"),
                        () -> {},
                        err -> fail("Should not be error")
                );
    }

    @Test
    void flatConsumeValue_error_shouldNotRunSupplier() {
        OptionalResult.error("Error")
                .flatConsumeValue(val -> { throw new RuntimeException(); })
                .consumeEither(
                        val -> fail("Should not have value"),
                        () -> fail("Should not be empty"),
                        err -> assertThat(err).isEqualTo("Error")
                );
    }

    @Test
    void flatConsumeValue_success_nullFunctionGivesNPE() {
        OptionalResult<String, String> result = OptionalResult.success("Success");
        assertThatThrownBy(() -> result.flatConsumeValue(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void flatConsumeValue_success_nullValueFromFunctionGivesNPE() {
        OptionalResult<String, String> result = OptionalResult.success("Success");
        assertThatThrownBy(() -> result.flatConsumeValue(val -> null))
                .isInstanceOf(NullPointerException.class);
    }
}