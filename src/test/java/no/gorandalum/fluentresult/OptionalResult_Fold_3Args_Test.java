package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.fail;

class OptionalResult_Fold_3Args_Test {

    @Test
    void fold_success_shouldGiveSuccessValueFunctionResult() {
        Integer folded = OptionalResult.success("Success").fold(
                String::length,
                () -> fail("Should not run"),
                err -> fail("Should not run"));
        assertThat(folded).isEqualTo(7);
    }

    @Test
    void fold_empty_shouldGiveEmptySupplierResult() {
        Integer folded = OptionalResult.empty().fold(
                val -> fail("Should not run"),
                () -> 6,
                err -> fail("Should not run"));
        assertThat(folded).isEqualTo(6);
    }

    @Test
    void fold_error_shouldGiveErrorValueFunctionResult() {
        Integer folded = OptionalResult.error("Error").fold(
                val -> fail("Should not run"),
                () -> fail("Should not run"),
                err -> 5);
        assertThat(folded).isEqualTo(5);
    }

    @Test
    void fold_success_nullValueFunctionGivesNPE() {
        OptionalResult<String, String> result = OptionalResult.success("Success");
        assertThatThrownBy(() -> result.fold(null, () -> 6, err -> 5))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void fold_success_nullEmptySupplierGivesNPE() {
        OptionalResult<String, String> result = OptionalResult.success("Success");
        assertThatThrownBy(() -> result.fold(val -> 7, null, err -> 5))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void fold_success_nullErrorFunctionGivesNPE() {
        OptionalResult<String, String> result = OptionalResult.success("Success");
        assertThatThrownBy(() -> result.fold(val -> 7, () -> 6, null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void fold_success_canReturnNull() {
        OptionalResult<String, Integer> result = OptionalResult.success("Success");
        Integer folded = result.fold(val -> null, () -> 6, err -> 5);
        assertThat(folded).isNull();
    }

    @Test
    void fold_empty_canReturnNull() {
        OptionalResult<String, Integer> result = OptionalResult.empty();
        Integer folded = result.fold(val -> 7, () -> null, err -> 5);
        assertThat(folded).isNull();
    }

    @Test
    void fold_error_canReturnNull() {
        OptionalResult<String, Integer> result = OptionalResult.error(7);
        Integer folded = result.fold(val -> 7, () -> 6, err -> null);
        assertThat(folded).isNull();
    }
}