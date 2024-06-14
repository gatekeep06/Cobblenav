package com.metacontent.cobblenav.integration;

import com.cobblemon.mod.common.pokemon.Species;
import net.minecraft.entity.player.PlayerEntity;

public interface SeenPokemonChecker {
    boolean check(Species species, PlayerEntity player);
}
