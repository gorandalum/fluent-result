package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class OptionalResult_MapToOptional_Test {

    @Test
    void mapToOptional_success_successfullyMapValue() {
        OptionalResult<Integer, String> result =
                OptionalResult.<String, String>success("Success")
                        .mapToOptional(maybeVal -> Optional.of(7));
        result.consumeEither(
                val -> assertThat(val).isEqualTo(7),
                () -> fail("Should not be empty"),
                err -> fail("Expected no error"));
    }
}