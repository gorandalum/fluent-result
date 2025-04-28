package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class Result_Recover_Test {

    @Test
    void recover_success_shouldRemainExistingSuccess() {
        Result.success("Success")
                .recover(e -> "Recovered")
                .consumeEither(
                        val -> assertThat(val).isEqualTo("Success"),
                        err -> fail("Should not be error"));
    }

    @Test
    void recover_error_shouldApplyRecoverFunction() {
        Result.error("Error")
                .recover(e -> "Recovered")
                .consumeEither(
                        val -> assertThat(val).isEqualTo("Recovered"),
                        err -> fail("Should not be error"));
    }
}
