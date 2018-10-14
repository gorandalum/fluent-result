package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class OptionalResult_VerifyValue_VoidResult_Test {

    @Test
    void verifyValue_voidResult_success_shouldKeepSuccessValueWhenVoidResultSuccess() {
        OptionalResult<String, String> result =
                OptionalResult.<String, String>success("Success")
                        .verifyValue(val -> VoidResult.success());
        result.consumeEither(
                val -> assertThat(val).isEqualTo("Success"),
                () -> fail("Should not be empty"),
                err -> fail("Expected no error"));
    }

    @Test
    void verifyValue_voidResult_success_shouldChangeToErrorWhenVoidResultError() {
        OptionalResult<String, String> result =
                OptionalResult.<String, String>success("Success")
                        .verifyValue(val -> VoidResult.error("Error"));
        result.consumeEither(
                val -> fail("Should not have value"),
                () -> fail("Should not be empty"),
                err -> assertThat(err).isEqualTo("Error"));
    }

    @Test
    void verifyValue_voidResult_empty_shouldRemainEmtpy() {
        OptionalResult<String, String> result =
                OptionalResult.<String, String>empty()
                        .verifyValue(val -> fail("Should not run"));
        result.consumeEither(
                val -> fail("Should not have value"),
                () -> {},
                err -> fail("Should not have error"));
    }

    @Test
    void verifyValue_voidResult_error_shouldKeepOriginalError() {
        OptionalResult<String, String> result =
                OptionalResult.<String, String>error("OriginalError")
                        .verifyValue(val -> fail("Should not run"));
        result.consumeEither(
                val -> fail("Should not have value"),
                () -> fail("Should not be empty"),
                err -> assertThat(err).isEqualTo("OriginalError"));
    }

    @Test
    void verifyValue_voidResult_success_nullFunctionGivesNPE() {
        OptionalResult<String, String> result = OptionalResult.success("Success");
        assertThatThrownBy(() -> result.verifyValue(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void verify_success_functionReturnsNullGivesNPE() {
        OptionalResult<String, String> result = OptionalResult.success("Success");
        assertThatThrownBy(() -> result.verifyValue(val -> null))
                .isInstanceOf(NullPointerException.class);
    }
}