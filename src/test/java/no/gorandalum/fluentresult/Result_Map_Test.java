package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class Result_Map_Test {

    @Test
    void map_success_shouldMapValue() {
        Result<String, String> result = Result.success("Success");
        result.map(String::length).consumeEither(
                val -> assertThat(val).isEqualTo(7),
                err -> fail("Should not be error")
        );
    }

    @Test
    void map_error_shouldNotMap() {
        Result<String, String> result = Result.error("Error");
        result.map(val -> fail("Should not be run"))
                .consumeEither(
                        val -> fail("Should not have value"),
                        err -> assertThat(err).isEqualTo("Error")
                );
    }

    @Test
    void map_success_nullValueFromFunctionGivesNPE() {
        Result<String, String> result = Result.success("Success");
        assertThatThrownBy(() -> result.map(val -> null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void map_error_nullFunctionGivesNPE() {
        Result<String, String> result = Result.error("Error");
        assertThatThrownBy(() -> result.map(null))
                .isInstanceOf(NullPointerException.class);
    }
}