package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class OptionalResult_Handle_WithExceptionMapping_Test {

    @Test
    void handle_withExceptionMapping_success_shouldContainValue() {
        OptionalResult<String, Exception> result = OptionalResult.handle(
                () -> Optional.of("Success"),
                ex -> fail("Should not map exception"));
        result.consumeEither(
                val -> assertThat(val).isEqualTo("Success"),
                () -> fail("Should not be empty"),
                err -> fail("Should not be error")
        );
    }

    @Test
    void handle_withExceptionMapping_success_shouldContainException() {
        OptionalResult<Integer, String> result = OptionalResult.handle(
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
        assertThatThrownBy(() -> OptionalResult.handle(null, Exception::getMessage))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void handle_withExceptionMapping_nullValueFromCallableGivesNPE() {
        assertThatThrownBy(() -> OptionalResult.handle(() -> null, Exception::getMessage))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void handle_withExceptionMapping_nullExceptionMapperGivesNPE() {
        assertThatThrownBy(() -> OptionalResult.handle(
                () -> Optional.of("Success"), null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void handle_withExceptionMapping_nullValueFromExceptionMapperGivesNPE() {
        assertThatThrownBy(() -> OptionalResult.handle(
                () -> {
                    throw new IOException("Error");
                },
                ex -> null))
                .isInstanceOf(NullPointerException.class);
    }
}