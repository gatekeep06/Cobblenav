package com.metacontent.cobblenav.networking;

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.RenderablePokemon;
import com.cobblemon.mod.common.pokemon.Species;
import com.metacontent.cobblenav.client.screen.pokenav.LocationScreen;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;

import java.util.HashMap;
import java.util.Map;

public class SpawnMapPacketClientReceiver {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        PacketByteBuf.PacketReader<RenderablePokemon> renderablePokemonPacketReader = RenderablePokemon.Companion::loadFromBuffer;
        PacketByteBuf.PacketReader<Float> floatPacketReader = PacketByteBuf::readFloat;
        Map<RenderablePokemon, Float> spawnMap = buf.readMap(renderablePokemonPacketReader, floatPacketReader);
        if (client.currentScreen instanceof LocationScreen locationScreen) {
            locationScreen.setSpawnMap(spawnMap);
            locationScreen.createSpawnInfoWidgets();
            locationScreen.setLoading(false);
            locationScreen.resetAnimationProgress();
        }
    }
}
