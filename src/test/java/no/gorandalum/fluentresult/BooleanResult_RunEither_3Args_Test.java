package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BooleanResult_RunEither_3Args_Test {

    @Test
    void runEither_3Args_success_trueRunnableShouldRun() {
        List<String> resultList = new ArrayList<>();
        BooleanResult<String> result = BooleanResult.success(true);
        BooleanResult<String> finalResult = result.runEither(
                () -> resultList.add("Run1"),
                () -> resultList.add("Run2"),
                () -> resultList.add("Run3"));
        assertThat(resultList).hasSize(1);
        assertThat(resultList.get(0)).isEqualTo("Run1");
        assertThat(finalResult).isNotNull();
    }

    @Test
    void runEither_3Args_success_falseRunnableShouldRun() {
        List<String> resultList = new ArrayList<>();
        BooleanResult<String> result = BooleanResult.success(false);
        BooleanResult<String> finalResult = result.runEither(
                () -> resultList.add("Run1"),
                () -> resultList.add("Run2"),
                () -> resultList.add("Run3"));
        assertThat(resultList).hasSize(1);
        assertThat(resultList.get(0)).isEqualTo("Run2");
        assertThat(finalResult).isNotNull();
    }

    @Test
    void runEither_3Args_error_errorRunnableShouldRun() {
        List<String> resultList = new ArrayList<>();
        BooleanResult<String> result = BooleanResult.error("Error");
        BooleanResult<String> finalResult = result.runEither(
                () -> resultList.add("Run1"),
                () -> resultList.add("Run2"),
                () -> resultList.add("Run3"));
        assertThat(resultList).hasSize(1);
        assertThat(resultList.get(0)).isEqualTo("Run3");
        assertThat(finalResult).isNotNull();
    }

    @Test
    void runEither_3Args_error_nullSuccessRunnableGivesNPE() {
        BooleanResult<String> result = BooleanResult.error("Error");
        assertThatThrownBy(() -> result.runEither(null, () -> {}, () -> {}))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void runEither_3Args_error_nullEmptyRunnableGivesNPE() {
        BooleanResult<String> result = BooleanResult.error("Error");
        assertThatThrownBy(() -> result.runEither(() -> {}, null, () -> {}))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void runEither_3Args_error_nullErrorRunnableGivesNPE() {
        BooleanResult<String> result = BooleanResult.error("Error");
        assertThatThrownBy(() -> result.runEither(() -> {}, () -> {}, null))
                .isInstanceOf(NullPointerException.class);
    }
}