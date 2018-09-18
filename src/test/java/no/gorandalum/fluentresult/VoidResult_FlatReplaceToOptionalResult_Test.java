package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class VoidResult_FlatReplaceToOptionalResult_Test {

    @Test
    void flatReplaceToOptionalResult_success_flatReplaceWithSuccess() {
        OptionalResult<String, String> result =
                VoidResult.<String>success()
                        .flatReplaceToOptionalResult(() -> OptionalResult.success("Success"));
        result.consumeEither(
                val -> assertThat(val).isEqualTo("Success"),
                () -> fail("Should not be empty"),
                err -> fail("Expected no error"));
    }

    @Test
    void flatReplaceToOptionalResult_error_shouldKeepOriginalError() {
        OptionalResult<String, String> result =
                VoidResult.error("Error")
                        .flatReplaceToOptionalResult(() -> OptionalResult.success("Success"));
        result.consumeEither(
                val -> fail("Should not have value"),
                () -> fail("Should not be empty"),
                err -> assertThat(err).isEqualTo("Error"));
    }
}