package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BooleanResult_RunAlways_Test {

    @Test
    void runAlways_success_consumerShouldRun() {
        List<String> resultList = new ArrayList<>();
        BooleanResult<String> result = BooleanResult.success(true);
        BooleanResult<String> finalResult =
                result.runAlways(() -> resultList.add("Ran"));
        assertThat(resultList.size()).isOne();
        assertThat(resultList.get(0)).isEqualTo("Ran");
        assertThat(finalResult).isNotNull();
    }
}