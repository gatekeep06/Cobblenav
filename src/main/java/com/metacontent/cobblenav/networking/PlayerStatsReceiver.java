package com.metacontent.cobblenav.networking;

import com.cobblemon.mod.common.client.CobblemonClient;
import com.metacontent.cobblenav.client.screen.pokenav.StatsScreen;
import com.metacontent.cobblenav.util.PlayerStats;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class PlayerStatsReceiver {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        PlayerStats playerStats = PlayerStats.fromBuf(buf);
        if (client.currentScreen instanceof StatsScreen statsScreen) {
            //TODO: replace test data
            PlayerStats testStats = new PlayerStats(46, 32, 87, 4, 98, Map.of(CobblemonClient.INSTANCE.getClientPlayerData().getStarterUUID(), 1), Date.from(Instant.now()), List.of("dark", "fairy"));
            statsScreen.createStatsDisplay(testStats);
        }
    }
}
