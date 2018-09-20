package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class OptionalResult_FlatMapToBooleanResult_Test {

    @Test
    void flatMapToBooleanResult_success_shouldFlatMapValue() {
        OptionalResult<String, String> result = OptionalResult.success("Success");
        result.flatMapToBooleanResult(
                maybeVal -> BooleanResult.success(maybeVal.map(String::isEmpty).orElse(true)))
                .consumeEither(
                        val -> assertThat(val).isFalse(),
                        err -> fail("Should not be error")
                );
    }
}