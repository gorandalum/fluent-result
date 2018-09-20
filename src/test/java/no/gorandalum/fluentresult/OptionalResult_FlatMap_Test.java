package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class OptionalResult_FlatMap_Test {

    @Test
    void flatMap_success_shouldFlatMapValue() {
        OptionalResult<String, String> result = OptionalResult.success("Success");
        result.flatMap(
                maybeVal -> Result.success(maybeVal.map(String::length).orElse(3)))
                .consumeEither(
                        val -> assertThat(val).isEqualTo(7),
                        err -> fail("Should not be error")
                );
    }
}