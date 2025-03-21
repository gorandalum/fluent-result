package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class VoidResult_Recover_Test {

    @Test
    void recover_success_shouldReturnValue() {
        VoidResult<String> result = VoidResult.success();
        VoidResult<String> shouldNotBeError = result.recover(error -> VoidResult.success())
                .consumeEither(
                        () -> { },
                        err -> fail("Should not be error")
                );
        assertThat(shouldNotBeError).isNotNull();
    }

    @Test
    void recover_error_should_return_recovered_value() {
        VoidResult<String> result = VoidResult.error("Error");
        result.recover(error -> VoidResult.success())
                .consumeEither(
                        () -> { },
                        err -> fail("Should not be error")
                );
    }
}
