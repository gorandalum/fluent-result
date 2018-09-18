package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OptionalResult_RunEither_3Args_Test {

    @Test
    void runEither_threeArgs_success_successRunnableShouldRun() {
        List<String> resultList = new ArrayList<>();
        OptionalResult<String, String> result = OptionalResult.success("Success");
        OptionalResult<String, String> finalResult = result.runEither(
                () -> resultList.add("Run1"),
                () -> resultList.add("Run2"),
                () -> resultList.add("Run3"));
        assertThat(resultList).hasSize(1);
        assertThat(resultList.get(0)).isEqualTo("Run1");
        assertThat(finalResult).isNotNull();
    }

    @Test
    void runEither_threeArgs_success_emptyRunnableShouldRun() {
        List<String> resultList = new ArrayList<>();
        OptionalResult<String, String> result = OptionalResult.empty();
        OptionalResult<String, String> finalResult = result.runEither(
                () -> resultList.add("Run1"),
                () -> resultList.add("Run2"),
                () -> resultList.add("Run3"));
        assertThat(resultList).hasSize(1);
        assertThat(resultList.get(0)).isEqualTo("Run2");
        assertThat(finalResult).isNotNull();
    }

    @Test
    void runEither_threeArgs_error_errorRunnableShouldRun() {
        List<String> resultList = new ArrayList<>();
        OptionalResult<String, String> result = OptionalResult.error("Error");
        OptionalResult<String, String> finalResult = result.runEither(
                () -> resultList.add("Run1"),
                () -> resultList.add("Run2"),
                () -> resultList.add("Run3"));
        assertThat(resultList).hasSize(1);
        assertThat(resultList.get(0)).isEqualTo("Run3");
        assertThat(finalResult).isNotNull();
    }

    @Test
    void runEither_threeArgs_error_nullSuccessRunnableGivesNPE() {
        OptionalResult<String, String> result = OptionalResult.error("Error");
        assertThatThrownBy(() -> result.runEither(null, () -> {}, () -> {}))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void runEither_threeArgs_error_nullEmptyRunnableGivesNPE() {
        OptionalResult<String, String> result = OptionalResult.error("Error");
        assertThatThrownBy(() -> result.runEither(() -> {}, null, () -> {}))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void runEither_threeArgs_error_nullErrorRunnableGivesNPE() {
        OptionalResult<String, String> result = OptionalResult.error("Error");
        assertThatThrownBy(() -> result.runEither(() -> {}, () -> {}, null))
                .isInstanceOf(NullPointerException.class);
    }
}