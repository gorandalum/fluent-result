package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class VoidResult_ReplaceWithOptional_Test {

    @Test
    void replaceWithOptional_success_successfullyMapValue() {
        OptionalResult<String, String> result =
                VoidResult.<String>success()
                        .replaceWithOptional(() -> Optional.of("Success"));
        result.consumeEither(
                val -> assertThat(val).isEqualTo("Success"),
                () -> fail("Should not be empty"),
                err -> fail("Expected no error"));
    }
}