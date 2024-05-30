package com.metacontent.cobblenav.networking;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.player.PlayerData;
import com.metacontent.cobblenav.util.PlayerStats;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerStatsRequestReceiver {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        server.execute(() -> {
            PlayerData data = Cobblemon.playerData.get(player);
            PlayerStats playerStats = PlayerStats.fromPlayerData(data);
            PacketByteBuf responseBuf = PacketByteBufs.create();
            playerStats.saveToBuf(responseBuf);
            responseSender.sendPacket(CobblenavPackets.PLAYER_STATS_PACKET, responseBuf);
        });
    }
}
