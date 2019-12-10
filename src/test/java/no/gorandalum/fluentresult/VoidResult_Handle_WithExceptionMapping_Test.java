package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class VoidResult_Handle_WithExceptionMapping_Test {

    @Test
    void handle_withExceptionMapping_success_shouldBeSuccess() {
        List<String> resultList = new ArrayList<>();
        VoidResult<Exception> result = VoidResult.handle(
                () -> {},
                ex -> fail("Should not map exception"));
        result.consumeEither(
                () -> resultList.add("Success"),
                err -> fail("Should not be error")
        );
        assertThat(resultList.get(0)).isEqualTo("Success");
    }

    @Test
    void handle_withExceptionMapping_success_shouldContainException() {
        VoidResult<String> result = VoidResult.handle(
                () -> {
                    throw new IOException("Error");
                },
                Exception::getMessage);
        result.consumeEither(
                () -> fail("Should not be success"),
                err -> assertThat(err).isEqualTo("Error")
        );
    }

    @Test
    void handle_withExceptionMapping_nullCallableGivesNPE() {
        assertThatThrownBy(() -> VoidResult.handle(null, Exception::getMessage))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void handle_withExceptionMapping_nullExceptionMapperGivesNPE() {
        assertThatThrownBy(() -> VoidResult.handle(() -> {}, null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void handle_withExceptionMapping_nullValueFromExceptionMapperGivesNPE() {
        assertThatThrownBy(() -> VoidResult.handle(
                () -> {
                    throw new IOException("Error");
                },
                ex -> null))
                .isInstanceOf(NullPointerException.class);
    }
}