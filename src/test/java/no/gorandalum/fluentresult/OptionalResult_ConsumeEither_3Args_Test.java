package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class OptionalResult_ConsumeEither_3Args_Test {

    @Test
    void consumeEither_3Args_success_shouldRunValueConsumer() {
        List<String> resultList = new ArrayList<>();
        OptionalResult<String, String> result = OptionalResult.success("Success");
        OptionalResult<String, String> finalResult = result.consumeEither(
                resultList::add,
                () -> { throw new RuntimeException(); },
                err -> { throw new RuntimeException(); });
        assertThat(resultList.size()).isOne();
        assertThat(resultList.get(0)).isEqualTo("Success");
        assertThat(finalResult).isNotNull();
    }

    @Test
    void consumeEither_3Args_empty_shouldRunEmptyRunnableConsumer() {
        OptionalResult<String, String> result = OptionalResult.empty();
        OptionalResult<String, String> finalResult = result.consumeEither(
                val -> { throw new RuntimeException(); },
                () -> {},
                err -> { throw new RuntimeException(); });
        assertThat(finalResult).isNotNull();
    }

    @Test
    void consumeEither_3Args_error_shouldRunErrorConsumer() {
        List<String> resultList = new ArrayList<>();
        OptionalResult<String, String> result = OptionalResult.error("Error");
        OptionalResult<String, String> finalResult = result.consumeEither(
                val -> { throw new RuntimeException(); },
                () -> { throw new RuntimeException(); },
                resultList::add);
        assertThat(resultList.size()).isOne();
        assertThat(resultList.get(0)).isEqualTo("Error");
        assertThat(finalResult).isNotNull();
    }

    @Test
    void consumeEither_3Args_success_nullValueConsumerGivesNPE() {
        OptionalResult<String, String> result = OptionalResult.success("Success");
        assertThatThrownBy(() ->
                result.consumeEither(
                        null,
                        () -> {},
                        err -> {}))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void consumeEither_3Args_success_nullEmptyRunnableGivesNPE() {
        OptionalResult<String, String> result = OptionalResult.success("Success");
        assertThatThrownBy(() ->
                result.consumeEither(
                        val -> {},
                        null,
                        err -> {}))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void consumeEither_3Args_success_nullErrorConsumerGivesNPE() {
        OptionalResult<String, String> result = OptionalResult.success("Success");
        assertThatThrownBy(() ->
                result.consumeEither(
                        err -> {},
                        () -> {},
                        null))
                .isInstanceOf(NullPointerException.class);
    }
}