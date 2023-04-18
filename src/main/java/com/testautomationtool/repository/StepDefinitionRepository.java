package com.testautomationtool.repository;

import com.testautomationtool.domain.StepDefinition;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the StepDefinition entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StepDefinitionRepository extends JpaRepository<StepDefinition, Long> {}
