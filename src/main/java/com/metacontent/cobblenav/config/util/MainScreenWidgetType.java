package com.metacontent.cobblenav.config.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.JsonAdapter;

import java.lang.reflect.Type;
import java.util.Arrays;

@JsonAdapter(MainScreenWidgetType.TypeDeserializer.class)
public enum MainScreenWidgetType {
    PARTY,
    NONE;

    public static class TypeDeserializer implements JsonDeserializer<MainScreenWidgetType> {
        @Override
        public MainScreenWidgetType deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            String string = jsonElement.getAsJsonPrimitive().getAsString();
            boolean hasMatch = Arrays.stream(MainScreenWidgetType.values()).anyMatch(mainScreenWidgetType -> mainScreenWidgetType.name().toLowerCase().equals(string));
            if (hasMatch) {
                return MainScreenWidgetType.valueOf(string.toUpperCase());
            }
            else {
                return MainScreenWidgetType.NONE;
            }
        }
    }
}
