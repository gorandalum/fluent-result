package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class Result_Recover_Test {

    @Test
    void recover_success_shouldReturnValue() {
        Result<String, String> result = Result.success("Success");
        String recover = result.recover(e -> "Recovered");
        assertThat(recover).isEqualTo("Success");
    }

    @Test
    void recover() {
        Result<String, String> result = Result.error("Success");
        String recover = result.recover(e -> "Recovered");
        assertThat(recover).isEqualTo("Recovered");
    }
}
