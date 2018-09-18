package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class BooleanResult_MapToOptional_Test {

    @Test
    void mapToOptional_success_successfullyMapValue() {
        OptionalResult<Integer, String> result =
                BooleanResult.<String>success(true)
                        .mapToOptional(val -> Optional.of(val ? 5 : 3));
        result.consumeEither(
                val -> assertThat(val).isEqualTo(5),
                () -> fail("Should not be empty"),
                err -> fail("Should not be error"));
    }
}