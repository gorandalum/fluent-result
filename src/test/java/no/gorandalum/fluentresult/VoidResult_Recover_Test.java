package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class VoidResult_Recover_Test {

    @Test
    void flatRecover_success_shouldRemainSuccess() {
        VoidResult.success()
                .recover()
                .consumeEither(
                        () -> {},
                        err -> fail("Should not be error")
                );
    }

    @Test
    void flatRecover_error_shouldBeSuccess() {
        VoidResult.error("Error")
                .recover()
                .consumeEither(
                        () -> {},
                        err -> fail("Should not be error")
                );
    }
}
