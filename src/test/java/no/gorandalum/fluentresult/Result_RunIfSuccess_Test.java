package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class Result_RunIfSuccess_Test {

    @Test
    void runIfSuccess_success_consumerShouldRun() {
        List<String> resultList = new ArrayList<>();
        Result<String, String> result = Result.success("Success");
        Result<String, String> finalResult =
                result.runIfSuccess(() -> resultList.add("Ran"));
        assertThat(resultList.size()).isOne();
        assertThat(resultList.get(0)).isEqualTo("Ran");
        assertThat(finalResult).isNotNull();
    }

    @Test
    void runIfSuccess_error_consumerShouldNotRun() {
        Result<String, String> result = Result.error("Error");
        Result<String, String> finalResult =
                result.runIfSuccess(() -> fail("Should not be run"));
        assertThat(finalResult).isNotNull();
    }

    @Test
    void runIfSuccess_error_nullRunnableGivesNPE() {
        Result<String, String> result = Result.error("Error");
        assertThatThrownBy(() -> result.runIfSuccess(null))
                .isInstanceOf(NullPointerException.class);
    }
}