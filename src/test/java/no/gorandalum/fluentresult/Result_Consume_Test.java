package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.fail;

class Result_Consume_Test {

    @Test
    void consume_success_consumerShouldBeRun() {
        List<String> resultList = new ArrayList<>();
        Result<String, String> result = Result.success("Success");
        Result<String, String> finalResult = result.consume(resultList::add);
        assertThat(resultList.size()).isOne();
        assertThat(resultList.get(0)).isEqualTo("Success");
        assertThat(finalResult).isNotNull();
    }

    @Test
    void consume_error_consumerShouldNotBeRun() {
        Result<String, String> result = Result.error("Error");
        Result<String, String> finalResult =
                result.consume(val -> fail("Should not be run"));
        assertThat(finalResult).isNotNull();
    }

    @Test
    void consume_error_nullConsumerGivesNPE() {
        Result<String, String> result = Result.error("Error");
        assertThatThrownBy(() -> result.consume(null))
                .isInstanceOf(NullPointerException.class);
    }
}