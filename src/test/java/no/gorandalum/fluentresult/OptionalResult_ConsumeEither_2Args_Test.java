package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.fail;

class OptionalResult_ConsumeEither_2Args_Test {

    @Test
    void consumeEither_2Args_success_shouldRunValueConsumer() {
        List<Optional<String>> resultList = new ArrayList<>();
        OptionalResult<String, String> result = OptionalResult.success("Success");
        OptionalResult<String, String> finalResult = result.consumeEither(
                resultList::add,
                err -> fail("Should not run"));
        assertThat(resultList.size()).isOne();
        assertThat(resultList.get(0)).isPresent();
        resultList.get(0).ifPresent(val -> assertThat(val).isEqualTo("Success"));
        assertThat(finalResult).isNotNull();
    }

}