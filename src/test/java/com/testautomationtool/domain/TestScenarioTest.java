package com.testautomationtool.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.testautomationtool.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TestScenarioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestScenario.class);
        TestScenario testScenario1 = new TestScenario();
        testScenario1.setId(1L);
        TestScenario testScenario2 = new TestScenario();
        testScenario2.setId(testScenario1.getId());
        assertThat(testScenario1).isEqualTo(testScenario2);
        testScenario2.setId(2L);
        assertThat(testScenario1).isNotEqualTo(testScenario2);
        testScenario1.setId(null);
        assertThat(testScenario1).isNotEqualTo(testScenario2);
    }
}
