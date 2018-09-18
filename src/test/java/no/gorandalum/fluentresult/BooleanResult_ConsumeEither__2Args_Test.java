package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class BooleanResult_ConsumeEither__2Args_Test {

    @Test
    void consumeEither_2Args_success_valueConsumerShouldBeRun() {
        List<Boolean> resultList = new ArrayList<>();
        BooleanResult<String> result = BooleanResult.success(true);
        BooleanResult<String> finalResult = result.consumeEither(
                resultList::add,
                err -> fail("Should not have error"));
        assertThat(resultList.size()).isOne();
        assertThat(resultList.get(0)).isTrue();
        assertThat(finalResult).isNotNull();
    }

    @Test
    void consumeEither_2Args_error_errorConsumerShouldBeRun() {
        List<String> resultList = new ArrayList<>();
        BooleanResult<String> result = BooleanResult.error("Error");
        BooleanResult<String> finalResult = result.consumeEither(
                val -> fail("Should not have value"),
                resultList::add);
        assertThat(resultList.size()).isOne();
        assertThat(resultList.get(0)).isEqualTo("Error");
        assertThat(finalResult).isNotNull();
    }
}