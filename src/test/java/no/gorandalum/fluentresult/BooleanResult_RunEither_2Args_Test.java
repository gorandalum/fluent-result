package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BooleanResult_RunEither_2Args_Test {

    @Test
    void runEither_twoArgs_success_successRunnableShouldRun() {
        List<String> resultList = new ArrayList<>();
        BooleanResult<String> result = BooleanResult.success(true);
        BooleanResult<String> finalResult = result.runEither(
                () -> resultList.add("Run1"),
                () -> resultList.add("Run2"));
        assertThat(resultList).hasSize(1);
        assertThat(resultList.get(0)).isEqualTo("Run1");
        assertThat(finalResult).isNotNull();
    }
}