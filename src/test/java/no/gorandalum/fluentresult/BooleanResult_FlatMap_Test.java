package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class BooleanResult_FlatMap_Test {

    @Test
    void flatMap_success_shouldFlatMapValue() {
        BooleanResult.success(true)
                .flatMap(val -> BooleanResult.success(!val))
                .consumeEither(
                        () -> fail("Should not be empty"),
                        () -> {},
                        err -> fail("Should not be error")
                );
    }
}