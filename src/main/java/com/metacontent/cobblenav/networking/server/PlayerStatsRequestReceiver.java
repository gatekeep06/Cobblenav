package com.metacontent.cobblenav.networking.server;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.player.PlayerData;
import com.metacontent.cobblenav.Cobblenav;
import com.metacontent.cobblenav.networking.CobblenavPackets;
import com.metacontent.cobblenav.util.PlayerStats;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;
import java.util.Set;

public class PlayerStatsRequestReceiver {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        server.execute(() -> {
            PlayerData data = Cobblemon.playerData.get(player);
            PlayerStats playerStats = PlayerStats.fromPlayerData(data);

            Set<String> badgeTypes = Cobblenav.CONFIG.badges.getTypes();

            PacketByteBuf responseBuf = PacketByteBufs.create();
            playerStats.saveToBuf(responseBuf);
            responseBuf.writeCollection(badgeTypes, PacketByteBuf::writeString);
            responseSender.sendPacket(CobblenavPackets.PLAYER_STATS_PACKET, responseBuf);
        });
    }
}
