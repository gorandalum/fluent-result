package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BooleanResult_Fold_2Args_Test {

    @Test
    void fold_success_shouldGiveSuccessValueFunctionResult() {
        BooleanResult<Integer> result = BooleanResult.success(true);
        Integer folded = result.fold(
                val -> val ? 7 : 3,
                err -> err);
        assertThat(folded).isEqualTo(7);
    }
}