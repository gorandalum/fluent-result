package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class BooleanResult_Recover_Test {

    @Test
    void recover_success_shouldReturnValue() {
        BooleanResult<String> result = BooleanResult.success(true);
        BooleanResult<String> shouldNotBeError = result.recover(error -> BooleanResult.success(true))
                .consumeEither(
                        value -> assertThat(value).isEqualTo(true),
                        err -> fail("Should not be error")
                );
        assertThat(shouldNotBeError).isNotNull();
    }

    @Test
    void recover() {
        BooleanResult<String> result = BooleanResult.error("Error");
        result.recover(error -> BooleanResult.success(true))
                .consumeEither(
                        value -> assertThat(value).isEqualTo(true),
                        err -> fail("Should not be error")
                );
    }
}
