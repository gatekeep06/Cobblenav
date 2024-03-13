package com.metacontent.cobblenav.networking;

import com.cobblemon.mod.common.pokemon.RenderablePokemon;
import com.metacontent.cobblenav.client.screen.pokenav.LocationScreen;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

import java.util.*;

public class SpawnMapPacketClientReceiver {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        if (client.currentScreen instanceof LocationScreen locationScreen) {
            if (locationScreen.getBucketIndex() != buf.readInt()) {
                return;
            }
            PacketByteBuf.PacketReader<RenderablePokemon> renderablePokemonPacketReader = RenderablePokemon.Companion::loadFromBuffer;
            PacketByteBuf.PacketReader<Float> floatPacketReader = PacketByteBuf::readFloat;
            Map<RenderablePokemon, Float> spawnMap = buf.readMap(renderablePokemonPacketReader, floatPacketReader);

            List<Map.Entry<RenderablePokemon, Float>> sortingList = new ArrayList<>(spawnMap.entrySet());
            int sortingMark = locationScreen.getSortingMark();
            Comparator<Map.Entry<RenderablePokemon, Float>> comparator = Map.Entry.comparingByValue(
                    (c1, c2) -> sortingMark * Float.compare(c1, c2)
            );
            sortingList.sort(comparator);
            Map<RenderablePokemon, Float> sortedSpawnMap = new LinkedHashMap<>();
            sortingList.forEach(entry -> sortedSpawnMap.put(entry.getKey(), entry.getValue()));

            locationScreen.setSpawnMap(sortedSpawnMap);
            locationScreen.createSpawnInfoWidgets();
            locationScreen.setLoading(false);
            locationScreen.resetAnimationProgress();
        }
    }
}
