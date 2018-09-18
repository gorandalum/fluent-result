package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BooleanResult_OrElseTrue_Test {

    @Test
    void orElseTrue_error_shouldRespondWithSuccessValue() {
        BooleanResult<String> result = BooleanResult.error("Error");
        boolean orElse = result.orElseTrue();
        assertThat(orElse).isTrue();
    }
}