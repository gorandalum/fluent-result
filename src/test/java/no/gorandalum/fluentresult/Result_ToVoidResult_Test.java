package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.fail;

class Result_ToVoidResult_Test {

    @Test
    void toVoidResult_success_shouldCreateEmptyVoidResult() {
        Result<String, String> result = Result.success("Success");
        VoidResult<String> voidResult = result.toVoidResult();
        voidResult.consumeEither(
                () -> {},
                err -> fail("Should not be error")
        );
    }

    @Test
    void toVoidResult_error_shouldKeepError() {
        Result<String, String> result = Result.error("Error");
        VoidResult<String> voidResult = result.toVoidResult();
        voidResult.consumeEither(
                () -> fail("Should not be success"),
                err -> assertThat(err).isEqualTo("Error")
        );
    }
}