package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OptionalResult_Run_Test {

    @Test
    void run_success_consumerShouldRun() {
        List<String> resultList = new ArrayList<>();
        OptionalResult<String, String> result = OptionalResult.success("Success");
        OptionalResult<String, String> finalResult =
                result.run(() -> resultList.add("Ran"));
        assertThat(resultList.size()).isOne();
        assertThat(resultList.get(0)).isEqualTo("Ran");
        assertThat(finalResult).isNotNull();
    }
}