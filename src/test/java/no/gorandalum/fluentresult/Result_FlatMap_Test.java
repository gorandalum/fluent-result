package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class Result_FlatMap_Test {

    @Test
    void flatMap_success_shouldFlatMapValue() {
        Result<String, String> result = Result.success("Success");
        result.flatMap(val -> Result.success(val.length()))
                .consumeEither(
                        val -> assertThat(val).isEqualTo(7),
                        err -> fail("Should not be error")
                );
    }

    @Test
    void flatMap_error_shouldNotFlatMap() {
        Result<String, String> result = Result.error("Error");
        Result<String, String> flatMapped = result.flatMap(
                val -> fail("Should not be run"));
        flatMapped.consumeEither(
                        val -> fail("Should not have value"),
                        err -> assertThat(err).isEqualTo("Error")
                );
    }

    @Test
    void flatMap_success_nullValueFromFunctionGivesNPE() {
        Result<String, String> result = Result.success("Success");
        assertThatThrownBy(() -> result.flatMap(val -> null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void flatMap_error_nullFunctionGivesNPE() {
        Result<String, String> result = Result.error("Error");
        assertThatThrownBy(() -> result.flatMap(null))
                .isInstanceOf(NullPointerException.class);
    }
}