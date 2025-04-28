package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class VoidResult_FlatRecover_Test {

    @Test
    void flatFlatRecover_success_shouldReturnValue() {
        VoidResult<String> result = VoidResult.success();
        VoidResult<String> shouldNotBeError = result
                .flatRecover(error -> VoidResult.error("Error"))
                .consumeEither(
                        () -> {},
                        err -> fail("Should not be error")
                );
        assertThat(shouldNotBeError).isNotNull();
    }

    @Test
    void flatFlatRecover_error_shouldApplyRecoverFunction() {
        VoidResult.error("Error")
                .flatRecover(error -> VoidResult.success())
                .consumeEither(
                        () -> {},
                        err -> fail("Should not be error")
                );
    }
}
