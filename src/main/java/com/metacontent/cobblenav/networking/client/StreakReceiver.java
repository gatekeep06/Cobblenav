package com.metacontent.cobblenav.networking.client;

import com.metacontent.cobblenav.client.screen.pokenav.FinderScreen;
import kotlin.Pair;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;

public class StreakReceiver {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        boolean useCounter = buf.readBoolean();
        if (client.currentScreen instanceof FinderScreen finderScreen) {
            if (useCounter) {
                String s = buf.readString();
                int i = buf.readInt();
                finderScreen.setStreak(new Pair<>(s, i));
            }
        }
    }
}
