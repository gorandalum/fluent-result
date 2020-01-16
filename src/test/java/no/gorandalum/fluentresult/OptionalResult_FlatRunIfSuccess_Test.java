package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class OptionalResult_FlatRunIfSuccess_Test {

    @Test
    void flatRunIfSuccess_success_shouldReturnUnalteredInstance() {
       OptionalResult.success("Success")
                .flatRunIfSuccess(VoidResult::success)
                .consumeEither(
                        val -> assertThat(val).isEqualTo("Success"),
                        () -> fail("Should not be empty"),
                        err -> fail("Should not be error")
                );
    }

    @Test
    void flatRunIfSuccess_success_shouldReturnErrorFromVoidResult() {
        OptionalResult.success("Success")
                .flatRunIfSuccess(() -> VoidResult.error("Error"))
                .consumeEither(
                        val -> fail("Should not have value"),
                        () -> fail("Should not be empty"),
                        err -> assertThat(err).isEqualTo("Error")
                );
    }

    @Test
    void flatRunIfSuccess_empty_shouldReturnUnalteredInstance() {
        OptionalResult.empty()
                .flatRunIfSuccess(VoidResult::success)
                .consumeEither(
                        val -> fail("Should not be success"),
                        () -> {},
                        err -> fail("Should not be error")
                );
    }

    @Test
    void flatRunIfSuccess_empty_shouldReturnErrorFromVoidResult() {
        OptionalResult.empty()
                .flatRunIfSuccess(() -> VoidResult.error("Error"))
                .consumeEither(
                        val -> fail("Should not have value"),
                        () -> fail("Should not be empty"),
                        err -> assertThat(err).isEqualTo("Error")
                );
    }

    @Test
    void flatRunIfSuccess_error_shouldNotRunSupplier() {
        OptionalResult.error("Error")
                .flatRunIfSuccess(() -> { throw new RuntimeException(); })
                .consumeEither(
                        val -> fail("Should not have value"),
                        () -> fail("Should not be empty"),
                        err -> assertThat(err).isEqualTo("Error")
                );
    }

    @Test
    void flatRunIfSuccess_success_nullFunctionGivesNPE() {
        OptionalResult<String, String> result = OptionalResult.success("Success");
        assertThatThrownBy(() -> result.flatRunIfSuccess(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void flatRunIfSuccess_success_nullValueFromFunctionGivesNPE() {
        OptionalResult<String, String> result = OptionalResult.success("Success");
        assertThatThrownBy(() -> result.flatRunIfSuccess(() -> null))
                .isInstanceOf(NullPointerException.class);
    }
}