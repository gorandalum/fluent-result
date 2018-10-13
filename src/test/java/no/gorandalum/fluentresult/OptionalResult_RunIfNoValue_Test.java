package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.fail;

class OptionalResult_RunIfNoValue_Test {

    @Test
    void runIfNoValue_success_runnableShouldNotRun() {
        OptionalResult<String, String> result = OptionalResult.success("Success");
        result.runIfNoValue(() -> fail("Should not be run"));
    }

    @Test
    void runIfNoValue_empty_runnableShouldRun() {
        List<String> resultList = new ArrayList<>();
        OptionalResult<String, String> result = OptionalResult.empty();
        OptionalResult<String, String> finalResult =
                result.runIfNoValue(() -> resultList.add("Ran"));
        assertThat(resultList.size()).isOne();
        assertThat(resultList.get(0)).isEqualTo("Ran");
        assertThat(finalResult).isNotNull();
    }

    @Test
    void runIfNoValue_error_runnableShouldNotRun() {
        List<String> resultList = new ArrayList<>();
        OptionalResult<String, String> result = OptionalResult.error("Error");
        OptionalResult<String, String> finalResult =
                result.runIfNoValue(() -> resultList.add("Ran"));
        assertThat(resultList.size()).isOne();
        assertThat(resultList.get(0)).isEqualTo("Ran");
        assertThat(finalResult).isNotNull();
    }

    @Test
    void runIfNoValue_success_nullRunnableGivesNPE() {
        OptionalResult<String, String> result = OptionalResult.success("Success");
        assertThatThrownBy(() -> result.runIfNoValue(null))
                .isInstanceOf(NullPointerException.class);
    }
}