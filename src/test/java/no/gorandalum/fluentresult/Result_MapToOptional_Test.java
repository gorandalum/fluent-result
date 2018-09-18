package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class Result_MapToOptional_Test {

    @Test
    void mapToOptional_success_shouldMapToValue() {
        Result<String, String> result = Result.success("Success");
        result.mapToOptional(Optional::of).consumeEither(
                val -> assertThat(val).isEqualTo("Success"),
                () -> fail("Should not be empty"),
                err -> fail("Should not be error")
        );
    }

    @Test
    void mapToOptional_success_shouldMapToEmpty() {
        Result<String, String> result = Result.success("Success");
        result.mapToOptional(val -> Optional.empty()).consumeEither(
                val -> fail("Should not have value"),
                () -> {},
                err -> fail("Should not be error")
        );
    }

    @Test
    void mapToOptional_error_shouldKeepError() {
        Result<String, String> result = Result.error("Error");
        result.mapToOptional(Optional::of).consumeEither(
                val -> fail("Should not have value"),
                () -> fail("Should not be error"),
                err -> assertThat(err).isEqualTo("Error")
        );
    }

}