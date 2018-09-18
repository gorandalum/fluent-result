package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BooleanResult_OrElseGet_Test {

    @Test
    void orElseGet_success_shouldRespondWithSuccessValue() {
        BooleanResult<String> result = BooleanResult.success(true);
        Boolean orElse = result.orElseGet(err -> false);
        assertThat(orElse).isTrue();
    }
}