package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class Result_Fold_Test {

    @Test
    void fold_success_shouldGiveSuccessValueFunctionResult() {
        Result<String, Integer> result = Result.success("Success");
        Integer folded = result.fold(String::length, err -> err);
        assertThat(folded).isEqualTo(7);
    }

    @Test
    void fold_error_shouldGiveErrorValueFunctionResult() {
        Result<String, Integer> result = Result.error(5);
        Integer folded = result.fold(String::length, err -> err);
        assertThat(folded).isEqualTo(5);
    }

    @Test
    void fold_success_nullValueFunctionGivesNPE() {
        Result<String, String> result = Result.success("Success");
        assertThatThrownBy(() -> result.fold(null, err -> err))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void fold_success_nullErrorFunctionGivesNPE() {
        Result<String, String> result = Result.success("Success");
        assertThatThrownBy(() -> result.fold(val -> val, null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void fold_success_canReturnNull() {
        Result<String, Integer> result = Result.success("Success");
        Integer folded = result.fold(val -> null, err -> err);
        assertThat(folded).isNull();
    }

    @Test
    void fold_error_canReturnNull() {
        Result<String, Integer> result = Result.error(7);
        Integer folded = result.fold(String::length, err -> null);
        assertThat(folded).isNull();
    }
}