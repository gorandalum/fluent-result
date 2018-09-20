package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class VoidResult_FlatReplaceToVoidResult_Test {

    @Test
    void flatReplaceToVoidResult_success_flatReplaceWithSuccessVoidResult() {
        VoidResult<String> result =
                VoidResult.<String>success()
                        .flatReplaceToVoidResult(VoidResult::success);
        result.consumeEither(
                () -> {},
                err -> fail("Expected no error"));
    }

    @Test
    void flatReplaceToVoidResult_success_flatReplaceWithErrorVoidResult() {
        VoidResult<String> result =
                VoidResult.<String>success()
                        .flatReplaceToVoidResult(() -> VoidResult.error("Error"));
        result.consumeEither(
                () -> fail("Should not be success"),
                err -> assertThat(err).isEqualTo("Error"));
    }
}