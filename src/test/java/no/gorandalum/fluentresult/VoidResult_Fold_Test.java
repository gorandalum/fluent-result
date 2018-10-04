package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class VoidResult_Fold_Test {

    @Test
    void fold_success_shouldGiveSuccessValueFunctionResult() {
        VoidResult<Integer> result = VoidResult.success();
        Integer folded = result.fold(() -> 7, err -> err);
        assertThat(folded).isEqualTo(7);
    }
}