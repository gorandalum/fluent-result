package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class OptionalResult_FlatMapToOptionalResult_Test {

    @Test
    void flatMapToOptionalResult_success_shouldFlatMapValue() {
        OptionalResult<String, String> result = OptionalResult.success("Success");
        result.flatMapToOptionalResult(
                maybeVal ->
                        OptionalResult.success(maybeVal.map(String::length).orElse(3)))
                .consumeEither(
                        val -> assertThat(val).isEqualTo(7),
                        () -> fail("Should not be empty"),
                        err -> fail("Should not be error")
                );
    }
}