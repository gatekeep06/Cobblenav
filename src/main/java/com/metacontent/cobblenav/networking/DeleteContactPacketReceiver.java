package com.metacontent.cobblenav.networking;

import com.metacontent.cobblenav.util.CobblenavNbtHelper;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class DeleteContactPacketReceiver {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        String uuid = buf.readString();
        server.executeSync(() -> CobblenavNbtHelper.deleteContact(player, uuid));
    }
}
