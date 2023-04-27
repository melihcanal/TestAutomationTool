package com.testautomationtool.domain.typeadapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.testautomationtool.domain.StepDefinition;
import com.testautomationtool.domain.enumeration.ActionType;
import java.io.IOException;

public class StepDefinitionAdapter extends TypeAdapter<StepDefinition> {

    @Override
    public void write(JsonWriter writer, StepDefinition stepDefinition) throws IOException {
        writer.beginObject();
        writer.name("actionType");
        writer.value(String.valueOf(stepDefinition.getActionType()));
        writer.name("message");
        writer.value(stepDefinition.getMessage());
        writer.name("xpathOrCssSelector");
        writer.value(stepDefinition.getXpathOrCssSelector());
        writer.name("keyword");
        writer.value(stepDefinition.getKeyword());
        writer.name("scrollLeft");
        writer.value(stepDefinition.getScrollLeft());
        writer.name("scrollTop");
        writer.value(stepDefinition.getScrollTop());
        writer.name("url");
        writer.value(stepDefinition.getUrl());
        writer.name("expected");
        writer.value(stepDefinition.getExpected());
        writer.endObject();
    }

    @Override
    public StepDefinition read(JsonReader reader) throws IOException {
        StepDefinition stepDefinition = new StepDefinition();
        reader.beginObject();
        String fieldName = null;

        while (reader.hasNext()) {
            JsonToken token = reader.peek();

            if (token.equals(JsonToken.NAME)) {
                fieldName = reader.nextName();
            }

            if ("actionType".equals(fieldName)) {
                stepDefinition.setActionType(ActionType.valueOf(reader.nextString()));
            } else if ("message".equals(fieldName)) {
                stepDefinition.setMessage(reader.nextString());
            } else if ("xpathOrCssSelector".equals(fieldName)) {
                stepDefinition.setXpathOrCssSelector(reader.nextString());
            } else if ("keyword".equals(fieldName)) {
                stepDefinition.setKeyword(reader.nextString());
            } else if ("scrollLeft".equals(fieldName)) {
                stepDefinition.setScrollLeft(reader.nextInt());
            } else if ("scrollTop".equals(fieldName)) {
                stepDefinition.setScrollTop(reader.nextInt());
            } else if ("url".equals(fieldName)) {
                stepDefinition.setUrl(reader.nextString());
            } else if ("expected".equals(fieldName)) {
                stepDefinition.setExpected(reader.nextString());
            }
        }
        reader.endObject();
        return stepDefinition;
    }
}
