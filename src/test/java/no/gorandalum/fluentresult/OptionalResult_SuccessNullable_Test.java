package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class OptionalResult_SuccessNullable_Test {

    @Test
    void verifyValue_successNullable_shouldContainValue() {
        OptionalResult<String, String> result =
                OptionalResult.successNullable("Success");
        result.consumeEither(
                val -> assertThat(val).isEqualTo("Success"),
                () -> fail("Should not be empty"),
                err -> fail("Expected no error"));
    }

    @Test
    void verifyValue_successNullable_shouldBeEmpty() {
        OptionalResult<String, String> result =
                OptionalResult.successNullable(null);
        result.consumeEither(
                val -> fail("Should not have value"),
                () -> {},
                err -> fail("Should not have error"));
    }
}