package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class BooleanResult_Verify_VoidResult_Test {

    @Test
    void verify_voidResult_success_shouldKeepSuccessResultWhenVoidResultSuccess() {
        BooleanResult<String> result = BooleanResult.<String>success(false)
                .verify(val -> VoidResult.success());
        result.consumeEither(
                () -> fail("Should not be true"),
                () -> {},
                err -> fail("Should not be error"));
    }

    @Test
    void verify_voidResult_success_shouldChangeToErrorWhenVoidResultError() {
        BooleanResult<String> result = BooleanResult.<String>success(true)
                .verify(val -> VoidResult.error("ValidationError"));
        result.consumeEither(
                val -> fail("Expected no value"),
                err -> assertThat(err).isEqualTo("ValidationError"));
    }

    @Test
    void verify_voidResult_error_shouldKeepOriginalError() {
        BooleanResult<String> result = BooleanResult.error("Error")
                .verify(val -> fail("Should not run"));
        result.consumeEither(
                val -> fail("Expected no value"),
                err -> assertThat(err).isEqualTo("Error"));
    }
}