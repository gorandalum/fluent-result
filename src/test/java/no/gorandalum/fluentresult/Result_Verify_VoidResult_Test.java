package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class Result_Verify_VoidResult_Test {

    @Test
    void verify_voidResult_success_shouldKeepSuccessResultWhenVoidResultSuccess() {
        Result<String, String> result = Result.<String, String>success("Success")
                .verify(val -> VoidResult.success());
        result.consumeEither(
                val -> assertThat(val).isEqualTo("Success"),
                err -> fail("Expected no error"));
    }

    @Test
    void verify_voidResult_success_shouldChangeToErrorWhenVoidResultError() {
        Result<String, String> result = Result.<String, String>success("Success")
                .verify(val -> VoidResult.error("ValidationError"));
        result.consumeEither(
                val -> fail("Expected no value"),
                err -> assertThat(err).isEqualTo("ValidationError"));
    }

    @Test
    void verify_voidResult_error_shouldKeepOriginalError() {
        Result<String, String> result = Result.<String, String>error("Error")
                .verify(val -> fail("Should not run"));
        result.consumeEither(
                val -> fail("Expected no value"),
                err -> assertThat(err).isEqualTo("Error"));
    }

    @Test
    void verify_voidResult_success_nullFunctionGivesNPE() {
        Result<String, String> result = Result.success("Success");
        assertThatThrownBy(() -> result.verify(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void verify_voidResult_success_functionReturnsNullGivesNPE() {
        Result<String, String> result = Result.success("Success");
        assertThatThrownBy(() -> result.verify(val -> null))
                .isInstanceOf(NullPointerException.class);
    }
}