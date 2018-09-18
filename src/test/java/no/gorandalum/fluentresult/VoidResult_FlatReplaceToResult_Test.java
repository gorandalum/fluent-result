package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class VoidResult_FlatReplaceToResult_Test {

    @Test
    void flatReplaceToResult_success_flatReplaceWithSuccessResult() {
        Result<String, String> result =
                VoidResult.<String>success()
                        .flatReplaceToResult(() -> Result.success("Success"));
        result.consumeEither(
                val -> assertThat(val).isEqualTo("Success"),
                err -> fail("Expected no error"));
    }

    @Test
    void flatReplaceToResult_error_shouldKeepOriginalError() {
        Result<String, String> result =
                VoidResult.error("Error")
                        .flatReplaceToResult(() -> Result.success("Success"));
        result.consumeEither(
                val -> fail("Should not have value"),
                err -> assertThat(err).isEqualTo("Error"));
    }
}