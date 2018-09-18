package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class Result_ConsumeError_Test {

    @Test
    void consumeError_success_consumerShouldNotBeRun() {
        List<String> resultList = new ArrayList<>();
        Result<String, String> result = Result.success("Success");
        Result<String, String> finalResult =
                result.consumeError(resultList::add);
        assertThat(resultList.size()).isZero();
        assertThat(finalResult).isNotNull();
    }

    @Test
    void consumeError_error_consumerShouldBeRun() {
        List<String> resultList = new ArrayList<>();
        Result<String, String> result = Result.error("Error");
        Result<String, String> finalResult =
                result.consumeError(resultList::add);
        assertThat(resultList.size()).isOne();
        assertThat(resultList.get(0)).isEqualTo("Error");
        assertThat(finalResult).isNotNull();
    }

    @Test
    void consume_success_nullConsumerGivesNPE() {
        Result<String, String> result = Result.success("Success");
        assertThatThrownBy(() -> result.consumeError(null))
                .isInstanceOf(NullPointerException.class);
    }
}