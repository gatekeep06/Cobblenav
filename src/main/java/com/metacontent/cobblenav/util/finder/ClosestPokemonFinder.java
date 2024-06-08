package com.metacontent.cobblenav.util.finder;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.metacontent.cobblenav.util.FoundPokemon;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ClosestPokemonFinder extends PokemonFinder {
    public ClosestPokemonFinder(PlayerEntity player, ServerWorld world) {
        super(player, world);
    }

    @Nullable
    @Override
    public FoundPokemon select(List<PokemonEntity> entities) {
        PokemonEntity closestEntity = world.getClosestEntity(
                entities, TargetPredicate.DEFAULT, player,
                player.getPos().x,
                player.getPos().y,
                player.getPos().z
        );
        if (closestEntity != null) {
            return FoundPokemon.createOf(closestEntity);
        }

        return null;
    }
}
