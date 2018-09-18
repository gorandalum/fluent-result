package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.fail;

class BooleanResult_FlatMapToVoid_Test {

    @Test
    void flatMapToVoid_success_shouldFlatMapValue() {
        BooleanResult.success(false)
                .flatMapToVoid(val -> VoidResult.success())
                .consumeEither(
                        () -> {},
                        err -> fail("Should not be error")
                );
    }
}