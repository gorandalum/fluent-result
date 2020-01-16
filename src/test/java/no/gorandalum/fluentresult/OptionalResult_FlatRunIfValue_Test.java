package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class OptionalResult_FlatRunIfValue_Test {

    @Test
    void flatRunIfValue_success_shouldReturnUnalteredInstance() {
       OptionalResult.success("Success")
                .flatRunIfValue(VoidResult::success)
                .consumeEither(
                        val -> assertThat(val).isEqualTo("Success"),
                        () -> fail("Should not be empty"),
                        err -> fail("Should not be error")
                );
    }

    @Test
    void flatRunIfValue_success_shouldReturnErrorFromVoidResult() {
        OptionalResult.success("Success")
                .flatRunIfValue(() -> VoidResult.error("Error"))
                .consumeEither(
                        val -> fail("Should not have value"),
                        () -> fail("Should not be empty"),
                        err -> assertThat(err).isEqualTo("Error")
                );
    }

    @Test
    void flatRunIfValue_empty_shouldNotRunSupplier() {
        OptionalResult.empty()
                .flatRunIfValue(() -> { throw new RuntimeException(); })
                .consumeEither(
                        val -> fail("Should not be success"),
                        () -> {},
                        err -> fail("Should not be error")
                );
    }

    @Test
    void flatRunIfValue_error_shouldNotRunSupplier() {
        OptionalResult.error("Error")
                .flatRunIfValue(() -> { throw new RuntimeException(); })
                .consumeEither(
                        val -> fail("Should not have value"),
                        () -> fail("Should not be empty"),
                        err -> assertThat(err).isEqualTo("Error")
                );
    }

    @Test
    void flatRunIfValue_success_nullFunctionGivesNPE() {
        OptionalResult<String, String> result = OptionalResult.success("Success");
        assertThatThrownBy(() -> result.flatRunIfValue(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void flatRunIfValue_success_nullValueFromFunctionGivesNPE() {
        OptionalResult<String, String> result = OptionalResult.success("Success");
        assertThatThrownBy(() -> result.flatRunIfValue(() -> null))
                .isInstanceOf(NullPointerException.class);
    }
}