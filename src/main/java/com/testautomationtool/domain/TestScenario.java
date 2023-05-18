package com.testautomationtool.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TestScenario.
 */
@Entity
@Table(name = "test_scenario")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestScenario extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "number_of_execution")
    private Long numberOfExecution;

    @Column(name = "number_of_passed")
    private Long numberOfPassed;

    @Column(name = "number_of_failed")
    private Long numberOfFailed;

    @OneToMany(mappedBy = "testScenario")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "testScenario" }, allowSetters = true)
    private Set<TestExecution> testExecutions = new HashSet<>();

    @OneToMany(mappedBy = "testScenario")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "testScenario" }, allowSetters = true)
    private List<StepDefinition> stepDefinitions = new ArrayList<>();

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    public Long getId() {
        return this.id;
    }

    public TestScenario id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public TestScenario title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public TestScenario description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getNumberOfExecution() {
        return this.numberOfExecution;
    }

    public TestScenario numberOfExecution(Long numberOfExecution) {
        this.setNumberOfExecution(numberOfExecution);
        return this;
    }

    public void setNumberOfExecution(Long numberOfExecution) {
        this.numberOfExecution = numberOfExecution;
    }

    public Long getNumberOfPassed() {
        return this.numberOfPassed;
    }

    public TestScenario numberOfPassed(Long numberOfPassed) {
        this.setNumberOfPassed(numberOfPassed);
        return this;
    }

    public void setNumberOfPassed(Long numberOfPassed) {
        this.numberOfPassed = numberOfPassed;
    }

    public Long getNumberOfFailed() {
        return this.numberOfFailed;
    }

    public TestScenario numberOfFailed(Long numberOfFailed) {
        this.setNumberOfFailed(numberOfFailed);
        return this;
    }

    public void setNumberOfFailed(Long numberOfFailed) {
        this.numberOfFailed = numberOfFailed;
    }

    public TestScenario createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public TestScenario createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public TestScenario lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public TestScenario lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public Set<TestExecution> getTestExecutions() {
        return this.testExecutions;
    }

    public void setTestExecutions(Set<TestExecution> testExecutions) {
        if (this.testExecutions != null) {
            this.testExecutions.forEach(i -> i.setTestScenario(null));
        }
        if (testExecutions != null) {
            testExecutions.forEach(i -> i.setTestScenario(this));
        }
        this.testExecutions = testExecutions;
    }

    public TestScenario testExecutions(Set<TestExecution> testExecutions) {
        this.setTestExecutions(testExecutions);
        return this;
    }

    public TestScenario addTestExecution(TestExecution testExecution) {
        this.testExecutions.add(testExecution);
        testExecution.setTestScenario(this);
        return this;
    }

    public TestScenario removeTestExecution(TestExecution testExecution) {
        this.testExecutions.remove(testExecution);
        testExecution.setTestScenario(null);
        return this;
    }

    public List<StepDefinition> getStepDefinitions() {
        return this.stepDefinitions;
    }

    public void setStepDefinitions(List<StepDefinition> stepDefinitions) {
        if (this.stepDefinitions != null) {
            this.stepDefinitions.forEach(i -> i.setTestScenario(null));
        }
        if (stepDefinitions != null) {
            stepDefinitions.forEach(i -> i.setTestScenario(this));
        }
        this.stepDefinitions = stepDefinitions;
    }

    public TestScenario stepDefinitions(List<StepDefinition> stepDefinitions) {
        this.setStepDefinitions(stepDefinitions);
        return this;
    }

    public TestScenario addStepDefinition(StepDefinition stepDefinition) {
        this.stepDefinitions.add(stepDefinition);
        stepDefinition.setTestScenario(this);
        return this;
    }

    public TestScenario removeStepDefinition(StepDefinition stepDefinition) {
        this.stepDefinitions.remove(stepDefinition);
        stepDefinition.setTestScenario(null);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TestScenario user(User user) {
        this.setUser(user);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestScenario)) {
            return false;
        }
        return id != null && id.equals(((TestScenario) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return (
            "TestScenario{" +
            "id=" +
            getId() +
            ", title='" +
            getTitle() +
            "'" +
            ", description='" +
            getDescription() +
            "'" +
            ", numberOfExecution=" +
            getNumberOfExecution() +
            ", numberOfPassed=" +
            getNumberOfPassed() +
            ", numberOfFailed=" +
            getNumberOfFailed() +
            ", createdBy='" +
            getCreatedBy() +
            "'" +
            ", createdDate='" +
            getCreatedDate() +
            "'" +
            ", lastModifiedBy='" +
            getLastModifiedBy() +
            "'" +
            ", lastModifiedDate='" +
            getLastModifiedDate() +
            "'" +
            "}"
        );
    }
}
