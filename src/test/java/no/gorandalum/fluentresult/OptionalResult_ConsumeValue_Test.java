package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class OptionalResult_ConsumeValue_Test {

    @Test
    void consumeValue_success_consumerShouldBeRun() {
        List<String> resultList = new ArrayList<>();
        OptionalResult<String, String> result = OptionalResult.success("Success");
        OptionalResult<String, String> finalResult = result.consumeValue(resultList::add);
        assertThat(resultList.size()).isOne();
        assertThat(resultList.get(0)).isEqualTo("Success");
        assertThat(finalResult).isNotNull();
    }

    @Test
    void consumeValue_error_consumerShouldNotBeRun() {
        OptionalResult<String, String> result = OptionalResult.error("Error");
        OptionalResult<String, String> finalResult =
                result.consumeValue(val -> fail("Should not be run"));
        assertThat(finalResult).isNotNull();
    }

    @Test
    void consumeValue_error_nullConsumerGivesNPE() {
        OptionalResult<String, String> result = OptionalResult.error("Error");
        assertThatThrownBy(() -> result.consumeValue(null))
                .isInstanceOf(NullPointerException.class);
    }
}