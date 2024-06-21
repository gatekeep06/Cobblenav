package com.metacontent.cobblenav.networking.client;

import com.cobblemon.mod.common.pokemon.RenderablePokemon;
import com.metacontent.cobblenav.client.screen.pokenav.AllPokemonScreen;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;

import java.util.List;

public class AllPokemonReceiver {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        List<RenderablePokemon> allPokemon = buf.readList(RenderablePokemon.Companion::loadFromBuffer);
        client.execute(() -> client.setScreen(new AllPokemonScreen(allPokemon)));
    }
}
