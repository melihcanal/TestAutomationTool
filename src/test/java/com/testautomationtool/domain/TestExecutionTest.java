package com.testautomationtool.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.testautomationtool.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TestExecutionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestExecution.class);
        TestExecution testExecution1 = new TestExecution();
        testExecution1.setId(1L);
        TestExecution testExecution2 = new TestExecution();
        testExecution2.setId(testExecution1.getId());
        assertThat(testExecution1).isEqualTo(testExecution2);
        testExecution2.setId(2L);
        assertThat(testExecution1).isNotEqualTo(testExecution2);
        testExecution1.setId(null);
        assertThat(testExecution1).isNotEqualTo(testExecution2);
    }
}
