package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OptionalResult_FlatReplaceEmptyWithResult_Test {

    @Test
    void flatReplaceEmptyWithResult_empty_shouldReturnSupplierValue() {
        OptionalResult<String, String> result = OptionalResult.empty();
        result.flatReplaceEmptyWithResult(() -> Result.success("Success"))
                .consumeEither(
                        val -> assertThat(val).isEqualTo("Success"),
                        err -> fail("Should not be error")
                );
    }

    @Test
    void flatReplaceEmptyWithResult_empty_shouldReturnSupplierError() {
        OptionalResult<String, String> result = OptionalResult.empty();
        result.flatReplaceEmptyWithResult(() -> Result.error("Failed"))
                .consumeEither(
                        val -> fail("Should not be value"),
                        err -> assertThat(err).isEqualTo("Failed")
                );
    }

    @Test
    void flatReplaceEmptyWithResult_success_shouldRemainSuccessValue() {
        OptionalResult<String, String> result = OptionalResult.success("Success 1");
        result.flatReplaceEmptyWithResult(() -> Result.success("Success 2"))
                .consumeEither(
                        val -> assertThat(val).isEqualTo("Success 1"),
                        err -> fail("Should not be error")
                );
    }

    @Test
    void flatReplaceEmptyWithResult_success_shouldRemainErrorValue() {
        OptionalResult<String, String> result = OptionalResult.error("Failed");
        result.flatReplaceEmptyWithResult(() -> Result.error("Success"))
                .consumeEither(
                        val -> fail("Should not be success value"),
                        err -> assertThat(err).isEqualTo("Failed")
                );
    }

    @Test
    void flatReplaceEmptyWithResult_empty_nullFunctionGivesNPE() {
        OptionalResult<String, String> result = OptionalResult.empty();
        assertThatThrownBy(() -> result.flatReplaceEmptyWithResult(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void flatReplaceEmptyWithResult_emtpy_nullReturnFromSupplierGivesNPE() {
        OptionalResult<String, String> result = OptionalResult.empty();
        assertThatThrownBy(() -> result.flatReplaceEmptyWithResult(() -> null))
                .isInstanceOf(NullPointerException.class);
    }
}
