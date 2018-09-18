package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class Result_FlatMapToBoolean_Test {

    @Test
    void flatMapToBoolean_success_shouldFlatMapValue() {
        Result<String, String> result = Result.success("Success");
        result.flatMapToBoolean(val -> BooleanResult.success(true))
                .consumeEither(
                        val -> assertThat(val).isTrue(),
                        err -> fail("Should not be error")
                );
    }
}