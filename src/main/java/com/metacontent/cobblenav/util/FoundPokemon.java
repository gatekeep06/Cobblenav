package com.metacontent.cobblenav.util;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.metacontent.cobblenav.util.finder.PokemonFeatureHelper;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

import java.util.concurrent.atomic.AtomicInteger;

public class FoundPokemon {
    private final int entityId;
    private final BlockPos pos;
    private final boolean shiny;
    private final int level;
    private final int potentialStarsAmount;
    private final String abilityName;
    private final String eggMoveName;
    private final boolean isAbilityHidden;

    public FoundPokemon(int entityId, BlockPos pos, boolean shiny, int level, int potentialStarsAmount, String abilityName, String eggMoveName, boolean isAbilityHidden) {
        this.entityId = entityId;
        this.pos = pos;
        this.shiny = shiny;
        this.level = level;
        this.potentialStarsAmount = potentialStarsAmount;
        this.abilityName = abilityName;
        this.eggMoveName = eggMoveName;
        this.isAbilityHidden = isAbilityHidden;
    }

    public static FoundPokemon createOf(PokemonEntity pokemonEntity) {
        Pokemon pokemon = pokemonEntity.getPokemon();
        int potentialStarsAmount = PokemonFeatureHelper.getPerfectIvsAmount(pokemon);
        String abilityName = pokemon.getAbility().getName();
        String eggMoveName = PokemonFeatureHelper.getEggMoveName(pokemon);
        boolean hasHiddenAbility = PokemonFeatureHelper.hasHiddenAbility(pokemon);
        return new FoundPokemon(pokemonEntity.getId(), pokemonEntity.getBlockPos(), pokemon.getShiny(), pokemon.getLevel(),
                potentialStarsAmount, abilityName, eggMoveName, hasHiddenAbility);
    }

    public int getEntityId() {
        return entityId;
    }

    public BlockPos getPos() {
        return pos;
    }

    public boolean isShiny() {
        return shiny;
    }

    public int getLevel() {
        return level;
    }

    public int getPotentialStarsAmount() {
        return potentialStarsAmount;
    }

    public String getAbilityName() {
        return abilityName;
    }

    public String getEggMoveName() {
        return eggMoveName;
    }

    public boolean isAbilityHidden() {
        return isAbilityHidden;
    }

    public void saveToBuf(PacketByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeBlockPos(pos);
        buf.writeBoolean(shiny);
        buf.writeInt(level);
        buf.writeInt(potentialStarsAmount);
        buf.writeString(abilityName);
        buf.writeString(eggMoveName);
        buf.writeBoolean(isAbilityHidden);
    }

    public static FoundPokemon getFromBuf(PacketByteBuf buf) {
        int entityId = buf.readInt();
        BlockPos pos = buf.readBlockPos();
        boolean shiny = buf.readBoolean();
        int level = buf.readInt();
        int potentialStarsAmount = buf.readInt();
        String abilityName = buf.readString();
        String eggMoveName = buf.readString();
        boolean isAbilityHidden = buf.readBoolean();
        return new FoundPokemon(entityId, pos, shiny, level, potentialStarsAmount, abilityName, eggMoveName, isAbilityHidden);
    }
}
