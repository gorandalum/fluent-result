package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class BooleanResult_Map_Test {

    @Test
    void map_success_successfullyMapValue() {
        BooleanResult<String> result =
                BooleanResult.<String>success(true)
                        .map(val -> !val);
        result.consumeEither(
                () -> fail("Should not be empty"),
                () -> {},
                err -> fail("Expected no error"));
    }
}