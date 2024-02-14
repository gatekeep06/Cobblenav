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
import com.metacontent.cobblenav.Cobblenav;
import com.metacontent.cobblenav.config.CobblenavConfig;
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
        Map<RenderablePokemon, Float> spawnMap = new HashMap<>();

        server.executeSync(() -> {
            try {
                if (config.getEnableSpawning()) {
                    PlayerSpawner spawner = CobblemonWorldSpawnerManager.INSTANCE.getSpawnersForPlayers().get(player.getUuid());
                    SpawnBucket bucket = Cobblemon.INSTANCE.getBestSpawner().getConfig().getBuckets().stream()
                            .filter(b -> BUCKET_NAMES.get(bucketIndex).equalsIgnoreCase(b.name)).findFirst().orElse(null);
                    if (spawner != null && bucket != null) {
                        SpawnCause cause = new SpawnCause(spawner, bucket, player);
                        WorldSlice slice = spawner.getProspector().prospect(spawner, new SpawningArea(cause, (ServerWorld) player.getWorld(),
                                (int) Math.ceil(player.getX() - config.getWorldSliceDiameter() / 2f),
                                (int) Math.ceil(player.getY() - config.getWorldSliceHeight() / 2f),
                                (int) Math.ceil(player.getZ() - config.getWorldSliceDiameter() / 2f),
                                CobblenavConfig.CHECK_SPAWNS_WIDTH == -1 ? config.getWorldSliceDiameter() : CobblenavConfig.CHECK_SPAWNS_WIDTH,
                                CobblenavConfig.CHECK_SPAWNS_HEIGHT == -1 ? config.getWorldSliceHeight() : CobblenavConfig.CHECK_SPAWNS_HEIGHT,
                                CobblenavConfig.CHECK_SPAWNS_WIDTH == -1 ? config.getWorldSliceDiameter() : CobblenavConfig.CHECK_SPAWNS_WIDTH));

                        List<AreaSpawningContext> contexts = spawner.getResolver().resolve(spawner, spawner.getContextCalculators(), slice);
                        Map<SpawnDetail, Float> spawnProbabilities = spawner.getSpawningSelector().getProbabilities(spawner, contexts);

                        spawnProbabilities.forEach((key, value) -> {
                            try {
                                if (key instanceof PokemonSpawnDetail pokemonSpawnDetail && pokemonSpawnDetail.isValid()) {
                                    RenderablePokemon renderablePokemon = pokemonSpawnDetail.getPokemon().asRenderablePokemon();
                                    boolean isIgnored = Arrays.stream(CobblenavConfig.IGNORED_LABELS).anyMatch(
                                            string -> renderablePokemon.getSpecies().getLabels().contains(string)
                                    );
                                    if (!isIgnored) {
                                        spawnMap.put(renderablePokemon, value);
                                    }
                                }
                            }
                            catch (Throwable e) {
                                Cobblenav.LOGGER.error(e.getMessage(), e);
                            }
                        });
                    }
                }
            }
            catch (Throwable e) {
                Cobblenav.LOGGER.error(e.getMessage(), e);
            }
            finally {
                PacketByteBuf responseBuf = PacketByteBufs.create();
                responseBuf.writeInt(bucketIndex);
                PacketByteBuf.PacketWriter<RenderablePokemon> renderablePokemonPacketWriter = (packetByteBuf, pokemon) -> pokemon.saveToBuffer(packetByteBuf);
                PacketByteBuf.PacketWriter<Float> floatPacketWriter = PacketByteBuf::writeFloat;
                responseBuf.writeMap(spawnMap, renderablePokemonPacketWriter, floatPacketWriter);

                responseSender.sendPacket(SPAWN_MAP_PACKET_CLIENT, responseBuf);
            }
        });
    }
}
