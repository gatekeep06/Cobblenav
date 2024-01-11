package com.metacontent.cobblenav.networking;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.api.spawning.CobblemonWorldSpawnerManager;
import com.cobblemon.mod.common.api.spawning.SpawnBucket;
import com.cobblemon.mod.common.api.spawning.SpawnCause;
import com.cobblemon.mod.common.api.spawning.WorldSlice;
import com.cobblemon.mod.common.api.spawning.context.AreaSpawningContext;
import com.cobblemon.mod.common.api.spawning.detail.SpawnDetail;
import com.cobblemon.mod.common.api.spawning.spawner.PlayerSpawner;
import com.cobblemon.mod.common.api.spawning.spawner.SpawningArea;
import com.cobblemon.mod.common.config.CobblemonConfig;
import com.cobblemon.mod.common.pokemon.RenderablePokemon;
import com.cobblemon.mod.common.pokemon.Species;
import com.metacontent.cobblenav.Cobblenav;
import com.metacontent.cobblenav.client.screen.pokenav.LocationScreen;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CobblenavPackets {
    public static final Identifier SPAWN_MAP_PACKET_SERVER = new Identifier(Cobblenav.ID, "spawn_map_packet_server");
    public static final Identifier SPAWN_MAP_PACKET_CLIENT = new Identifier(Cobblenav.ID, "spawn_map_packet_client");

    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(SPAWN_MAP_PACKET_SERVER, ((server, player, handler, buf, responseSender) -> {
            Map<String, Float> namedProbabilities = new HashMap<>();

            CobblemonConfig config = Cobblemon.config;

            int bucketIndex = buf.readInt();

            if (config.getEnableSpawning()) {
                PlayerSpawner spawner = CobblemonWorldSpawnerManager.INSTANCE.getSpawnersForPlayers().get(player.getUuid());
                SpawnBucket bucket = Cobblemon.INSTANCE.getBestSpawner().getConfig().getBuckets().stream()
                        .filter(b -> LocationScreen.BUCKET_NAMES.get(bucketIndex).equalsIgnoreCase(b.name)).findFirst().orElse(null);
                if (spawner != null && bucket != null) {
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

                    spawnProbabilities.forEach((key, value) -> {
                        if (key.getName().toString() != null) {
                            String name = key.getName().getString().toLowerCase().replaceAll(" ", "").replaceAll("\\.", "");
                            if (!namedProbabilities.containsKey(name)) {
                                namedProbabilities.put(name, value);
                            }
                        }
                    });

                    PacketByteBuf responseBuf = PacketByteBufs.create();
                    PacketByteBuf.PacketWriter<String> namePacketWriter = PacketByteBuf::writeString;
                    PacketByteBuf.PacketWriter<Float> floatPacketWriter = PacketByteBuf::writeFloat;
                    responseBuf.writeMap(namedProbabilities, namePacketWriter, floatPacketWriter);

                    responseSender.sendPacket(SPAWN_MAP_PACKET_CLIENT, responseBuf);
                }
            }
        }));
    }

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(SPAWN_MAP_PACKET_CLIENT, ((client, handler, buf, responseSender) -> {
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
        }));
    }
}
