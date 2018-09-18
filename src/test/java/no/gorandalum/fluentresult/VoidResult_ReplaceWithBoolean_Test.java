package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class VoidResult_ReplaceWithBoolean_Test {

    @Test
    void replaceWithBoolean_success_successfullyMapValue() {
        BooleanResult<String> result =
                VoidResult.<String>success()
                        .replaceWithBoolean(() -> true);
        result.consumeEither(
                () -> {},
                () -> fail("Should not be empty"),
                err -> fail("Expected no error"));
    }
}