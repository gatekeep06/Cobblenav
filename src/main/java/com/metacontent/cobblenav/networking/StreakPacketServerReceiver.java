package com.metacontent.cobblenav.networking;

import kotlin.Pair;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import us.timinc.mc.cobblemon.counter.api.CaptureApi;

public class StreakPacketServerReceiver {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        server.execute(() -> {
            Pair<String, Integer> streak = CaptureApi.INSTANCE.getStreak(player);
            PacketByteBuf responseBuf = PacketByteBufs.create();
            responseBuf.writeString(streak.component1());
            responseBuf.writeInt(streak.component2());
            responseSender.sendPacket(CobblenavPackets.STREAK_PACKET_CLIENT, responseBuf);
        });
    }
}
