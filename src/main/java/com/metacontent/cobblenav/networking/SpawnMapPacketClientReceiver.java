package com.metacontent.cobblenav.networking;

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
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
        PacketByteBuf.PacketReader<String> namePacketReader = PacketByteBuf::readString;
        PacketByteBuf.PacketReader<Float> floatPacketReader = PacketByteBuf::readFloat;
        Map<String, Float> namedProbabilities = buf.readMap(namePacketReader, floatPacketReader);
        Map<RenderablePokemon, Float> spawnMap = new HashMap<>();
        namedProbabilities.forEach((key, value) -> {
            Species species = PokemonSpecies.INSTANCE.getByName(key);
            if (species != null) {
                RenderablePokemon renderablePokemon = species.create(1).asRenderablePokemon();
                spawnMap.put(renderablePokemon, value);
            }
        });
        if (client.currentScreen instanceof LocationScreen locationScreen) {
            locationScreen.setSpawnMap(spawnMap);
            locationScreen.createSpawnInfoWidgets();
            locationScreen.setLoading(false);
            locationScreen.resetAnimationProgress();
        }
    }
}
