package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.fail;

class BooleanResult_FlatMapToVoidResult_Test {

    @Test
    void flatMapToVoidResult_success_shouldFlatMapValue() {
        BooleanResult.success(false)
                .flatMapToVoidResult(val -> VoidResult.success())
                .consumeEither(
                        () -> {},
                        err -> fail("Should not be error")
                );
    }
}