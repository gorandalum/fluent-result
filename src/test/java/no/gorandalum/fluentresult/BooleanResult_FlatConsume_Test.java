package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class BooleanResult_FlatConsume_Test {

    @Test
    void flatConsume_success_shouldReturnUnalteredInstance() {
       BooleanResult.successTrue()
                .flatConsume(val -> VoidResult.success())
                .consumeEither(
                        val -> assertThat(val).isEqualTo(true),
                        err -> fail("Should not be error")
                );
    }

    @Test
    void flatConsume_success_shouldReturnErrorFromVoidResult() {
        BooleanResult.successTrue()
                .flatConsume(val -> VoidResult.error("Error"))
                .consumeEither(
                        val -> fail("Should not have value"),
                        err -> assertThat(err).isEqualTo("Error")
                );
    }

    @Test
    void flatConsume_error_shouldNotRunSupplier() {
        BooleanResult.error("Error")
                .flatConsume(val -> { throw new RuntimeException(); })
                .consumeEither(
                        val -> fail("Should not have value"),
                        err -> assertThat(err).isEqualTo("Error")
                );
    }

    @Test
    void flatConsume_success_nullFunctionGivesNPE() {
        BooleanResult<String> result = BooleanResult.successTrue();
        assertThatThrownBy(() -> result.flatConsume(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void flatConsume_success_nullValueFromFunctionGivesNPE() {
        BooleanResult<String> result = BooleanResult.successTrue();
        assertThatThrownBy(() -> result.flatConsume(val -> null))
                .isInstanceOf(NullPointerException.class);
    }
}