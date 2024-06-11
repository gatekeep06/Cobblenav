package com.metacontent.cobblenav.config.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.JsonAdapter;
import java.lang.reflect.Type;
import java.util.Arrays;

@JsonAdapter(PokemonFinderType.TypeDeserializer.class)
public enum PokemonFinderType {
    BEST,
    CLOSEST;

    public static class TypeDeserializer implements JsonDeserializer<PokemonFinderType> {

        @Override
        public PokemonFinderType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            String string = json.getAsJsonPrimitive().getAsString().toUpperCase();
            boolean hasMatch = Arrays.stream(PokemonFinderType.values()).anyMatch(pokemonFinderType -> pokemonFinderType.name().equals(string));
            if (hasMatch) {
                return PokemonFinderType.valueOf(string);
            }
            else {
                return PokemonFinderType.BEST;
            }
        }
    }
}
