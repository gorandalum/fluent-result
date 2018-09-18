package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class Result_RunIfError_Test {

    @Test
    void runIfError_success_consumerShouldBeRun() {
        Result<String, String> result = Result.success("Success");
        Result<String, String> finalResult =
                result.runIfError(() -> fail("Should not be run"));
        assertThat(finalResult).isNotNull();
    }

    @Test
    void runIfError_error_consumerShouldBeRun() {
        List<String> resultList = new ArrayList<>();
        Result<String, String> result = Result.error("Error");
        Result<String, String> finalResult =
                result.runIfError(() -> resultList.add("Ran"));
        assertThat(resultList.size()).isOne();
        assertThat(resultList.get(0)).isEqualTo("Ran");
        assertThat(finalResult).isNotNull();
    }

    @Test
    void runIfError_success_nullRunnableGivesNPE() {
        Result<String, String> result = Result.success("Success");
        assertThatThrownBy(() -> result.runIfError(null))
                .isInstanceOf(NullPointerException.class);
    }
}