package com.testautomationtool.repository;

import com.testautomationtool.domain.TestExecution;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TestExecution entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TestExecutionRepository extends JpaRepository<TestExecution, Long> {}
