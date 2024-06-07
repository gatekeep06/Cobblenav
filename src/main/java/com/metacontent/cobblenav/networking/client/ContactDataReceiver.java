package com.metacontent.cobblenav.networking.client;

import com.metacontent.cobblenav.client.screen.pokenav.ContactsScreen;
import com.metacontent.cobblenav.util.PokenavContact;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;

import java.util.List;

public class ContactDataReceiver {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        if (client.currentScreen instanceof ContactsScreen contactsScreen) {
            List<PokenavContact> contacts = buf.readList(PokenavContact::fromBuf);
            if (contacts != null) {
                contactsScreen.createContactList(contacts);
            }
        }
    }
}
