package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class OptionalResult_Success_Test {

    @Test
    void verifyValue_success_shouldAllowSubtypeOfValueInOptional() {
        Integer integer = 5;
        Optional<Integer> integer1 = Optional.of(integer);
        OptionalResult<Number, String> result =
                OptionalResult.success(integer1);
        result.consumeEither(
                val -> assertThat(val).isEqualTo(5),
                () -> fail("Should not be empty"),
                err -> fail("Expected no error"));
    }
}