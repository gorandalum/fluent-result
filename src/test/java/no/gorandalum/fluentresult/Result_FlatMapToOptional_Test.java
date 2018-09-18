package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.fail;

class Result_FlatMapToOptional_Test {

    @Test
    void flatMapToOptional_success_shouldFlatMapValue() {
        Result<String, String> result = Result.success("Success");
        result.flatMapToOptional(val -> OptionalResult.success(val.length()))
                .consumeEither(
                        val -> assertThat(val).isEqualTo(7),
                        () -> fail("Should not be empty"),
                        err -> fail("Should not be error")
                );
    }

}