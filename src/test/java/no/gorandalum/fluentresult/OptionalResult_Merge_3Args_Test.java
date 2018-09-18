package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.fail;

class OptionalResult_Merge_3Args_Test {

    @Test
    void merge_success_shouldGiveSuccessValueFunctionResult() {
        Integer merged = OptionalResult.success("Success").merge(
                String::length,
                () -> fail("Should not run"),
                err -> fail("Should not run"));
        assertThat(merged).isEqualTo(7);
    }

    @Test
    void merge_empty_shouldGiveEmptySupplierResult() {
        Integer merged = OptionalResult.empty().merge(
                val -> fail("Should not run"),
                () -> 6,
                err -> fail("Should not run"));
        assertThat(merged).isEqualTo(6);
    }

    @Test
    void merge_error_shouldGiveErrorValueFunctionResult() {
        Integer merged = OptionalResult.error("Error").merge(
                val -> fail("Should not run"),
                () -> fail("Should not run"),
                err -> 5);
        assertThat(merged).isEqualTo(5);
    }

    @Test
    void merge_success_nullValueFunctionGivesNPE() {
        OptionalResult<String, String> result = OptionalResult.success("Success");
        assertThatThrownBy(() -> result.merge(null, () -> 6, err -> 5))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void merge_success_nullEmptySupplierGivesNPE() {
        OptionalResult<String, String> result = OptionalResult.success("Success");
        assertThatThrownBy(() -> result.merge(val -> 7, null, err -> 5))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void merge_success_nullErrorFunctionGivesNPE() {
        OptionalResult<String, String> result = OptionalResult.success("Success");
        assertThatThrownBy(() -> result.merge(val -> 7, () -> 6, null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void merge_success_canReturnNull() {
        OptionalResult<String, Integer> result = OptionalResult.success("Success");
        Integer merged = result.merge(val -> null, () -> 6, err -> 5);
        assertThat(merged).isNull();
    }

    @Test
    void merge_empty_canReturnNull() {
        OptionalResult<String, Integer> result = OptionalResult.empty();
        Integer merged = result.merge(val -> 7, () -> null, err -> 5);
        assertThat(merged).isNull();
    }

    @Test
    void merge_error_canReturnNull() {
        OptionalResult<String, Integer> result = OptionalResult.error(7);
        Integer merged = result.merge(val -> 7, () -> 6, err -> null);
        assertThat(merged).isNull();
    }
}