package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class OptionalResult_FlatConsume_Test {

    @Test
    void flatConsume_success_shouldReturnUnalteredInstance() {
       OptionalResult.success("Success")
                .flatConsume(val -> VoidResult.success())
                .consumeEither(
                        val -> assertThat(val).isEqualTo("Success"),
                        () -> fail("Should not be empty"),
                        err -> fail("Should not be error")
                );
    }

    @Test
    void flatConsume_success_shouldReturnErrorFromVoidResult() {
        OptionalResult.success("Success")
                .flatConsume(val -> VoidResult.error("Error"))
                .consumeEither(
                        val -> fail("Should not have value"),
                        () -> fail("Should not be empty"),
                        err -> assertThat(err).isEqualTo("Error")
                );
    }

    @Test
    void flatConsume_empty_shouldReturnUnalteredInstance() {
        OptionalResult.empty()
                .flatConsume(val -> VoidResult.success())
                .consumeEither(
                        val -> fail("Should not be success"),
                        () -> {},
                        err -> fail("Should not be error")
                );
    }

    @Test
    void flatConsume_empty_shouldReturnErrorFromVoidResult() {
        OptionalResult.empty()
                .flatConsume(val -> VoidResult.error("Error"))
                .consumeEither(
                        val -> fail("Should not have value"),
                        () -> fail("Should not be empty"),
                        err -> assertThat(err).isEqualTo("Error")
                );
    }

    @Test
    void flatConsume_error_shouldNotRunSupplier() {
        OptionalResult.error("Error")
                .flatConsume(val -> { throw new RuntimeException(); })
                .consumeEither(
                        val -> fail("Should not have value"),
                        () -> fail("Should not be empty"),
                        err -> assertThat(err).isEqualTo("Error")
                );
    }

    @Test
    void flatConsume_success_nullFunctionGivesNPE() {
        OptionalResult<String, String> result = OptionalResult.success("Success");
        assertThatThrownBy(() -> result.flatConsume(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void flatConsume_success_nullValueFromFunctionGivesNPE() {
        OptionalResult<String, String> result = OptionalResult.success("Success");
        assertThatThrownBy(() -> result.flatConsume(val -> null))
                .isInstanceOf(NullPointerException.class);
    }
}