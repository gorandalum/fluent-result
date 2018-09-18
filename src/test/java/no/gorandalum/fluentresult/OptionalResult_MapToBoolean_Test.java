package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class OptionalResult_MapToBoolean_Test {

    @Test
    void mapToBoolean_success_successfullyMapValue() {
        BooleanResult<String> result =
                OptionalResult.<String, String>success("Success")
                        .mapToBoolean(maybeVal -> true);
        result.consumeEither(
                val -> assertThat(val).isTrue(),
                err -> fail("Should not be error"));
    }
}