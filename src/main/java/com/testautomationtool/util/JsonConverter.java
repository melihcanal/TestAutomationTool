package com.testautomationtool.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.testautomationtool.domain.StepDefinition;
import com.testautomationtool.domain.typeadapter.StepDefinitionAdapter;
import java.util.List;

public class JsonConverter {

    public static String convertStepDefinitionListToJson(List<StepDefinition> list) {
        Gson gson = new GsonBuilder().registerTypeAdapter(StepDefinition.class, new StepDefinitionAdapter()).setPrettyPrinting().create();
        return gson.toJson(list);
    }
}
