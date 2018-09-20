package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class BooleanResult_Map_Test {

    @Test
    void map_success_successfullyMapValue() {
        Result<Integer, String> result =
                BooleanResult.<String>success(true)
                        .map(val -> val ? 5 : 3);
        result.consumeEither(
                val -> assertThat(val).isEqualTo(5),
                err -> fail("Should not be error"));
    }
}