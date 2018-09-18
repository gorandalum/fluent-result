package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class BooleanResult_Merge_3Args_Test {

    @Test
    void merge_success_shouldGiveSuccessValueFunctionResult() {
        Integer merged = BooleanResult.success(true).merge(
                () -> 7,
                () -> fail("Should not run"),
                err -> fail("Should not run"));
        assertThat(merged).isEqualTo(7);
    }

    @Test
    void merge_empty_shouldGiveEmptySupplierResult() {
        Integer merged = BooleanResult.success(false).merge(
                () -> fail("Should not run"),
                () -> 6,
                err -> fail("Should not run"));
        assertThat(merged).isEqualTo(6);
    }

    @Test
    void merge_error_shouldGiveErrorValueFunctionResult() {
        Integer merged = BooleanResult.error("Error").merge(
                () -> fail("Should not run"),
                () -> fail("Should not run"),
                err -> 5);
        assertThat(merged).isEqualTo(5);
    }

    @Test
    void merge_success_nullValueFunctionGivesNPE() {
        BooleanResult<String> result = BooleanResult.success(true);
        assertThatThrownBy(() -> result.merge(null, () -> 6, err -> 5))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void merge_success_nullEmptySupplierGivesNPE() {
        BooleanResult<String> result = BooleanResult.success(true);
        assertThatThrownBy(() -> result.merge(() -> 7, null, err -> 5))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void merge_success_nullErrorFunctionGivesNPE() {
        BooleanResult<String> result = BooleanResult.success(true);
        assertThatThrownBy(() -> result.merge(() -> 7, () -> 6, null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void merge_success_canReturnNull() {
        BooleanResult<Integer> result = BooleanResult.success(true);
        Integer merged = result.merge(() -> null, () -> 6, err -> 5);
        assertThat(merged).isNull();
    }

    @Test
    void merge_empty_canReturnNull() {
        BooleanResult<Integer> result = BooleanResult.success(false);
        Integer merged = result.merge(() -> 7, () -> null, err -> 5);
        assertThat(merged).isNull();
    }

    @Test
    void merge_error_canReturnNull() {
        BooleanResult<Integer> result = BooleanResult.error(7);
        Integer merged = result.merge(() -> 7, () -> 6, err -> null);
        assertThat(merged).isNull();
    }
}