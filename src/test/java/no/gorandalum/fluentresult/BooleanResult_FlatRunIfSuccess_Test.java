package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class BooleanResult_FlatRunIfSuccess_Test {

    @Test
    void flatRunIfSuccess_success_shouldReturnUnalteredInstance() {
       BooleanResult.successTrue()
                .flatRunIfSuccess(VoidResult::success)
                .consumeEither(
                        val -> assertThat(val).isEqualTo(true),
                        err -> fail("Should not be error")
                );
    }

    @Test
    void flatRunIfSuccess_success_shouldReturnErrorFromVoidResult() {
        BooleanResult.successTrue()
                .flatRunIfSuccess(() -> VoidResult.error("Error"))
                .consumeEither(
                        val -> fail("Should not have value"),
                        err -> assertThat(err).isEqualTo("Error")
                );
    }

    @Test
    void flatRunIfSuccess_error_shouldNotRunSupplier() {
        BooleanResult.error("Error")
                .flatRunIfSuccess(() -> { throw new RuntimeException(); })
                .consumeEither(
                        val -> fail("Should not have value"),
                        err -> assertThat(err).isEqualTo("Error")
                );
    }

    @Test
    void flatRunIfSuccess_success_nullFunctionGivesNPE() {
        BooleanResult<String> result = BooleanResult.successTrue();
        assertThatThrownBy(() -> result.flatRunIfSuccess(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void flatRunIfSuccess_success_nullValueFromFunctionGivesNPE() {
        BooleanResult<String> result = BooleanResult.successTrue();
        assertThatThrownBy(() -> result.flatRunIfSuccess(() -> null))
                .isInstanceOf(NullPointerException.class);
    }
}