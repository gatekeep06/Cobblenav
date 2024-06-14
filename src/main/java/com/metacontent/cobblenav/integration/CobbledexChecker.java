package com.metacontent.cobblenav.integration;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.pokemon.Species;
import com.rafacasari.mod.cobbledex.cobblemon.extensions.PlayerDiscovery;
import net.minecraft.entity.player.PlayerEntity;

public class CobbledexChecker implements SeenPokemonChecker {
    @Override
    public boolean check(Species species, PlayerEntity player) {
        PlayerDiscovery playerDiscovery = (PlayerDiscovery) Cobblemon.playerData.get(player).getExtraData().get(PlayerDiscovery.NAME_KEY);
        return playerDiscovery.getCaughtSpecies().contains(species.getNationalPokedexNumber());
    }
}
