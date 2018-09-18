package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class VoidResult_FlatReplace_Test {

    @Test
    void flatReplace_success_flatReplaceWithSuccessVoidResult() {
        VoidResult<String> result =
                VoidResult.<String>success()
                        .flatReplace(VoidResult::success);
        result.consumeEither(
                () -> {},
                err -> fail("Expected no error"));
    }

    @Test
    void flatReplace_success_flatReplaceWithErrorVoidResult() {
        VoidResult<String> result =
                VoidResult.<String>success()
                        .flatReplace(() -> VoidResult.error("Error"));
        result.consumeEither(
                () -> fail("Should not be success"),
                err -> assertThat(err).isEqualTo("Error"));
    }
}