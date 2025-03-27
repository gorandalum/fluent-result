package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class BooleanResult_Recover_Test {

    @Test
    void flatFlatRecover_success_shouldReturnValue() {
        BooleanResult<String> result = BooleanResult.success(true);
        BooleanResult<String> shouldNotBeError = result.flatRecover(error -> BooleanResult.success(true))
                .consumeEither(
                        value -> assertThat(value).isEqualTo(true),
                        err -> fail("Should not be error")
                );
        assertThat(shouldNotBeError).isNotNull();
    }

    @Test
    void flatFlatRecover() {
        BooleanResult<String> result = BooleanResult.error("Error");
        result.flatRecover(error -> BooleanResult.success(true))
                .consumeEither(
                        value -> assertThat(value).isEqualTo(true),
                        err -> fail("Should not be error")
                );
    }
}
