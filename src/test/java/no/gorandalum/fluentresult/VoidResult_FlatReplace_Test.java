package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class VoidResult_FlatReplace_Test {

    @Test
    void flatReplace_success_flatReplaceWithSuccessResult() {
        Result<String, String> result =
                VoidResult.<String>success()
                        .flatReplace(() -> Result.success("Success"));
        result.consumeEither(
                val -> assertThat(val).isEqualTo("Success"),
                err -> fail("Expected no error"));
    }

    @Test
    void flatReplace_error_shouldKeepOriginalError() {
        Result<String, String> result =
                VoidResult.error("Error")
                        .flatReplace(() -> Result.success("Success"));
        result.consumeEither(
                val -> fail("Should not have value"),
                err -> assertThat(err).isEqualTo("Error"));
    }
}