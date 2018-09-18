package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BooleanResult_OrElseFalse_Test {

    @Test
    void orElseFalse_error_shouldRespondWithSuccessValue() {
        BooleanResult<String> result = BooleanResult.error("Error");
        boolean orElse = result.orElseFalse();
        assertThat(orElse).isFalse();
    }
}