package com.metacontent.cobblenav.networking;

import com.metacontent.cobblenav.client.CobblenavClient;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

public class TrackedEntityIdPacketClientReceiver {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        int id = buf.readInt();
        if (id == -1) {
            if (client.player != null && CobblenavClient.CONFIG.notifyIfPokemonIsNotFound) {
                client.player.sendMessage(Text.translatable("message.cobblenav.not_found_message")
                        .setStyle(Style.EMPTY.withItalic(true).withColor(0xff9a38)));
            }
        }
        else {
            CobblenavClient.TRACK_ARROW_HUD_OVERLAY.setTrackedEntityId(id);
        }
    }
}
