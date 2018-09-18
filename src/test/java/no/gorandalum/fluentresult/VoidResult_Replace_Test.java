package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class VoidResult_Replace_Test {

    @Test
    void replace_success_successfullyMapValue() {
        Result<String, String> result =
                VoidResult.<String>success()
                        .replace(() -> "Success");
        result.consumeEither(
                val -> assertThat(val).isEqualTo("Success"),
                err -> fail("Expected no error"));
    }
}