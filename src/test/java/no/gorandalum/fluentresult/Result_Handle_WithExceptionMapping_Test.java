package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.*;

class Result_Handle_WithExceptionMapping_Test {

    @Test
    void handle_withExceptionMapping_success_shouldContainValue() {
        Result<String, Exception> result = Result.handle(
                () -> "Success",
                ex -> fail("Should not map exception"));
        result.consumeEither(
                val -> assertThat(val).isEqualTo("Success"),
                err -> fail("Should not be error")
        );
    }

    @Test
    void handle_withExceptionMapping_success_shouldContainException() {
        Result<Integer, String> result = Result.handle(
                () -> {
                    throw new IOException("Error");
                },
                Exception::getMessage);
        result.consumeEither(
                val -> fail("Should not be success"),
                err -> assertThat(err).isEqualTo("Error")
        );
    }

    @Test
    void handle_withExceptionMapping_nullCallableGivesNPE() {
        assertThatThrownBy(() -> Result.handle(null, Exception::getMessage))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void handle_withExceptionMapping_nullValueFromCallableGivesNPE() {
        assertThatThrownBy(() -> Result.handle(() -> null, Exception::getMessage))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void handle_withExceptionMapping_nullExceptionMapperGivesNPE() {
        assertThatThrownBy(() -> Result.handle(() -> "Success", null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void handle_withExceptionMapping_nullValueFromExceptionMapperGivesNPE() {
        assertThatThrownBy(() -> Result.handle(
                () -> {
                    throw new IOException("Error");
                },
                ex -> null))
                .isInstanceOf(NullPointerException.class);
    }
}