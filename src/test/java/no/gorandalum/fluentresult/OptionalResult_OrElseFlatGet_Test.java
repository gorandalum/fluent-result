package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class OptionalResult_OrElseFlatGet_Test {

    @Test
    void flatReplaceEmpty_empty_shouldReturnSupplierValue() {
        OptionalResult<String, String> result = OptionalResult.empty();
        result.flatReplaceEmpty(() -> OptionalResult.success("Success"))
                .consumeEither(
                        val -> assertThat(val).isEqualTo("Success"),
                        () -> fail("Should not be empty"),
                        err -> fail("Should not be error")
                );
    }

    @Test
    void flatReplaceEmpty_empty_shouldReturnSupplierError() {
        OptionalResult<String, String> result = OptionalResult.empty();
        result.flatReplaceEmpty(() -> OptionalResult.error("Failed"))
                .consumeEither(
                        val -> fail("Should not be value"),
                        () -> fail("Should not be empty"),
                        err -> assertThat(err).isEqualTo("Failed")
                );
    }
}