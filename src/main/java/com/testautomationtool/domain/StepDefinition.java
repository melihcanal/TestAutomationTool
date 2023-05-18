package com.testautomationtool.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.testautomationtool.domain.enumeration.ActionType;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A StepDefinition.
 */
@Entity
@Table(name = "step_definition")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StepDefinition extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "action_type")
    private ActionType actionType;

    @Column(name = "message")
    private String message;

    @Column(name = "xpath_or_css_selector")
    private String xpathOrCssSelector;

    @Column(name = "keyword")
    private String keyword;

    @Column(name = "scroll_left")
    private Integer scrollLeft;

    @Column(name = "scroll_top")
    private Integer scrollTop;

    @Column(name = "url")
    private String url;

    @Column(name = "expected")
    private String expected;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = { "testExecutions", "stepDefinitions", "user" }, allowSetters = true)
    private TestScenario testScenario;

    public Long getId() {
        return this.id;
    }

    public StepDefinition id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ActionType getActionType() {
        return this.actionType;
    }

    public StepDefinition actionType(ActionType actionType) {
        this.setActionType(actionType);
        return this;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public String getMessage() {
        return this.message;
    }

    public StepDefinition message(String message) {
        this.setMessage(message);
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getXpathOrCssSelector() {
        return this.xpathOrCssSelector;
    }

    public StepDefinition xpathOrCssSelector(String xpathOrCssSelector) {
        this.setXpathOrCssSelector(xpathOrCssSelector);
        return this;
    }

    public void setXpathOrCssSelector(String xpathOrCssSelector) {
        this.xpathOrCssSelector = xpathOrCssSelector;
    }

    public String getKeyword() {
        return this.keyword;
    }

    public StepDefinition keyword(String keyword) {
        this.setKeyword(keyword);
        return this;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getScrollLeft() {
        return this.scrollLeft;
    }

    public StepDefinition scrollLeft(Integer scrollLeft) {
        this.setScrollLeft(scrollLeft);
        return this;
    }

    public void setScrollLeft(Integer scrollLeft) {
        this.scrollLeft = scrollLeft;
    }

    public Integer getScrollTop() {
        return this.scrollTop;
    }

    public StepDefinition scrollTop(Integer scrollTop) {
        this.setScrollTop(scrollTop);
        return this;
    }

    public void setScrollTop(Integer scrollTop) {
        this.scrollTop = scrollTop;
    }

    public String getUrl() {
        return this.url;
    }

    public StepDefinition url(String url) {
        this.setUrl(url);
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getExpected() {
        return this.expected;
    }

    public StepDefinition expected(String expected) {
        this.setExpected(expected);
        return this;
    }

    public void setExpected(String expected) {
        this.expected = expected;
    }

    public StepDefinition createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public StepDefinition createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public StepDefinition lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public StepDefinition lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public TestScenario getTestScenario() {
        return this.testScenario;
    }

    public void setTestScenario(TestScenario testScenario) {
        this.testScenario = testScenario;
    }

    public StepDefinition testScenario(TestScenario testScenario) {
        this.setTestScenario(testScenario);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StepDefinition)) {
            return false;
        }
        return id != null && id.equals(((StepDefinition) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return (
            "StepDefinition{" +
            "id=" +
            getId() +
            ", actionType='" +
            getActionType() +
            "'" +
            ", message='" +
            getMessage() +
            "'" +
            ", xpathOrCssSelector='" +
            getXpathOrCssSelector() +
            "'" +
            ", keyword='" +
            getKeyword() +
            "'" +
            ", scrollLeft=" +
            getScrollLeft() +
            ", scrollTop=" +
            getScrollTop() +
            ", url='" +
            getUrl() +
            "'" +
            ", expected='" +
            getExpected() +
            "'" +
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
