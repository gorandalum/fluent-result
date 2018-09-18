package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.fail;

class BooleanResult_ConsumeEither__3Args_Test {

    @Test
    void consumeEither_3Args_success_trueRunnableShouldBeRun() {
        List<String> resultList = new ArrayList<>();
        BooleanResult<String> result = BooleanResult.success(true);
        BooleanResult<String> finalResult = result.consumeEither(
                () -> resultList.add("True"),
                () -> fail("Should not be false"),
                err -> fail("Should not have error"));
        assertThat(resultList).isNotEmpty();
        assertThat(resultList.get(0)).isEqualTo("True");
        assertThat(finalResult).isNotNull();
    }

    @Test
    void consumeEither_3Args_success_falseRunnableShouldBeRun() {
        BooleanResult<String> result = BooleanResult.success(false);
        List<String> resultList = new ArrayList<>();
        BooleanResult<String> finalResult = result.consumeEither(
                () -> fail("Should not be true"),
                () -> resultList.add("False"),
                err -> fail("Should not have error"));
        assertThat(resultList).isNotEmpty();
        assertThat(resultList.get(0)).isEqualTo("False");
        assertThat(finalResult).isNotNull();
    }

    @Test
    void consumeEither_3Args_error_errorConsumerShouldBeRun() {
        List<String> resultList = new ArrayList<>();
        BooleanResult<String> result = BooleanResult.error("Error");
        BooleanResult<String> finalResult = result.consumeEither(
                () -> fail("Should not be true"),
                () -> fail("Should not be false"),
                resultList::add);
        assertThat(resultList.size()).isOne();
        assertThat(resultList.get(0)).isEqualTo("Error");
        assertThat(finalResult).isNotNull();
    }

    @Test
    void consumeEither_3Args_success_nullTrueRunnableGivesNPE() {
        BooleanResult<String> result = BooleanResult.success(true);
        assertThatThrownBy(() -> result.consumeEither(
                null,
                () -> {},
                err -> {}))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void consumeEither_3Args_success_nullFalseRunnableGivesNPE() {
        BooleanResult<String> result = BooleanResult.success(true);
        assertThatThrownBy(() -> result.consumeEither(
                () -> {},
                null,
                err -> {}))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void consumeEither_3Args_success_nullErrorConsumerGivesNPE() {
        BooleanResult<String> result = BooleanResult.success(true);
        assertThatThrownBy(() -> result.consumeEither(
                () -> {},
                () -> {},
                null))
                .isInstanceOf(NullPointerException.class);
    }
}