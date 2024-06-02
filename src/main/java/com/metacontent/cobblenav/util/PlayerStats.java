package com.metacontent.cobblenav.util;

import com.cobblemon.mod.common.api.storage.player.PlayerAdvancementData;
import com.cobblemon.mod.common.api.storage.player.PlayerData;
import com.metacontent.cobblenav.store.AdditionalStatsData;
import net.minecraft.network.PacketByteBuf;

import java.util.*;
import java.util.stream.Collectors;

public record PlayerStats(
        int totalPvp,
        int pvpWinnings,
        int captures,
        int shinyCaptures,
        int evolutions,
        Map<UUID, Integer> pokemonUsage,
        Date startDate,
        Set<String> gymBadges
) {
    public static PlayerStats fromPlayerData(PlayerData data) {
        PlayerAdvancementData advancementData = data.getAdvancementData();
        int captures = advancementData.getTotalCaptureCount();
        int evolutions = advancementData.getTotalEvolvedCount();
        int pvpWinnings = advancementData.getTotalPvPBattleVictoryCount();
        int shinyCaptures = advancementData.getTotalShinyCaptureCount();

        AdditionalStatsData statsData = AdditionalStatsData.getFromData(data);
        int totalPvp = statsData.getTotalPvpCount();
        Map<UUID, Integer> pokemonUsage = statsData.getPokemonUsage();
        Date startDate = statsData.getStartDate();
        Set<String> gymBadges = statsData.getGymBadges();

        return new PlayerStats(totalPvp, pvpWinnings, captures, shinyCaptures, evolutions, pokemonUsage, startDate, gymBadges);
    }

    public void saveToBuf(PacketByteBuf buf) {
        buf.writeInt(totalPvp);
        buf.writeInt(pvpWinnings);
        buf.writeInt(captures);
        buf.writeInt(shinyCaptures);
        buf.writeInt(evolutions);
        buf.writeMap(pokemonUsage, PacketByteBuf::writeUuid, PacketByteBuf::writeInt);
        buf.writeDate(startDate);
        buf.writeCollection(gymBadges, PacketByteBuf::writeString);
    }

    public static PlayerStats fromBuf(PacketByteBuf buf) {
        int totalPvp = buf.readInt();
        int pvpWinnings = buf.readInt();
        int captures = buf.readInt();
        int shinyCaptures = buf.readInt();
        int evolutions = buf.readInt();
        Map<UUID, Integer> pokemonUsage = buf.readMap(PacketByteBuf::readUuid, PacketByteBuf::readInt);
        Date startDate = buf.readDate();
        Set<String> gymBadges = new HashSet<>(buf.readList(PacketByteBuf::readString));
        return new PlayerStats(totalPvp, pvpWinnings, captures, shinyCaptures, evolutions, pokemonUsage, startDate, gymBadges);
    }
}
