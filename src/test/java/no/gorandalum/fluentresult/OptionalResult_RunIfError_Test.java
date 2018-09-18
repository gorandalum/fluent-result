package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class OptionalResult_RunIfError_Test {

    @Test
    void runIfError_success_consumerShouldBeRun() {
        OptionalResult<String, String> result = OptionalResult.success("Success");
        OptionalResult<String, String> finalResult =
                result.runIfError(() -> fail("Should not be run"));
        assertThat(finalResult).isNotNull();
    }
}