package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class OptionalResult_FlatMapToVoidResult_Test {

    @Test
    void flatMapToVoidResult_success_shouldFlatMapValue() {
        OptionalResult<String, String> result = OptionalResult.success("Success");
        result.flatMapToVoidResult(
                maybeVal -> VoidResult.success())
                .consumeEither(
                        () -> {},
                        err -> fail("Should not be error")
                );
    }
}