package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.fail;

class BooleanResult_SuccessFalse_Test {

    @Test
    void mapToResult_successFlase_shouldCreateWithFalseValue() {
        BooleanResult<String> result = BooleanResult.successFalse();
        result.consumeEither(
                () -> fail("Should not be true"),
                () -> {},
                err -> fail("Should not be error"));
    }
}