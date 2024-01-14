package com.metacontent.cobblenav.networking;

import com.metacontent.cobblenav.util.ContactSaverEntity;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

import static com.metacontent.cobblenav.networking.CobblenavPackets.CONTACT_DATA_PACKET_CLIENT;

public class ContactDataPacketServerReceiver {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        if (player instanceof ContactSaverEntity contactSaverEntity) {
            NbtCompound nbt = contactSaverEntity.cobblenav$getContactData();

            PacketByteBuf responseBuf = PacketByteBufs.create();
            responseBuf.writeNbt(nbt);
            responseSender.sendPacket(CONTACT_DATA_PACKET_CLIENT, responseBuf);
        }
    }
}
