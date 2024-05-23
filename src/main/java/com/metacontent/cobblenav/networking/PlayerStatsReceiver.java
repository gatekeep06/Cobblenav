package com.metacontent.cobblenav.networking;

import com.metacontent.cobblenav.client.screen.pokenav.StatsScreen;
import com.metacontent.cobblenav.util.PlayerStats;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;

public class PlayerStatsReceiver {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        PlayerStats playerStats = PlayerStats.fromBuf(buf);
        if (client.currentScreen instanceof StatsScreen statsScreen) {
            statsScreen.setStats(playerStats);
        }
    }
}
