package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.*;

class Result_Handle_Test {

    @Test
    void handle_success_shouldContainValue() {
        Result<String, Exception> result = Result.handle(() -> "Success");
        result.consumeEither(
                val -> assertThat(val).isEqualTo("Success"),
                err -> fail("Should not be error")
        );
    }

    @Test
    void handle_success_shouldContainException() {
        Result<String, Exception> result = Result.handle(() -> {
            throw new IOException("Error");
        });
        result.consumeEither(
                val -> fail("Should not be success"),
                err -> assertThat(err).isInstanceOf(IOException.class)
        );
    }

    @Test
    void handle_nullCallableGivesNPE() {
        assertThatThrownBy(() -> Result.handle(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void handle_nullValueFromCallableGivesNPE() {
        assertThatThrownBy(() -> Result.handle(() -> null))
                .isInstanceOf(NullPointerException.class);
    }
}