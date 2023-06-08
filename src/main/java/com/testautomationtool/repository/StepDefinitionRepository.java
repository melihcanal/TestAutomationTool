package com.testautomationtool.repository;

import com.testautomationtool.domain.StepDefinition;
import com.testautomationtool.domain.TestScenario;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the StepDefinition entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StepDefinitionRepository extends JpaRepository<StepDefinition, Long> {
    List<StepDefinition> findByTestScenario(TestScenario testScenario);

    void deleteByTestScenario(TestScenario testScenario);
}
