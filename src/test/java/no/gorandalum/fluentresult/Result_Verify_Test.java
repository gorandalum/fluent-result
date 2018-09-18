package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.fail;

class Result_Verify_Test {

    @Test
    void verify_success_shouldKeepSuccessResultWhenVerifiedTrue() {
        Result<String, String> result = Result.<String, String>success("Success")
                .verify(val -> val.length() == 7, () -> "ValidationError");
        result.consumeEither(
                val -> assertThat(val).isEqualTo("Success"),
                err -> fail("Expected no error"));
    }

    @Test
    void verify_success_shouldChangeToProvidedErrorWhenVerifiedFalse() {
        Result<String, String> result = Result.<String, String>success("Success")
                .verify(val -> val.length() == 5, () -> "ValidationError");
        result.consumeEither(
                val -> fail("Expected no value"),
                err -> assertThat(err).isEqualTo("ValidationError"));
    }

    @Test
    void verify_error_shouldKeepOriginalError() {
        Result<String, String> result = Result.<String, String>error("Error")
                .verify(val -> val.length() == 5, () -> "ValidationError");
        result.consumeEither(
                val -> fail("Expected no value"),
                err -> assertThat(err).isEqualTo("Error"));
    }

    @Test
    void verify_error_shouldNotRunVerificatorWhenError() {
        Result<String, String> result = Result.<String, String>error("Error")
                .verify(
                        val -> {
                            throw new RuntimeException();
                        },
                        () -> "ValidationError");
        result.consumeEither(
                val -> fail("Expected no value"),
                err -> assertThat(err).isEqualTo("Error"));
    }

    @Test
    void verify_success_nullVerificatorGivesNPE() {
        Result<String, String> result = Result.success("Success");
        assertThatThrownBy(() -> result.verify(null, () -> "ValidationError"))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void verify_success_nullErrorSupplierGivesNPE() {
        Result<String, String> result = Result.success("Success");
        assertThatThrownBy(() -> result.verify(val -> true, null))
                .isInstanceOf(NullPointerException.class);
    }
}