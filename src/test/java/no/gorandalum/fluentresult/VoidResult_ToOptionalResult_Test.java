package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.fail;

class VoidResult_ToOptionalResult_Test {

    @Test
    void toOptionalResult_success_shouldCreateEmptyOptionalResult() {
        VoidResult<String> voidResult = VoidResult.success();
        OptionalResult<String, String> result = voidResult.toOptionalResult();
        result.consumeEither(
                val -> fail("Should not have value"),
                () -> {},
                err -> fail("Should not have error"));
    }

    @Test
    void toOptionalResult_error_shouldKeepError() {
        VoidResult<String> voidResult = VoidResult.error("Error");
        OptionalResult<String, String> result = voidResult.toOptionalResult();
        result.consumeEither(
                val -> fail("Should not have value"),
                () -> fail("Should not be empty"),
                err -> assertThat(err).isEqualTo("Error"));
    }
}