package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class OptionalResult_Recover_Test {

    @Test
    void recover_success_shouldReturnValue() {
        OptionalResult<String, String> result = OptionalResult.success("Success");
        result.recover(error -> OptionalResult.success("replaced error " + error))
                .consumeEither(
                        value -> assertThat(value).isEqualTo(Optional.of("Success")),
                        err -> fail("Should not be error")
                );
    }

    @Test
    void recover_error_should_return_recovered_value() {
        OptionalResult<String, String> result = OptionalResult.error("Error");
        result.recover(error -> OptionalResult.success("replaced error " + error))
                .consumeEither(
                        value -> assertThat(value).isEqualTo(Optional.of("replaced error Error")),
                        err -> fail("Should not be error")
                );
    }
}
