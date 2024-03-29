package com.metacontent.cobblenav.util;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class FoundPokemon {
    private final int entityId;
    private final BlockPos pos;
    private final int level;
    private final int potentialStarsAmount;
    private final String abilityName;
    private final String eggMoveName;
    private final boolean isAbilityHidden;

    public FoundPokemon(int entityId, BlockPos pos, int level, int potentialStarsAmount, String abilityName, String eggMoveName, boolean isAbilityHidden) {
        this.entityId = entityId;
        this.pos = pos;
        this.level = level;
        this.potentialStarsAmount = potentialStarsAmount;
        this.abilityName = abilityName;
        this.eggMoveName = eggMoveName;
        this.isAbilityHidden = isAbilityHidden;
    }

    public int getEntityId() {
        return entityId;
    }

    public BlockPos getPos() {
        return pos;
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
        buf.writeInt(level);
        buf.writeInt(potentialStarsAmount);
        buf.writeString(abilityName);
        buf.writeString(eggMoveName);
        buf.writeBoolean(isAbilityHidden);
    }

    public static FoundPokemon getFromBuf(PacketByteBuf buf) {
        int entityId = buf.readInt();
        BlockPos pos = buf.readBlockPos();
        int level = buf.readInt();
        int potentialStarsAmount = buf.readInt();
        String abilityName = buf.readString();
        String eggMoveName = buf.readString();
        boolean isAbilityHidden = buf.readBoolean();
        return new FoundPokemon(entityId, pos, level, potentialStarsAmount, abilityName, eggMoveName, isAbilityHidden);
    }
}
