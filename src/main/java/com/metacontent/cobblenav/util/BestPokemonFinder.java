package com.metacontent.cobblenav.util;

import com.cobblemon.mod.common.api.abilities.PotentialAbility;
import com.cobblemon.mod.common.api.moves.BenchedMove;
import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.moves.MoveTemplate;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.abilities.HiddenAbility;
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
    public static Map.Entry<FoundPokemon, Integer> selectBest(@NotNull List<PokemonEntity> entities) {
        Map<FoundPokemon, Integer> ratedFoundPokemonMap = new HashMap<>();

        entities.forEach(pokemonEntity -> {
            Pokemon pokemon = pokemonEntity.getPokemon();
            AtomicInteger rating = new AtomicInteger(0);
            AtomicInteger potentialStarsAmount = new AtomicInteger(0);
            boolean hasHiddenAbility = false;
            String eggMoveName = "";
            pokemon.getIvs().forEach(entry -> {
                int value = entry.getValue();
                if (value == 31) {
                    potentialStarsAmount.getAndIncrement();
                    rating.getAndIncrement();
                }
            });
            for (PotentialAbility potentialAbility : pokemon.getForm().getAbilities()) {
                if (potentialAbility instanceof HiddenAbility && pokemon.getAbility().getTemplate() == potentialAbility.getTemplate()) {
                    rating.getAndIncrement();
                    hasHiddenAbility = true;
                    break;
                }
            }

            List<MoveTemplate> eggMoves = pokemon.getForm().getMoves().getEggMoves();
            List<MoveTemplate> levelupMoves = new ArrayList<>();
            pokemon.getForm().getMoves().getLevelUpMoves().values().forEach(levelupMoves::addAll);
            for (Move move : pokemon.getMoveSet()) {
                MoveTemplate moveTemplate = move.getTemplate();
                if (eggMoves.contains(moveTemplate) && !levelupMoves.contains(moveTemplate)) {
                    rating.getAndIncrement();
                    eggMoveName = move.getName();
                    break;
                }
            }
            if (eggMoveName.isEmpty()) {
                for (BenchedMove benchedMove : pokemon.getBenchedMoves()) {
                    MoveTemplate moveTemplate = benchedMove.getMoveTemplate();
                    if (eggMoves.contains(moveTemplate) && !levelupMoves.contains(moveTemplate)) {
                        rating.getAndIncrement();
                        eggMoveName = moveTemplate.getName();
                        break;
                    }
                }
            }

            FoundPokemon foundPokemon = new FoundPokemon(pokemonEntity.getId(), pokemonEntity.getBlockPos(), pokemon.getLevel(),
                    potentialStarsAmount.get(), pokemon.getAbility().getName(), eggMoveName, hasHiddenAbility);

            ratedFoundPokemonMap.put(foundPokemon, rating.get());
        });

        return ratedFoundPokemonMap.entrySet().stream()
                .max(Map.Entry.comparingByValue()).orElse(null);
    }
}
