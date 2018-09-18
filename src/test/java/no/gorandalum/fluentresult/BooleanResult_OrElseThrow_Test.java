package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BooleanResult_OrElseThrow_Test {

    @Test
    void orElseThrow_success_shouldRespondWithSuccessValue() {
        BooleanResult<String> result = BooleanResult.success(true);
        Boolean orElse = result.orElseThrow(IllegalArgumentException::new);
        assertThat(orElse).isTrue();
    }
}