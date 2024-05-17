package com.metacontent.cobblenav.networking;

import com.metacontent.cobblenav.client.screen.pokenav.FinderScreen;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;

public class StreakPacketClientReceiver {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        String s = buf.readString();
        int i = buf.readInt();
        if (client.currentScreen instanceof FinderScreen finderScreen) {

        }
    }
}
