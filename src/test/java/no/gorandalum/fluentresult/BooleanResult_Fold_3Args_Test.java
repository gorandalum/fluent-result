package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class BooleanResult_Fold_3Args_Test {

    @Test
    void fold_success_shouldGiveSuccessValueFunctionResult() {
        Integer folded = BooleanResult.success(true).fold(
                () -> 7,
                () -> fail("Should not run"),
                err -> fail("Should not run"));
        assertThat(folded).isEqualTo(7);
    }

    @Test
    void fold_empty_shouldGiveEmptySupplierResult() {
        Integer folded = BooleanResult.success(false).fold(
                () -> fail("Should not run"),
                () -> 6,
                err -> fail("Should not run"));
        assertThat(folded).isEqualTo(6);
    }

    @Test
    void fold_error_shouldGiveErrorValueFunctionResult() {
        Integer folded = BooleanResult.error("Error").fold(
                () -> fail("Should not run"),
                () -> fail("Should not run"),
                err -> 5);
        assertThat(folded).isEqualTo(5);
    }

    @Test
    void fold_success_nullValueFunctionGivesNPE() {
        BooleanResult<String> result = BooleanResult.success(true);
        assertThatThrownBy(() -> result.fold(null, () -> 6, err -> 5))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void fold_success_nullEmptySupplierGivesNPE() {
        BooleanResult<String> result = BooleanResult.success(true);
        assertThatThrownBy(() -> result.fold(() -> 7, null, err -> 5))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void fold_success_nullErrorFunctionGivesNPE() {
        BooleanResult<String> result = BooleanResult.success(true);
        assertThatThrownBy(() -> result.fold(() -> 7, () -> 6, null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void fold_success_canReturnNull() {
        BooleanResult<Integer> result = BooleanResult.success(true);
        Integer folded = result.fold(() -> null, () -> 6, err -> 5);
        assertThat(folded).isNull();
    }

    @Test
    void fold_empty_canReturnNull() {
        BooleanResult<Integer> result = BooleanResult.success(false);
        Integer folded = result.fold(() -> 7, () -> null, err -> 5);
        assertThat(folded).isNull();
    }

    @Test
    void fold_error_canReturnNull() {
        BooleanResult<Integer> result = BooleanResult.error(7);
        Integer folded = result.fold(() -> 7, () -> 6, err -> null);
        assertThat(folded).isNull();
    }
}