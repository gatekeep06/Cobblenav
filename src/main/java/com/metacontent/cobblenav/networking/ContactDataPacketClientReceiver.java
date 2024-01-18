package com.metacontent.cobblenav.networking;

import com.metacontent.cobblenav.client.screen.pokenav.ContactsScreen;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;

public class ContactDataPacketClientReceiver {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        if (client.currentScreen instanceof ContactsScreen contactsScreen) {
            NbtCompound nbt = buf.readNbt();
            if (nbt != null) {
                contactsScreen.createContactList(nbt);
            }
        }
    }
}
