package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OptionalResult_FlatReplaceEmpty_Test {

    @Test
    void flatReplaceEmpty_empty_shouldReturnSupplierValue() {
        OptionalResult<String, String> result = OptionalResult.empty();
        result.flatReplaceEmpty(() -> OptionalResult.success("Success"))
                .consumeEither(
                        val -> assertThat(val).isEqualTo("Success"),
                        () -> fail("Should not be empty"),
                        err -> fail("Should not be error")
                );
    }

    @Test
    void flatReplaceEmpty_empty_shouldReturnSupplierError() {
        OptionalResult<String, String> result = OptionalResult.empty();
        result.flatReplaceEmpty(() -> OptionalResult.error("Failed"))
                .consumeEither(
                        val -> fail("Should not be value"),
                        () -> fail("Should not be empty"),
                        err -> assertThat(err).isEqualTo("Failed")
                );
    }

    @Test
    void flatReplaceEmpty_success_shouldRemainSuccessValue() {
        OptionalResult<String, String> result = OptionalResult.success("Success 1");
        result.flatReplaceEmpty(() -> OptionalResult.success("Success 2"))
                .consumeEither(
                        val -> assertThat(val).isEqualTo("Success 1"),
                        () -> fail("Should not be empty"),
                        err -> fail("Should not be error")
                );
    }

    @Test
    void flatReplaceEmpty_success_shouldRemainErrorValue() {
        OptionalResult<String, String> result = OptionalResult.error("Failed");
        result.flatReplaceEmpty(() -> OptionalResult.success("Success"))
                .consumeEither(
                        val -> fail("Should not be success value"),
                        () -> fail("Should not be empty"),
                        err -> assertThat(err).isEqualTo("Failed")
                );
    }

    @Test
    void flatReplaceEmpty_empty_nullFunctionGivesNPE() {
        OptionalResult<String, String> result = OptionalResult.empty();
        assertThatThrownBy(() -> result.flatReplaceEmpty(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void flatReplaceEmpty_emtpy_nullReturnFromSupplierGivesNPE() {
        OptionalResult<String, String> result = OptionalResult.empty();
        assertThatThrownBy(() -> result.flatReplaceEmpty(() -> null))
                .isInstanceOf(NullPointerException.class);
    }
}
