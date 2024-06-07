package com.metacontent.cobblenav.networking.server;

import com.cobblemon.mod.common.Cobblemon;
import com.metacontent.cobblenav.store.ContactData;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

import static com.metacontent.cobblenav.networking.CobblenavPackets.CONTACT_DATA_PACKET;

public class ContactDataRequestReceiver {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        server.execute(() -> {
            ContactData contactData = (ContactData) Cobblemon.playerData.get(player).getExtraData().getOrDefault(ContactData.NAME, null);
            if (contactData == null) {
                contactData = new ContactData();
                Cobblemon.playerData.get(player).getExtraData().put(ContactData.NAME, contactData);
            }
            PacketByteBuf responseBuf = PacketByteBufs.create();
            responseBuf.writeCollection(contactData.getContacts().values(), (buf1, contact) -> contact.saveToBuf(buf1));
            responseSender.sendPacket(CONTACT_DATA_PACKET, responseBuf);
        });
    }
}
