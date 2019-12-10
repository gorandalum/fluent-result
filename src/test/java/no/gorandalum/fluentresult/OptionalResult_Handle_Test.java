package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class OptionalResult_Handle_Test {

    @Test
    void handle_success_shouldContainValue() {
        OptionalResult<String, Exception> result =
                OptionalResult.handle(() -> Optional.of("Success"));
        result.consumeEither(
                val -> assertThat(val).isEqualTo("Success"),
                () -> fail("Should not be empty"),
                err -> fail("Should not be error")
        );
    }

    @Test
    void handle_success_shouldContainException() {
        OptionalResult<String, Exception> result = OptionalResult.handle(() -> {
            throw new IOException("Error");
        });
        result.consumeEither(
                val -> fail("Should not be success"),
                err -> assertThat(err).isInstanceOf(IOException.class)
        );
    }

    @Test
    void handle_nullCallableGivesNPE() {
        assertThatThrownBy(() -> OptionalResult.handle(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void handle_nullValueFromCallableGivesNPE() {
        assertThatThrownBy(() -> OptionalResult.handle(() -> null))
                .isInstanceOf(NullPointerException.class);
    }
}