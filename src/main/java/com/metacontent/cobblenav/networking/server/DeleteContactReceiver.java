package com.metacontent.cobblenav.networking.server;

import com.metacontent.cobblenav.store.ContactData;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class DeleteContactReceiver {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        String uuid = buf.readString();
        server.executeSync(() -> {
            ContactData.executeForDataOf(player, contactData -> contactData.getContacts().remove(uuid));
        });
    }
}
