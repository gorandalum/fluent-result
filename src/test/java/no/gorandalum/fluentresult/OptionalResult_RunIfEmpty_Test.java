package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OptionalResult_RunIfEmpty_Test {

    @Test
    void run_success_consumerShouldNotRun() {
        List<String> resultList = new ArrayList<>();
        OptionalResult<String, String> result = OptionalResult.success("Success");
        result.runIfEmpty(() -> resultList.add("Ran"));
        assertThat(resultList).isEmpty();
    }

    @Test
    void run_empty_consumerShouldNotRun() {
        List<String> resultList = new ArrayList<>();
        OptionalResult<String, String> result = OptionalResult.empty();
        OptionalResult<String, String> finalResult =
                result.runIfEmpty(() -> resultList.add("Ran"));
        assertThat(resultList.size()).isOne();
        assertThat(resultList.get(0)).isEqualTo("Ran");
        assertThat(finalResult).isNotNull();
    }

    @Test
    void run_error_consumerShouldNotRun() {
        List<String> resultList = new ArrayList<>();
        OptionalResult<String, String> result = OptionalResult.error("Error");
        result.runIfEmpty(() -> resultList.add("Ran"));
        assertThat(resultList).isEmpty();
    }

    @Test
    void run_success_nullRunnableGivesNPE() {
        OptionalResult<String, String> result = OptionalResult.success("Success");
        assertThatThrownBy(() -> result.runIfEmpty(null))
                .isInstanceOf(NullPointerException.class);
    }
}