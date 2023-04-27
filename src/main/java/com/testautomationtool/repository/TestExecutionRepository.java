package com.testautomationtool.repository;

import com.testautomationtool.domain.StepDefinition;
import com.testautomationtool.domain.TestExecution;
import com.testautomationtool.domain.TestScenario;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TestExecution entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TestExecutionRepository extends JpaRepository<TestExecution, Long> {
    List<TestExecution> findByTestScenario(TestScenario testScenario);
}
