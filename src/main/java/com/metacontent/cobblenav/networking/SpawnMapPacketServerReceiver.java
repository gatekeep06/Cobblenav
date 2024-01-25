package com.metacontent.cobblenav.networking;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.api.spawning.CobblemonWorldSpawnerManager;
import com.cobblemon.mod.common.api.spawning.SpawnBucket;
import com.cobblemon.mod.common.api.spawning.SpawnCause;
import com.cobblemon.mod.common.api.spawning.WorldSlice;
import com.cobblemon.mod.common.api.spawning.context.AreaSpawningContext;
import com.cobblemon.mod.common.api.spawning.detail.PokemonSpawnDetail;
import com.cobblemon.mod.common.api.spawning.detail.SpawnDetail;
import com.cobblemon.mod.common.api.spawning.spawner.PlayerSpawner;
import com.cobblemon.mod.common.api.spawning.spawner.SpawningArea;
import com.cobblemon.mod.common.config.CobblemonConfig;
import com.cobblemon.mod.common.pokemon.RenderablePokemon;
import com.cobblemon.mod.common.pokemon.Species;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

import java.util.*;

import static com.metacontent.cobblenav.networking.CobblenavPackets.SPAWN_MAP_PACKET_CLIENT;

public class SpawnMapPacketServerReceiver {
    public static final List<String> BUCKET_NAMES = List.of("common", "uncommon", "rare", "ultra-rare");

    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        CobblemonConfig config = Cobblemon.config;

        int bucketIndex = buf.readInt();

        if (config.getEnableSpawning()) {
            PlayerSpawner spawner = CobblemonWorldSpawnerManager.INSTANCE.getSpawnersForPlayers().get(player.getUuid());
            SpawnBucket bucket = Cobblemon.INSTANCE.getBestSpawner().getConfig().getBuckets().stream()
                    .filter(b -> BUCKET_NAMES.get(bucketIndex).equalsIgnoreCase(b.name)).findFirst().orElse(null);
            if (spawner != null && bucket != null) {
//                Map<String, Float> namedProbabilities = new HashMap<>();
                SpawnCause cause = new SpawnCause(spawner, bucket, player);

                WorldSlice slice = spawner.getProspector().prospect(spawner, new SpawningArea(cause, (ServerWorld) player.getWorld(),
                        (int) Math.ceil(player.getX() - (double) config.getWorldSliceDiameter() / 2),
                        (int) Math.ceil(player.getY() - (double) config.getWorldSliceHeight() / 2),
                        (int) Math.ceil(player.getZ() - (double) config.getWorldSliceDiameter() / 2),
                        config.getWorldSliceDiameter(),
                        config.getWorldSliceHeight(),
                        config.getWorldSliceDiameter()));

                List<AreaSpawningContext> contexts = spawner.getResolver().resolve(spawner, spawner.getContextCalculators(), slice);

                Map<SpawnDetail, Float> spawnProbabilities = spawner.getSpawningSelector().getProbabilities(spawner, contexts);

                Map<RenderablePokemon, Float> spawnMap = new HashMap<>();

                spawnProbabilities.forEach((key, value) -> {
                    if (key instanceof PokemonSpawnDetail pokemonSpawnDetail && pokemonSpawnDetail.isValid()) {
                        RenderablePokemon renderablePokemon = pokemonSpawnDetail.getPokemon().asRenderablePokemon();
                        if (renderablePokemon.getSpecies().getImplemented() && !renderablePokemon.getSpecies().getLabels().contains("not_modeled")) {
                            spawnMap.put(renderablePokemon, value);
                        }
                    }
//                    if (key.getName().toString() != null) {
//                        String[] s = key.getName().toString().split("\\.");
//                        String name = s[s.length - 2];
//                        if (!namedProbabilities.containsKey(name)) {
//                            namedProbabilities.put(name, value);
//                        }
//                    }
                });

//                namedProbabilities.forEach((key, value) -> {
//                    Species species = PokemonSpecies.INSTANCE.getByName(key);
//                    if (species != null) {
//                        RenderablePokemon renderablePokemon = species.create(10).asRenderablePokemon();
//                        spawnMap.put(renderablePokemon, value);
//                    }
//                });

                PacketByteBuf responseBuf = PacketByteBufs.create();
                PacketByteBuf.PacketWriter<RenderablePokemon> renderablePokemonPacketWriter = (packetByteBuf, pokemon) -> pokemon.saveToBuffer(packetByteBuf);
                PacketByteBuf.PacketWriter<Float> floatPacketWriter = PacketByteBuf::writeFloat;
                responseBuf.writeMap(spawnMap, renderablePokemonPacketWriter, floatPacketWriter);

                responseSender.sendPacket(SPAWN_MAP_PACKET_CLIENT, responseBuf);
            }
        }
    }
}
