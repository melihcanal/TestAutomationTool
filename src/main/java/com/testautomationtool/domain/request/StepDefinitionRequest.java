package com.testautomationtool.domain.request;

import com.testautomationtool.domain.StepDefinition;
import java.util.List;

public class StepDefinitionRequest {

    List<StepDefinition> stepDefinitionList;

    public List<StepDefinition> getStepDefinitionList() {
        return stepDefinitionList;
    }

    public void setStepDefinitionList(List<StepDefinition> stepDefinitionList) {
        this.stepDefinitionList = stepDefinitionList;
    }

    @Override
    public String toString() {
        return "StepDefinitionRequest{" + "stepDefinitionList=" + stepDefinitionList + '}';
    }
}
