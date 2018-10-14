package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class OptionalResult_Verify_VoidResult_Test {

    @Test
    void verify_voidResult_success_shouldKeepSuccessResultWhenVoidResultSuccess() {
        OptionalResult<String, String> result = OptionalResult.<String, String>success("Success")
                .verify(val -> VoidResult.success());
        result.consumeEither(
                val -> assertThat(val).isEqualTo("Success"),
                () -> fail("Should not be empty"),
                err -> fail("Should not be error"));
    }

    @Test
    void verify_voidResult_success_shouldChangeToErrorWhenVoidResultError() {
        OptionalResult<String, String> result = OptionalResult.<String, String>success("Success")
                .verify(val -> VoidResult.error("ValidationError"));
        result.consumeEither(
                val -> fail("Expected no value"),
                err -> assertThat(err).isEqualTo("ValidationError"));
    }

    @Test
    void verify_voidResult_error_shouldKeepOriginalError() {
        OptionalResult<String, String> result = OptionalResult.<String, String>error("Error")
                .verify(val -> fail("Should not be run"));
        result.consumeEither(
                val -> fail("Expected no value"),
                err -> assertThat(err).isEqualTo("Error"));
    }

    @Test
    void verify_voidResult_success_nullFunctionGivesNPE() {
        OptionalResult<String, String> result = OptionalResult.success("Success");
        assertThatThrownBy(() -> result.verify(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void verify_voidResult_success_functionReturnNullGivesNPE() {
        OptionalResult<String, String> result = OptionalResult.success("Success");
        assertThatThrownBy(() -> result.verify(val -> null))
                .isInstanceOf(NullPointerException.class);
    }
}