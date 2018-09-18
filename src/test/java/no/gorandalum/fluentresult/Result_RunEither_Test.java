package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class Result_RunEither_Test {

    @Test
    void runEither_success_successRunnableShouldRun() {
        List<String> resultList = new ArrayList<>();
        Result<String, String> result = Result.success("Success");
        Result<String, String> finalResult = result.runEither(
                () -> resultList.add("Run1"),
                () -> resultList.add("Run2"));
        assertThat(resultList).hasSize(1);
        assertThat(resultList.get(0)).isEqualTo("Run1");
        assertThat(finalResult).isNotNull();
    }

    @Test
    void runEither_error_errorRunnableShouldRun() {
        List<String> resultList = new ArrayList<>();
        Result<String, String> result = Result.error("Error");
        Result<String, String> finalResult = result.runEither(
                () -> resultList.add("Run1"),
                () -> resultList.add("Run2"));
        assertThat(resultList).hasSize(1);
        assertThat(resultList.get(0)).isEqualTo("Run2");
        assertThat(finalResult).isNotNull();
    }

    @Test
    void runEither_success_nullSuccessRunnableGivesNPE() {
        Result<String, String> result = Result.error("Error");
        assertThatThrownBy(() -> result.runEither(null, () -> {}))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void runEither_success_nullErrorRunnableGivesNPE() {
        Result<String, String> result = Result.error("Error");
        assertThatThrownBy(() -> result.runEither(() -> {}, null))
                .isInstanceOf(NullPointerException.class);
    }
}