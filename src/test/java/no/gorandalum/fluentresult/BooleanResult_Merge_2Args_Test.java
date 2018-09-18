package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BooleanResult_Merge_2Args_Test {

    @Test
    void merge_success_shouldGiveSuccessValueFunctionResult() {
        BooleanResult<Integer> result = BooleanResult.success(true);
        Integer merged = result.merge(
                val -> val ? 7 : 3,
                err -> err);
        assertThat(merged).isEqualTo(7);
    }
}