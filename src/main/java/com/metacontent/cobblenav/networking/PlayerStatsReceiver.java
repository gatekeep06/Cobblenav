package com.metacontent.cobblenav.networking;

import com.cobblemon.mod.common.client.CobblemonClient;
import com.metacontent.cobblenav.client.screen.pokenav.StatsScreen;
import com.metacontent.cobblenav.util.PlayerStats;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;

import java.time.Instant;
import java.util.*;

public class PlayerStatsReceiver {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        PlayerStats playerStats = PlayerStats.fromBuf(buf);
        Set<String> badges = new HashSet<>(buf.readList(PacketByteBuf::readString));
        if (client.currentScreen instanceof StatsScreen statsScreen) {
            statsScreen.createStatsDisplay(playerStats, badges);
        }
    }
}
