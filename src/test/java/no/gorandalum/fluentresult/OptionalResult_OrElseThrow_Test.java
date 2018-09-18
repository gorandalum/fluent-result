package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OptionalResult_OrElseThrow_Test {

    @Test
    void orElseThrow_success_shouldRespondWithSuccessValue() {
        OptionalResult<String, String> result = OptionalResult.success("Success");
        Optional<String> orElse = result.orElseThrow(IllegalArgumentException::new);
        assertThat(orElse).isPresent();
        orElse.ifPresent(val -> assertThat(val).isEqualTo("Success"));
    }
}