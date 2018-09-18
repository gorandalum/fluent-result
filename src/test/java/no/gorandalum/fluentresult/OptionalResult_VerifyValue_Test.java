package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class OptionalResult_VerifyValue_Test {

    @Test
    void verifyValue_success_valueVerifiedTrue() {
        OptionalResult<String, String> result =
                OptionalResult.<String, String>success("Success")
                        .verifyValue(val -> val.length() == 7, () -> "Error");
        result.consumeEither(
                val -> assertThat(val).isEqualTo("Success"),
                () -> fail("Should not be empty"),
                err -> fail("Expected no error"));
    }

    @Test
    void verifyValue_success_valueVerifiedFalse() {
        OptionalResult<String, String> result =
                OptionalResult.<String, String>success("Success")
                        .verifyValue(val -> val.length() == 5, () -> "Error");
        result.consumeEither(
                val -> fail("Should not have value"),
                () -> fail("Should not be empty"),
                err -> assertThat(err).isEqualTo("Error"));
    }

    @Test
    void verifyValue_empty_shouldRemainEmpty() {
        OptionalResult<String, String> result =
                OptionalResult.<String, String>empty()
                        .verifyValue(val -> val.length() == 5, () -> "Error");
        result.consumeEither(
                val -> fail("Should not have value"),
                () -> {},
                err -> fail("Should not have error"));
    }

    @Test
    void verifyValue_error_shouldKeepOriginalError() {
        OptionalResult<String, String> result =
                OptionalResult.<String, String>error("OriginalError")
                        .verifyValue(val -> val.length() == 5, () -> "Error");
        result.consumeEither(
                val -> fail("Should not have value"),
                () -> fail("Should not be empty"),
                err -> assertThat(err).isEqualTo("OriginalError"));
    }

    @Test
    void verifyValue_empty_shouldNotRunVerificatorWhenEmpty() {
        OptionalResult.empty().verifyValue(
                val -> {
                    throw new RuntimeException();
                },
                () -> "Error");
    }

    @Test
    void verifyValue_error_shouldNotRunVerificatorWhenError() {
        OptionalResult.error("OriginalError").verifyValue(
                val -> {
                    throw new RuntimeException();
                },
                () -> "Error");
    }

    @Test
    void verifyValue_success_nullVerificatorGivesNPE() {
        OptionalResult<String, String> result = OptionalResult.success("Success");
        assertThatThrownBy(() -> result.verifyValue(null, () -> "ValidationError"))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void verify_success_nullErrorSupplierGivesNPE() {
        OptionalResult<String, String> result = OptionalResult.success("Success");
        assertThatThrownBy(() -> result.verifyValue(val -> true, null))
                .isInstanceOf(NullPointerException.class);
    }
}