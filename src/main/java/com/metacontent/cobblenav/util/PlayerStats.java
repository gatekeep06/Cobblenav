package com.metacontent.cobblenav.util;

import com.cobblemon.mod.common.api.storage.player.PlayerAdvancementData;
import com.cobblemon.mod.common.api.storage.player.PlayerData;
import com.metacontent.cobblenav.store.AdditionalStatsData;
import net.minecraft.network.PacketByteBuf;

import java.util.Map;

public record PlayerStats(
        int totalPvp,
        int pvpWinnings,
        int captures,
        int shinyCaptures,
        int evolutions,
        Map<String, Integer> pvpTypeUsage
) {
    public static PlayerStats fromPlayerData(PlayerData data) {
        PlayerAdvancementData advancementData = data.getAdvancementData();
        int captures = advancementData.getTotalCaptureCount();
        int evolutions = advancementData.getTotalEvolvedCount();
        int pvpWinnings = advancementData.getTotalPvPBattleVictoryCount();
        int shinyCaptures = advancementData.getTotalShinyCaptureCount();

        AdditionalStatsData statsData = AdditionalStatsData.getFromData(data);
        int totalPvp = statsData.getTotalPvpCount();
        Map<String, Integer> pvpTypeUsage = statsData.getPvpTypeUsageCounts();

        return new PlayerStats(totalPvp, pvpWinnings, captures, shinyCaptures, evolutions, pvpTypeUsage);
    }

    public void saveToBuf(PacketByteBuf buf) {
        buf.writeInt(totalPvp);
        buf.writeInt(pvpWinnings);
        buf.writeInt(captures);
        buf.writeInt(shinyCaptures);
        buf.writeInt(evolutions);
        buf.writeMap(pvpTypeUsage, PacketByteBuf::writeString, PacketByteBuf::writeInt);
    }

    public static PlayerStats fromBuf(PacketByteBuf buf) {
        int totalPvp = buf.readInt();
        int pvpWinnings = buf.readInt();
        int captures = buf.readInt();
        int shinyCaptures = buf.readInt();
        int evolutions = buf.readInt();
        Map<String, Integer> pvpTypeUsage = buf.readMap(PacketByteBuf::readString, PacketByteBuf::readInt);
        return new PlayerStats(totalPvp, pvpWinnings, captures, shinyCaptures, evolutions, pvpTypeUsage);
    }
}
