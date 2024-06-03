package com.metacontent.cobblenav.networking;

import com.metacontent.cobblenav.Cobblenav;
import kotlin.Pair;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import us.timinc.mc.cobblemon.counter.api.CaptureApi;

public class StreakRequestReceiver {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        server.execute(() -> {
            PacketByteBuf responseBuf = PacketByteBufs.create();
            if (FabricLoader.getInstance().isModLoaded("cobblemon_counter") && Cobblenav.CONFIG.useCounterIntegration) {
                Pair<String, Integer> streak = CaptureApi.INSTANCE.getStreak(player);
                responseBuf.writeBoolean(true);
                responseBuf.writeString(streak.component1());
                responseBuf.writeInt(streak.component2());
            }
            else {
                responseBuf.writeBoolean(false);
            }
            responseSender.sendPacket(CobblenavPackets.STREAK_PACKET, responseBuf);
        });
    }
}
