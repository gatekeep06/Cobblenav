package com.metacontent.cobblenav.networking;

import com.cobblemon.mod.common.pokemon.RenderablePokemon;
import com.metacontent.cobblenav.client.screen.pokenav.LocationScreen;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class SpawnMapPacketClientReceiver {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        PacketByteBuf.PacketReader<RenderablePokemon> renderablePokemonPacketReader = RenderablePokemon.Companion::loadFromBuffer;
        PacketByteBuf.PacketReader<Float> floatPacketReader = PacketByteBuf::readFloat;
        Map<RenderablePokemon, Float> spawnMap = buf.readMap(renderablePokemonPacketReader, floatPacketReader);
        //Map<RenderablePokemon, Float> sortedSpawnMap = new TreeMap<>(Comparator.comparing(spawnMap::get));
        //sortedSpawnMap.putAll(spawnMap);
        if (client.currentScreen instanceof LocationScreen locationScreen) {
            locationScreen.setSpawnMap(spawnMap);
            locationScreen.createSpawnInfoWidgets();
            locationScreen.setLoading(false);
            locationScreen.resetAnimationProgress();
        }
    }
}
