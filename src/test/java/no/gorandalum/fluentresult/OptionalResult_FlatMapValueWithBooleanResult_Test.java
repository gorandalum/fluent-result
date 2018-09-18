package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class OptionalResult_FlatMapValueWithBooleanResult_Test {

    @Test
    void flatMapValueWithBooleanResult_success_successfullyMapValue() {
        OptionalResult<Boolean, String> result =
                OptionalResult.<String, String>success("Success")
                        .flatMapValueWithBooleanResult(val -> BooleanResult.success(val.isEmpty()));
        result.consumeEither(
                val -> assertThat(val).isFalse(),
                () -> fail("Should not be empty"),
                err -> fail("Expected no error"));
    }
}