package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class Result_FlatMapToVoidResult_Test {

    @Test
    void flatMapToVoidResult_success_shouldFlatMapValue() {
        Result<String, String> result = Result.success("Success");
        result.flatMapToVoidResult(val -> VoidResult.success())
                .consumeEither(
                        () -> {},
                        err -> fail("Should not be error")
                );
    }
}