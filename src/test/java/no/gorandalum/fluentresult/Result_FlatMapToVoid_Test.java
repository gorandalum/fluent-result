package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class Result_FlatMapToVoid_Test {

    @Test
    void flatMapToVoid_success_shouldFlatMapValue() {
        Result<String, String> result = Result.success("Success");
        result.flatMapToVoid(val -> VoidResult.success())
                .consumeEither(
                        () -> {},
                        err -> fail("Should not be error")
                );
    }
}