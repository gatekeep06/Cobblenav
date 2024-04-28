package com.metacontent.cobblenav.config.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Arrays;

public enum PercentageDisplayType {
    PERCENT_ONLY,
    PERMILLE_ALLOWED;

    public static class TypeDeserializer implements JsonDeserializer<PercentageDisplayType> {
        @Override
        public PercentageDisplayType deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            String string = jsonElement.getAsJsonPrimitive().getAsString();
            boolean hasMatch = Arrays.stream(PercentageDisplayType.values()).anyMatch(percentageDisplayType -> percentageDisplayType.name().toLowerCase().equals(string));
            if (hasMatch) {
                return PercentageDisplayType.valueOf(string.toUpperCase());
            }
            else {
                return PercentageDisplayType.PERCENT_ONLY;
            }
        }
    }
}
