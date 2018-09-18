package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OptionalResult_Merge_2Args_Test {

    @Test
    void merge_success_shouldGiveSuccessValueFunctionResult() {
        OptionalResult<String, Integer> result = OptionalResult.success("Success");
        Integer merged = result.merge(
                maybeVal -> maybeVal.map(String::length).orElse(3),
                err -> err);
        assertThat(merged).isEqualTo(7);
    }
}