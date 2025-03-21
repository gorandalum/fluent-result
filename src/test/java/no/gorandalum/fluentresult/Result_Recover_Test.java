package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class Result_Recover_Test {

    @Test
    void recover_success_shouldReturnValue() {
        Result<String, String> result = Result.success("Success");
        result.recover(error -> Result.success("replaced error " + error))
                .consumeEither(
                        value -> assertThat(value).isEqualTo("Success"),
                        err -> fail("Should not be error")
                );
    }

    @Test
    void recover() {
        Result<String, String> result = Result.error("Error");
        result.recover(error -> Result.success("replaced error " + error))
                .consumeEither(
                        value -> assertThat(value).isEqualTo("replaced error Error"),
                        err -> fail("Should not be error")
                );
    }
}
