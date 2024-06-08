package com.metacontent.cobblenav.util.finder;

import com.cobblemon.mod.common.api.abilities.PotentialAbility;
import com.cobblemon.mod.common.api.moves.BenchedMove;
import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.moves.MoveTemplate;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.abilities.HiddenAbility;
import com.google.common.util.concurrent.AtomicDouble;
import com.metacontent.cobblenav.Cobblenav;
import com.metacontent.cobblenav.util.FoundPokemon;
import com.metacontent.cobblenav.util.PokemonFeatureWeights;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class BestPokemonFinder extends PokemonFinder {
    public BestPokemonFinder(PlayerEntity player, ServerWorld world) {
        super(player, world);
    }

    @Nullable
    @Override
    public FoundPokemon select(@NotNull List<PokemonEntity> entities) {
        Map<FoundPokemon, Float> ratedFoundPokemonMap = new HashMap<>();
        PokemonFeatureWeights weights = Cobblenav.CONFIG.pokemonFeatureWeights;
        FoundPokemon highestLevelPokemon = null;

        for (PokemonEntity pokemonEntity : entities) {
            Pokemon pokemon = pokemonEntity.getPokemon();
            AtomicDouble rating = new AtomicDouble(0d);
            boolean hasHiddenAbility = PokemonFeatureHelper.hasHiddenAbility(pokemon);
            if (hasHiddenAbility) {
                rating.addAndGet(weights.hiddenAbility());
            }
            int potentialStarsAmount = PokemonFeatureHelper.getPerfectIvsAmount(pokemon);
            rating.addAndGet(weights.getIvsWeight(potentialStarsAmount));
            String eggMoveName = PokemonFeatureHelper.getEggMoveName(pokemon);
            if (!eggMoveName.isEmpty()) {
                rating.addAndGet(weights.eggMove());
            }
            if (pokemon.getShiny()) {
                rating.addAndGet(weights.shiny());
            }

            FoundPokemon foundPokemon = new FoundPokemon(pokemonEntity.getId(), pokemonEntity.getBlockPos(), pokemon.getShiny(), pokemon.getLevel(),
                    potentialStarsAmount, pokemon.getAbility().getName(), eggMoveName, hasHiddenAbility);

            if (highestLevelPokemon == null) {
                highestLevelPokemon = foundPokemon;
            }
            else if (foundPokemon.getLevel() > highestLevelPokemon.getLevel()) {
                highestLevelPokemon = foundPokemon;
            }

            ratedFoundPokemonMap.put(foundPokemon, rating.floatValue());
        };

        if (highestLevelPokemon != null && ratedFoundPokemonMap.containsKey(highestLevelPokemon)) {
            float f = ratedFoundPokemonMap.get(highestLevelPokemon);
            ratedFoundPokemonMap.put(highestLevelPokemon, f + weights.highestLevel());
        }

        Map.Entry<FoundPokemon, Float> bestEntry = ratedFoundPokemonMap.entrySet().stream()
                .max(Map.Entry.comparingByValue()).orElse(null);

        if (bestEntry != null) {
            return bestEntry.getKey();
        }
        else {
            return null;
        }
    }
}
