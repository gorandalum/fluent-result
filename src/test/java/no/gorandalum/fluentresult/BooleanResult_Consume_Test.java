package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class BooleanResult_Consume_Test {

    @Test
    void consume_success_consumerShouldBeRun() {
        List<Boolean> resultList = new ArrayList<>();
        BooleanResult<String> result = BooleanResult.success(true);
        BooleanResult<String> finalResult = result.consume(resultList::add);
        assertThat(resultList.size()).isOne();
        assertThat(resultList.get(0)).isTrue();
        assertThat(finalResult).isNotNull();
    }
}