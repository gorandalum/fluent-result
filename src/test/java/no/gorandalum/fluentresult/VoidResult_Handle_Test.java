package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class VoidResult_Handle_Test {

    @Test
    void handle_success_shouldBeSuccess() {
        List<String> resultList = new ArrayList<>();
        VoidResult<Exception> result = VoidResult.handle(() -> {});
        result.consumeEither(
                () -> resultList.add("Success"),
                err -> fail("Should not be error")
        );
        assertThat(resultList.get(0)).isEqualTo("Success");
    }

    @Test
    void handle_success_shouldContainException() {
        VoidResult<Exception> result = VoidResult.handle(() -> {
            throw new IOException("Error");
        });
        result.consumeEither(
                () -> fail("Should not be success"),
                err -> assertThat(err).isInstanceOf(IOException.class)
        );
    }

    @Test
    void handle_nullCallableGivesNPE() {
        assertThatThrownBy(() -> VoidResult.handle(null))
                .isInstanceOf(NullPointerException.class);
    }
}