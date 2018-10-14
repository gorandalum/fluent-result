package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class BooleanResult_Verify_Predicate_Test {

    @Test
    void verify_predicate_success_shouldKeepSuccessResultWhenVerifiedTrue() {
        BooleanResult<String> result = BooleanResult.<String>success(false)
                .verify(
                        val -> !val,
                        () -> "ValidationError");
        result.consumeEither(
                () -> fail("Should not be true"),
                () -> {},
                err -> fail("Should not be error"));
    }

    @Test
    void verify_predicate_success_shouldChangeToProvidedErrorWhenVerifiedFalse() {
        BooleanResult<String> result = BooleanResult.<String>success(true)
                .verify(
                        val -> !val,
                        () -> "ValidationError");
        result.consumeEither(
                val -> fail("Expected no value"),
                err -> assertThat(err).isEqualTo("ValidationError"));
    }

    @Test
    void verify_predicate_error_shouldKeepOriginalError() {
        BooleanResult<String> result = BooleanResult.error("Error")
                .verify(
                        val -> fail("Should not run verificator"),
                        () -> "ValidationError");
        result.consumeEither(
                val -> fail("Expected no value"),
                err -> assertThat(err).isEqualTo("Error"));
    }
}