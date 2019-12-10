package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class BooleanResult_Handle_WithExceptionMapping_Test {

    @Test
    void handle_withExceptionMapping_success_shouldContainValue() {
        BooleanResult<Exception> result = BooleanResult.handle(
                () -> true,
                ex -> fail("Should not map exception"));
        result.consumeEither(
                val -> assertThat(val).isTrue(),
                err -> fail("Should not be error")
        );
    }

    @Test
    void handle_withExceptionMapping_success_shouldContainException() {
        BooleanResult<String> result = BooleanResult.handle(
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
        assertThatThrownBy(() -> BooleanResult.handle(null, Exception::getMessage))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void handle_withExceptionMapping_nullValueFromCallableGivesNPE() {
        assertThatThrownBy(() -> BooleanResult.handle(() -> null, Exception::getMessage))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void handle_withExceptionMapping_nullExceptionMapperGivesNPE() {
        assertThatThrownBy(() -> BooleanResult.handle(
                () -> true, null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void handle_withExceptionMapping_nullValueFromExceptionMapperGivesNPE() {
        assertThatThrownBy(() -> BooleanResult.handle(
                () -> {
                    throw new IOException("Error");
                },
                ex -> null))
                .isInstanceOf(NullPointerException.class);
    }
}