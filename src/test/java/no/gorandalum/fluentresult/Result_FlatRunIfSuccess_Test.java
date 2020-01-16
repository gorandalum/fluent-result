package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class Result_FlatRunIfSuccess_Test {

    @Test
    void flatRunIfSuccess_success_shouldReturnUnalteredInstance() {
        Result.success("Success")
                .flatRunIfSuccess(VoidResult::success)
                .consumeEither(
                        val -> assertThat(val).isEqualTo("Success"),
                        err -> fail("Should not be error")
                );
    }

    @Test
    void flatRunIfSuccess_success_shouldReturnErrorFromVoidResult() {
        Result.success("Success")
                .flatRunIfSuccess(() -> VoidResult.error("Error"))
                .consumeEither(
                        val -> fail("Should not have value"),
                        err -> assertThat(err).isEqualTo("Error")
                );
    }

    @Test
    void flatRunIfSuccess_error_shouldNotRunSupplier() {
        Result.error("Error")
                .flatRunIfSuccess(() -> { throw new RuntimeException(); })
                .consumeEither(
                        val -> fail("Should not have value"),
                        err -> assertThat(err).isEqualTo("Error")
                );
    }

    @Test
    void flatRunIfSuccess_success_nullFunctionGivesNPE() {
        Result<String, String> result = Result.success("Success");
        assertThatThrownBy(() -> result.flatRunIfSuccess(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void flatRunIfSuccess_success_nullValueFromFunctionGivesNPE() {
        Result<String, String> result = Result.success("Success");
        assertThatThrownBy(() -> result.flatRunIfSuccess(() -> null))
                .isInstanceOf(NullPointerException.class);
    }
}