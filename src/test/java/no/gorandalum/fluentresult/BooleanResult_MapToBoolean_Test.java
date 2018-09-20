package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class BooleanResult_MapToBoolean_Test {

    @Test
    void mapToBoolean_success_successfullyMapValue() {
        BooleanResult<String> result =
                BooleanResult.<String>success(true)
                        .mapToBoolean(val -> !val);
        result.consumeEither(
                () -> fail("Should not be empty"),
                () -> {},
                err -> fail("Expected no error"));
    }
}