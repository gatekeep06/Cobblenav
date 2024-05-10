package com.metacontent.cobblenav.util;

import com.cobblemon.mod.common.api.abilities.PotentialAbility;
import com.cobblemon.mod.common.api.moves.BenchedMove;
import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.moves.MoveTemplate;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.abilities.HiddenAbility;
import com.google.common.util.concurrent.AtomicDouble;
import com.metacontent.cobblenav.Cobblenav;
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

public class BestPokemonFinder {
    private final PlayerEntity player;
    private final ServerWorld world;

    public BestPokemonFinder(PlayerEntity player, ServerWorld world) {
        this.player = player;
        this.world = world;
    }

    public List<PokemonEntity> find(String name) {
        return world.getEntitiesByClass(
                PokemonEntity.class,
                Box.of(
                        player.getPos(),
                        Cobblenav.CONFIG.findingAreaWidth,
                        Cobblenav.CONFIG.findingAreaHeight,
                        Cobblenav.CONFIG.findingAreaWidth
                ),
                (pokemonEntity -> pokemonEntity.getPokemon().isWild() && pokemonEntity.getPokemon().getForm().showdownId().equals(name))
        );
    }

    @Nullable
    public static Map.Entry<FoundPokemon, Float> selectBest(@NotNull List<PokemonEntity> entities) {
        Map<FoundPokemon, Float> ratedFoundPokemonMap = new HashMap<>();
        PokemonFeatureWeights weights = Cobblenav.CONFIG.pokemonFeatureWeights;
        FoundPokemon highestLevelPokemon = null;

        for (PokemonEntity pokemonEntity : entities) {
            Pokemon pokemon = pokemonEntity.getPokemon();
            AtomicDouble rating = new AtomicDouble(0d);
            AtomicInteger potentialStarsAmount = new AtomicInteger(0);
            boolean hasHiddenAbility = false;
            String eggMoveName = "";
            pokemon.getIvs().forEach(entry -> {
                int value = entry.getValue();
                if (value == 31) {
                    potentialStarsAmount.getAndIncrement();
                }
            });
            rating.addAndGet(weights.getIvsWeight(potentialStarsAmount.get()));
            for (PotentialAbility potentialAbility : pokemon.getForm().getAbilities()) {
                if (potentialAbility instanceof HiddenAbility && pokemon.getAbility().getTemplate() == potentialAbility.getTemplate()) {
                    rating.addAndGet(weights.hiddenAbility());
                    hasHiddenAbility = true;
                    break;
                }
                else if (potentialAbility instanceof HiddenAbility) {
                    break;
                }
            }

            List<MoveTemplate> eggMoves = pokemon.getForm().getMoves().getEggMoves();
            List<MoveTemplate> levelupMoves = new ArrayList<>();
            pokemon.getForm().getMoves().getLevelUpMoves().values().forEach(levelupMoves::addAll);
            for (Move move : pokemon.getMoveSet()) {
                MoveTemplate moveTemplate = move.getTemplate();
                if (eggMoves.contains(moveTemplate) && !levelupMoves.contains(moveTemplate)) {
                    rating.addAndGet(weights.eggMove());
                    eggMoveName = move.getName();
                    break;
                }
            }
            if (eggMoveName.isEmpty()) {
                for (BenchedMove benchedMove : pokemon.getBenchedMoves()) {
                    MoveTemplate moveTemplate = benchedMove.getMoveTemplate();
                    if (eggMoves.contains(moveTemplate) && !levelupMoves.contains(moveTemplate)) {
                        rating.addAndGet(weights.eggMove());
                        eggMoveName = moveTemplate.getName();
                        break;
                    }
                }
            }
            if (pokemon.getShiny()) {
                rating.addAndGet(weights.shiny());
            }

            FoundPokemon foundPokemon = new FoundPokemon(pokemonEntity.getId(), pokemonEntity.getBlockPos(), pokemon.getShiny(), pokemon.getLevel(),
                    potentialStarsAmount.get(), pokemon.getAbility().getName(), eggMoveName, hasHiddenAbility);

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

        return ratedFoundPokemonMap.entrySet().stream()
                .max(Map.Entry.comparingByValue()).orElse(null);
    }
}
