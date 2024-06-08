package com.metacontent.cobblenav.util.finder;

import com.cobblemon.mod.common.api.abilities.PotentialAbility;
import com.cobblemon.mod.common.api.moves.BenchedMove;
import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.moves.MoveTemplate;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.abilities.HiddenAbility;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public interface PokemonFeatureHelper {
    static String getEggMoveName(Pokemon pokemon) {
        String name = "";
        List<MoveTemplate> eggMoves = pokemon.getForm().getMoves().getEggMoves();
        List<MoveTemplate> levelupMoves = new ArrayList<>();
        pokemon.getForm().getMoves().getLevelUpMoves().values().forEach(levelupMoves::addAll);
        for (Move move : pokemon.getMoveSet()) {
            MoveTemplate moveTemplate = move.getTemplate();
            if (eggMoves.contains(moveTemplate) && !levelupMoves.contains(moveTemplate)) {
                name = move.getName();
                break;
            }
        }
        if (name.isEmpty()) {
            for (BenchedMove benchedMove : pokemon.getBenchedMoves()) {
                MoveTemplate moveTemplate = benchedMove.getMoveTemplate();
                if (eggMoves.contains(moveTemplate) && !levelupMoves.contains(moveTemplate)) {
                    name = moveTemplate.getName();
                    break;
                }
            }
        }
        return name;
    }

    static int getPerfectIvsAmount(Pokemon pokemon) {
        AtomicInteger amount = new AtomicInteger();
        pokemon.getIvs().forEach(iv -> {
            int value = iv.getValue();
            if (value == 31) {
                amount.getAndIncrement();
            }
        });
        return amount.get();
    }

    static boolean hasHiddenAbility(Pokemon pokemon) {
        boolean hasHiddenAbility = false;
        for (PotentialAbility potentialAbility : pokemon.getForm().getAbilities()) {
            if (potentialAbility instanceof HiddenAbility && pokemon.getAbility().getTemplate() == potentialAbility.getTemplate()) {
                hasHiddenAbility = true;
                break;
            }
            else if (potentialAbility instanceof HiddenAbility) {
                break;
            }
        }
        return hasHiddenAbility;
    }
}
