package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.fail;

class BooleanResult_ToVoidResult_Test {

    @Test
    void toVoidResult_success_shouldCreateEmptyVoidResultWhenTrue() {
        BooleanResult<String> result = BooleanResult.success(true);
        VoidResult<String> voidResult = result.toVoidResult();
        voidResult.consumeEither(
                () -> {},
                err -> fail("Should not be error")
        );
    }

    @Test
    void toVoidResult_success_shouldCreateEmptyVoidResultWhenFalse() {
        BooleanResult<String> result = BooleanResult.success(false);
        VoidResult<String> voidResult = result.toVoidResult();
        voidResult.consumeEither(
                () -> {},
                err -> fail("Should not be error")
        );
    }

    @Test
    void toVoidResult_error_shouldKeepError() {
        BooleanResult<String> result = BooleanResult.error("Error");
        VoidResult<String> voidResult = result.toVoidResult();
        voidResult.consumeEither(
                () -> fail("Should not be success"),
                err -> assertThat(err).isEqualTo("Error")
        );
    }
}