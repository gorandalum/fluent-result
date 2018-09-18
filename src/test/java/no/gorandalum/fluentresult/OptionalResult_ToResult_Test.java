package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.fail;

class OptionalResult_ToResult_Test {

    @Test
    void toResult_success_shouldCreateResultContaining() {
        Result<String, String> result = OptionalResult.<String, String>success("Success")
                .toResult(() -> "Error");
        result.consumeEither(
                val -> assertThat(val).isEqualTo("Success"),
                err -> fail("Should not be error")
        );
    }

    @Test
    void toResult_empty_shouldRunErrorSupplier() {
        Result<String, String> result = OptionalResult.<String, String>empty()
                .toResult(() -> "Error");
        result.consumeEither(
                val -> fail("Should not be success"),
                err -> assertThat(err).isEqualTo("Error")
        );
    }

    @Test
    void toResult_error_shouldKeepError() {
        Result<String, String> result = OptionalResult.<String, String>error("OriginalError")
                .toResult(() -> "Error");
        result.consumeEither(
                val -> fail("Should not be success"),
                err -> assertThat(err).isEqualTo("OriginalError")
        );
    }
}