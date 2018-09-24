package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.fail;

class OptionalResult_Flatten_Test {

    @Test
    void flatten_success_shouldCreateResultContaining() {
        Result<String, String> result = OptionalResult.<String, String>success("Success")
                .flatten(() -> "Error");
        result.consumeEither(
                val -> assertThat(val).isEqualTo("Success"),
                err -> fail("Should not be error")
        );
    }

    @Test
    void flatten_empty_shouldRunErrorSupplier() {
        Result<String, String> result = OptionalResult.<String, String>empty()
                .flatten(() -> "Error");
        result.consumeEither(
                val -> fail("Should not be success"),
                err -> assertThat(err).isEqualTo("Error")
        );
    }

    @Test
    void flatten_error_shouldKeepError() {
        Result<String, String> result = OptionalResult.<String, String>error("OriginalError")
                .flatten(() -> "Error");
        result.consumeEither(
                val -> fail("Should not be success"),
                err -> assertThat(err).isEqualTo("OriginalError")
        );
    }
}