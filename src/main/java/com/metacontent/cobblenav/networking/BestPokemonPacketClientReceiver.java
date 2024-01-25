package com.metacontent.cobblenav.networking;

import com.metacontent.cobblenav.client.screen.pokenav.FinderScreen;
import com.metacontent.cobblenav.util.FoundPokemon;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

public class BestPokemonPacketClientReceiver {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        boolean hasFoundPokemon = buf.readBoolean();
        if (client.currentScreen instanceof FinderScreen finderScreen) {
            if (hasFoundPokemon) {
                FoundPokemon foundPokemon = FoundPokemon.getFromBuf(buf);
                finderScreen.setFoundPokemon(foundPokemon);
            }
            else {
                finderScreen.setFoundPokemon(null);
            }
            finderScreen.setLoading(false);
        }
    }
}
