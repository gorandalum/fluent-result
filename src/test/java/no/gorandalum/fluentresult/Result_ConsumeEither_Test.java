package no.gorandalum.fluentresult;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class Result_ConsumeEither_Test {


    private boolean passed;

    @BeforeEach
    void beforeEach() {
        passed = false;
    }

    @Test
    void consumeEither_success_valueConsumerShouldBeRun() {
        List<String> resultList = new ArrayList<>();
        Result<String, String> result = Result.success("Success");
        Result<String, String> finalResult =
                result.consumeEither(resultList::add, resultList::add);
        assertThat(resultList.size()).isOne();
        assertThat(resultList.get(0)).isEqualTo("Success");
        assertThat(finalResult).isNotNull();
    }

    @Test
    void consumeEither_success_errorConsumerShouldBeRun() {
        List<String> resultList = new ArrayList<>();
        Result<String, String> result = Result.error("Error");
        Result<String, String> finalResult =
                result.consumeEither(resultList::add, resultList::add);
        assertThat(resultList.size()).isOne();
        assertThat(resultList.get(0)).isEqualTo("Error");
        assertThat(finalResult).isNotNull();
    }

    @Test
    void consumeEither_success_nullValueConsumerGivesNPE() {
        Result<String, String> result = Result.success("Success");
        assertThatThrownBy(() -> result.consumeEither(null, err -> {}))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void consumeEither_success_nullErrorConsumerGivesNPE() {
        Result<String, String> result = Result.success("Success");
        assertThatThrownBy(() -> result.consumeEither(val -> {}, null))
                .isInstanceOf(NullPointerException.class);
    }
}