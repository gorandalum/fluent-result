package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class Result_MapToBoolean_Test {

    @Test
    void mapToBoolean_success_shouldMapToBoolean() {
        Result<String, String> result = Result.success("Success");
        result.mapToBoolean(val -> true).consumeEither(
                val -> assertThat(val).isEqualTo(true),
                err -> fail("Should not be error")
        );
    }
}