package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class OptionalResult_Recover_Test {

    @Test
    void recover_success_shouldRemainExistingSuccess() {
        OptionalResult.success("Success")
                .recover(e -> Optional.of("Recovered"))
                .consumeEither(
                        val -> assertThat(val).isEqualTo("Success"),
                        () -> fail("Should not be empty"),
                        err -> fail("Should not be error"));
    }

    @Test
    void recover_empty_shouldRemainEmpty() {
        OptionalResult.empty()
                .recover(e -> Optional.of("Recovered"))
                .consumeEither(
                        val -> fail("Should not have success value"),
                        () -> {},
                        err -> fail("Should not be error"));
    }

    @Test
    void recover_error_shouldApplyRecoverFunctionAndBeSuccessValue() {
        OptionalResult.error("Error")
                .recover(e -> Optional.of("Recovered"))
                .consumeEither(
                        val -> assertThat(val).isEqualTo("Recovered"),
                        () -> fail("Should not be empty"),
                        err -> fail("Should not be error"));
    }

    @Test
    void recover_error_shouldApplyRecoverFunctionAndBeEmpty() {
        OptionalResult.error("Error")
                .recover(e -> Optional.empty())
                .consumeEither(
                        val -> fail("Should not have success value"),
                        () -> {},
                        err -> fail("Should not be error"));
    }
}
