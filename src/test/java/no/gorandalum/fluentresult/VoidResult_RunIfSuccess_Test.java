package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class VoidResult_RunIfSuccess_Test {

    @Test
    void runIfSuccess_success_consumerShouldRun() {
        List<String> resultList = new ArrayList<>();
        VoidResult<String> result = VoidResult.success();
        VoidResult<String> finalResult =
                result.runIfSuccess(() -> resultList.add("Ran"));
        assertThat(resultList.size()).isOne();
        assertThat(resultList.get(0)).isEqualTo("Ran");
        assertThat(finalResult).isNotNull();
    }
}