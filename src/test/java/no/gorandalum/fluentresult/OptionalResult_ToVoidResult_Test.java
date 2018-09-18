package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.fail;

class OptionalResult_ToVoidResult_Test {

    @Test
    void toVoidResult_success_shouldCreateEmptyVoidResult() {
        OptionalResult<String, String> result = OptionalResult.success("Success");
        VoidResult<String> voidResult = result.toVoidResult();
        voidResult.consumeEither(
                () -> {},
                err -> fail("Should not be error")
        );
    }

    @Test
    void toVoidResult_empty_shouldCreateEmptyVoidResult() {
        OptionalResult<String, String> result = OptionalResult.empty();
        VoidResult<String> voidResult = result.toVoidResult();
        voidResult.consumeEither(
                () -> {},
                err -> fail("Should not be error")
        );
    }

    @Test
    void toVoidResult_error_shouldKeepError() {
        OptionalResult<String, String> result = OptionalResult.error("Error");
        VoidResult<String> voidResult = result.toVoidResult();
        voidResult.consumeEither(
                () -> fail("Should not be success"),
                err -> assertThat(err).isEqualTo("Error")
        );
    }
}