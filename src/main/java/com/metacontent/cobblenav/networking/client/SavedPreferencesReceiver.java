package com.metacontent.cobblenav.networking.client;

import com.metacontent.cobblenav.client.screen.pokenav.LocationScreen;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;

public class SavedPreferencesReceiver {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        int bucketIndex = buf.readInt();
        int sortingMark = buf.readInt();
        if (client.currentScreen instanceof LocationScreen locationScreen) {
            locationScreen.setPreferences(bucketIndex, sortingMark);
            locationScreen.checkSpawns();
        }
    }
}
