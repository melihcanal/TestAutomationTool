package com.testautomationtool.repository;

import com.testautomationtool.domain.TestScenario;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TestScenario entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TestScenarioRepository extends JpaRepository<TestScenario, Long> {
    @Query("select testScenario from TestScenario testScenario where testScenario.user.login = ?#{principal.preferredUsername}")
    List<TestScenario> findByUserIsCurrentUser();
}
