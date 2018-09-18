package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class BooleanResult_FlatMapToResult_Test {

    @Test
    void flatMapToResult_success_shouldFlatMapValue() {
        BooleanResult.success(true)
                .flatMapToResult(val -> Result.success(val ? "Success" : "Failure"))
                .consumeEither(
                        val -> assertThat(val).isEqualTo("Success"),
                        err -> fail("Should not be error")
                );
    }
}