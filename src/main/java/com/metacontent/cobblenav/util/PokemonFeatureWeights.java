package com.metacontent.cobblenav.util;

import java.util.Map;

public record PokemonFeatureWeights(
        Map<Integer, Float> perfectIvs,
        float hiddenAbility,
        float eggMove,
        float highestLevel,
        float shiny
) {
    public static final PokemonFeatureWeights BASE_WEIGHTS = new PokemonFeatureWeights(
            Map.of(0, 0f, 1, 1f, 2, 2f, 3, 3f, 4, 4f, 5, 5f, 6, 6f),
            1f, 1f, 0f, 1f
    );

    public float getIvsWeight(int amount) {
        if (!perfectIvs.containsKey(amount)) {
            return 0f;
        }
        return perfectIvs.get(amount);
    }
}
