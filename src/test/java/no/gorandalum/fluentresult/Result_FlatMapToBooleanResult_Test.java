package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class Result_FlatMapToBooleanResult_Test {

    @Test
    void flatMapToBooleanResult_success_shouldFlatMapValue() {
        Result<String, String> result = Result.success("Success");
        result.flatMapToBooleanResult(val -> BooleanResult.success(true))
                .consumeEither(
                        val -> assertThat(val).isTrue(),
                        err -> fail("Should not be error")
                );
    }
}