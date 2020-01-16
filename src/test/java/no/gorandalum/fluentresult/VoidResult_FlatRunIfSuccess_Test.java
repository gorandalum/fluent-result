package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class VoidResult_FlatRunIfSuccess_Test {

    @Test
    void flatRunIfSuccess_success_shouldReturnUnalteredInstance() {
       VoidResult.success()
                .flatRunIfSuccess(VoidResult::success)
                .consumeEither(
                        () -> {},
                        err -> fail("Should not be error")
                );
    }

    @Test
    void flatRunIfSuccess_success_shouldReturnErrorFromVoidResult() {
        VoidResult.success()
                .flatRunIfSuccess(() -> VoidResult.error("Error"))
                .consumeEither(
                        () -> fail("Should not have value"),
                        err -> assertThat(err).isEqualTo("Error")
                );
    }

    @Test
    void flatRunIfSuccess_error_shouldNotRunSupplier() {
        VoidResult.error("Error")
                .flatRunIfSuccess(() -> { throw new RuntimeException(); })
                .consumeEither(
                        () -> fail("Should not have value"),
                        err -> assertThat(err).isEqualTo("Error")
                );
    }

    @Test
    void flatRunIfSuccess_success_nullFunctionGivesNPE() {
        VoidResult<String> result = VoidResult.success();
        assertThatThrownBy(() -> result.flatRunIfSuccess(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void flatRunIfSuccess_success_nullValueFromFunctionGivesNPE() {
        VoidResult<String> result = VoidResult.success();
        assertThatThrownBy(() -> result.flatRunIfSuccess(() -> null))
                .isInstanceOf(NullPointerException.class);
    }
}