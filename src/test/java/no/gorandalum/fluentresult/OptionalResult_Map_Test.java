package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class OptionalResult_Map_Test {

    @Test
    void map_success_successfullyMapValue() {
        Result<Integer, String> result =
                OptionalResult.<String, String>success("Success")
                        .map(maybeVal -> maybeVal.map(String::length).orElse(4));
        result.consumeEither(
                val -> assertThat(val).isEqualTo(7),
                err -> fail("Should not be error"));
    }
}