package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class VoidResult_FlatReplaceToBooleanResult_Test {

    @Test
    void flatReplaceToOptionalResult_success_flatReplaceWithSuccess() {
        BooleanResult<String> result =
                VoidResult.<String>success()
                        .flatReplaceToBooleanResult(() -> BooleanResult.success(false));
        result.consumeEither(
                () -> fail("Should not be true"),
                () -> {},
                err -> fail("Expected no error"));
    }

    @Test
    void flatReplaceToBooleanResult_success_flatReplaceWithError() {
        BooleanResult<String> result =
                VoidResult.<String>success()
                        .flatReplaceToBooleanResult(() -> BooleanResult.error("Error"));
        result.consumeEither(
                () -> fail("Should not have value"),
                () -> fail("Should not be empty"),
                err -> assertThat(err).isEqualTo("Error"));
    }
}