package com.metacontent.cobblenav.networking;

import com.cobblemon.mod.common.pokemon.RenderablePokemon;
import com.metacontent.cobblenav.client.screen.pokenav.MainScreen;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;

public class RenderablePokemonReceiver {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        if (client.currentScreen instanceof MainScreen mainScreen && buf.readBoolean()) {
            RenderablePokemon pokemon = RenderablePokemon.Companion.loadFromBuffer(buf);
            mainScreen.createFinderShortcutWidget(pokemon);
        }
    }
}
