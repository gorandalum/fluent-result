package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class Result_RunAlways_Test {

    @Test
    void runAlways_success_consumerShouldRun() {
        List<String> resultList = new ArrayList<>();
        Result<String, String> result = Result.success("Error");
        Result<String, String> finalResult =
                result.runAlways(() -> resultList.add("Ran"));
        assertThat(resultList.size()).isOne();
        assertThat(resultList.get(0)).isEqualTo("Ran");
        assertThat(finalResult).isNotNull();
    }

    @Test
    void runAlways_error_consumerShouldRun() {
        List<String> resultList = new ArrayList<>();
        Result<String, String> result = Result.error("Error");
        Result<String, String> finalResult =
                result.runAlways(() -> resultList.add("Ran"));
        assertThat(resultList.size()).isOne();
        assertThat(resultList.get(0)).isEqualTo("Ran");
    }

    @Test
    void runAlways_success_nullRunnableGivesNPE() {
        Result<String, String> result = Result.success("Success");
        assertThatThrownBy(() -> result.runAlways(null))
                .isInstanceOf(NullPointerException.class);
    }
}