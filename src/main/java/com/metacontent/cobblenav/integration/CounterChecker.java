package com.metacontent.cobblenav.integration;

import com.cobblemon.mod.common.pokemon.Species;
import net.minecraft.entity.player.PlayerEntity;
import us.timinc.mc.cobblemon.counter.api.EncounterApi;

public class CounterChecker implements SeenPokemonChecker {
    @Override
    public boolean check(Species species, PlayerEntity player) {
        return EncounterApi.INSTANCE.check(player, species.getName().toLowerCase());
    }
}
