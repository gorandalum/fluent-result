package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OptionalResult_Fold_2Args_Test {

    @Test
    void fold_success_shouldGiveSuccessValueFunctionResult() {
        OptionalResult<String, Integer> result = OptionalResult.success("Success");
        Integer folded = result.fold(
                maybeVal -> maybeVal.map(String::length).orElse(3),
                err -> err);
        assertThat(folded).isEqualTo(7);
    }
}