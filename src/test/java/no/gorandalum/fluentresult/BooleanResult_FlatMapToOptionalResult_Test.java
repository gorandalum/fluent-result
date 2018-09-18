package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class BooleanResult_FlatMapToOptionalResult_Test {

    @Test
    void flatMapToOptionalResult_success_shouldFlatMapValue() {
        BooleanResult.success(true)
                .flatMapToOptional(val -> OptionalResult.success(val ? "Success" : "Failure"))
                .consumeEither(
                        val -> assertThat(val).isEqualTo("Success"),
                        () -> fail("Should not be empty"),
                        err -> fail("Should not be error")
                );
    }
}