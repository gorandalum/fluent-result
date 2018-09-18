package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class Result_Run_Test {

    @Test
    void run_success_consumerShouldRun() {
        List<String> resultList = new ArrayList<>();
        Result<String, String> result = Result.success("Error");
        Result<String, String> finalResult =
                result.run(() -> resultList.add("Ran"));
        assertThat(resultList.size()).isOne();
        assertThat(resultList.get(0)).isEqualTo("Ran");
        assertThat(finalResult).isNotNull();
    }

    @Test
    void run_error_consumerShouldRun() {
        List<String> resultList = new ArrayList<>();
        Result<String, String> result = Result.error("Error");
        Result<String, String> finalResult =
                result.run(() -> resultList.add("Ran"));
        assertThat(resultList.size()).isOne();
        assertThat(resultList.get(0)).isEqualTo("Ran");
    }

    @Test
    void run_success_nullRunnableGivesNPE() {
        Result<String, String> result = Result.success("Success");
        assertThatThrownBy(() -> result.run(null))
                .isInstanceOf(NullPointerException.class);
    }
}