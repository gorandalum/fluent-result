package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class Result_Merge_Test {

    @Test
    void merge_success_shouldGiveSuccessValueFunctionResult() {
        Result<String, Integer> result = Result.success("Success");
        Integer merged = result.merge(String::length, err -> err);
        assertThat(merged).isEqualTo(7);
    }

    @Test
    void merge_error_shouldGiveErrorValueFunctionResult() {
        Result<String, Integer> result = Result.error(5);
        Integer merged = result.merge(String::length, err -> err);
        assertThat(merged).isEqualTo(5);
    }

    @Test
    void merge_success_nullValueFunctionGivesNPE() {
        Result<String, String> result = Result.success("Success");
        assertThatThrownBy(() -> result.merge(null, err -> err))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void merge_success_nullErrorFunctionGivesNPE() {
        Result<String, String> result = Result.success("Success");
        assertThatThrownBy(() -> result.merge(val -> val, null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void merge_success_canReturnNull() {
        Result<String, Integer> result = Result.success("Success");
        Integer merged = result.merge(val -> null, err -> err);
        assertThat(merged).isNull();
    }

    @Test
    void merge_error_canReturnNull() {
        Result<String, Integer> result = Result.error(7);
        Integer merged = result.merge(String::length, err -> null);
        assertThat(merged).isNull();
    }
}