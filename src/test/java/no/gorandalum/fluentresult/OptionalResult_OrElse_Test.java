package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class OptionalResult_OrElse_Test {

    @Test
    void orElse_success_shouldRespondWithSuccessValue() {
        OptionalResult<String, Object> result = OptionalResult.success("Success");
        Optional<String> orElse = result.orElse(Optional.of("Other"));
        assertThat(orElse).isPresent();
        orElse.ifPresent(val -> assertThat(val).isEqualTo("Success"));
    }
}