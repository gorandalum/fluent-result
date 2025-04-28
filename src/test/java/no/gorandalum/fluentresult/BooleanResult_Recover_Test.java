package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class BooleanResult_Recover_Test {

    @Test
    void recover_success_shouldRemainFalse() {
        BooleanResult.success(false)
                .recover(e -> true)
                .consumeEither(
                        val -> assertThat(val).isEqualTo(false),
                        err -> fail("Should not be error"));
    }

    @Test
    void recover_empty_shouldRemainTrue() {
        BooleanResult.success(true)
                .recover(e -> false)
                .consumeEither(
                        val -> assertThat(val).isEqualTo(true),
                        err -> fail("Should not be error"));
    }

    @Test
    void recover_error_shouldApplyRecoverFunction() {
        BooleanResult.error("Error")
                .recover(e -> true)
                .consumeEither(
                        val -> assertThat(val).isEqualTo(true),
                        err -> fail("Should not be error"));
    }
}
