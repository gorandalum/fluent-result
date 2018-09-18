package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OptionalResult_ConsumeError_Test {

    @Test
    void consumeError_success_consumerShouldNotBeRun() {
        List<String> resultList = new ArrayList<>();
        OptionalResult<String, String> result = OptionalResult.success("Success");
        OptionalResult<String, String> finalResult =
                result.consumeError(resultList::add);
        assertThat(resultList.size()).isZero();
        assertThat(finalResult).isNotNull();
    }
}