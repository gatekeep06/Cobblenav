package com.metacontent.cobblenav.networking;

import com.metacontent.cobblenav.client.CobblenavClient;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;

public class TrackedEntityIdPacketClientReceiver {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        int id = buf.readInt();
        CobblenavClient.TRACK_ARROW_HUD_OVERLAY.setTrackedEntityId(id);
    }
}
