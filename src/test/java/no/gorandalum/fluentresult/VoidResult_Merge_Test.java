package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class VoidResult_Merge_Test {

    @Test
    void merge_success_shouldGiveSuccessValueFunctionResult() {
        VoidResult<Integer> result = VoidResult.success();
        Integer merged = result.merge(() -> 7, err -> err);
        assertThat(merged).isEqualTo(7);
    }
}