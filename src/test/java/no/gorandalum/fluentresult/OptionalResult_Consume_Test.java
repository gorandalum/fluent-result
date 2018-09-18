package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class OptionalResult_Consume_Test {

    @Test
    void consume_success_consumerShouldBeRun() {
        List<Optional<String>> resultList = new ArrayList<>();
        OptionalResult<String, String> result = OptionalResult.success("Success");
        OptionalResult<String, String> finalResult = result.consume(resultList::add);
        assertThat(resultList.size()).isOne();
        assertThat(resultList.get(0)).isPresent();
        resultList.get(0).ifPresent(val -> assertThat(val).isEqualTo("Success"));
        assertThat(finalResult).isNotNull();
    }
}