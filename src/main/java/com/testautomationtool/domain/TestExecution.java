package com.testautomationtool.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TestExecution.
 */
@Entity
@Table(name = "test_execution")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestExecution extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "message")
    private String message;

    @Column(name = "report_url")
    private String reportUrl;

    @ManyToOne
    @JsonIgnoreProperties(value = { "testExecutions", "user" }, allowSetters = true)
    private TestScenario testScenario;

    public Long getId() {
        return this.id;
    }

    public TestExecution id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getStatus() {
        return this.status;
    }

    public TestExecution status(Boolean status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return this.message;
    }

    public TestExecution message(String message) {
        this.setMessage(message);
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReportUrl() {
        return this.reportUrl;
    }

    public TestExecution reportUrl(String reportUrl) {
        this.setReportUrl(reportUrl);
        return this;
    }

    public void setReportUrl(String reportUrl) {
        this.reportUrl = reportUrl;
    }

    public TestExecution createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public TestExecution createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public TestExecution lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public TestExecution lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public TestScenario getTestScenario() {
        return this.testScenario;
    }

    public void setTestScenario(TestScenario testScenario) {
        this.testScenario = testScenario;
    }

    public TestExecution testScenario(TestScenario testScenario) {
        this.setTestScenario(testScenario);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestExecution)) {
            return false;
        }
        return id != null && id.equals(((TestExecution) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestExecution{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", message='" + getMessage() + "'" +
            ", reportUrl='" + getReportUrl() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
