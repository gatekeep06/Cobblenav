package com.metacontent.cobblenav.util.finder;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.metacontent.cobblenav.Cobblenav;
import com.metacontent.cobblenav.config.util.PokemonFinderType;
import com.metacontent.cobblenav.util.FoundPokemon;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;

import java.util.List;

public abstract class PokemonFinder {
    protected final PlayerEntity player;
    protected final ServerWorld world;

    public PokemonFinder(PlayerEntity player, ServerWorld world) {
        this.player = player;
        this.world = world;
    }

    public static PokemonFinder get(PokemonFinderType type, PlayerEntity player, ServerWorld world) {
        return switch (type) {
            case BEST -> new BestPokemonFinder(player, world);
            case CLOSEST -> new ClosestPokemonFinder(player, world);
        };
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

    abstract public FoundPokemon select(List<PokemonEntity> entities);
}
