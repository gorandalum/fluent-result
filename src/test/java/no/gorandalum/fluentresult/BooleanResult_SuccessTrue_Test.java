package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class BooleanResult_SuccessTrue_Test {

    @Test
    void mapToResult_successTrue_shouldCreateWithTrueValue() {
        BooleanResult<String> result = BooleanResult.successTrue();
        result.consumeEither(
                () -> {},
                () -> fail("Should not be false"),
                err -> fail("Should not be error"));
    }
}