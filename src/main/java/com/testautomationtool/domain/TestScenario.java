package com.testautomationtool.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
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

    @Lob
    @Column(name = "test_steps")
    private String testSteps;

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

    @ManyToOne
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

    public String getTestSteps() {
        return this.testSteps;
    }

    public TestScenario testSteps(String testSteps) {
        this.setTestSteps(testSteps);
        return this;
    }

    public void setTestSteps(String testSteps) {
        this.testSteps = testSteps;
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
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestScenario{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", testSteps='" + getTestSteps() + "'" +
            ", numberOfExecution=" + getNumberOfExecution() +
            ", numberOfPassed=" + getNumberOfPassed() +
            ", numberOfFailed=" + getNumberOfFailed() +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
