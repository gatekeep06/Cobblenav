package com.metacontent.cobblenav.networking.server;

import com.cobblemon.mod.common.Cobblemon;
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
import com.metacontent.cobblenav.Cobblenav;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import us.timinc.mc.cobblemon.counter.api.EncounterApi;

import java.util.*;

import static com.metacontent.cobblenav.networking.CobblenavPackets.SPAWN_MAP_PACKET;

public class SpawnMapRequestReceiver {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        CobblemonConfig config = Cobblemon.config;
        String bucketName = buf.readString();
        Map<RenderablePokemon, Float> spawnMap = new HashMap<>();

        server.executeSync(() -> {
            try {
                if (config.getEnableSpawning()) {
                    PlayerSpawner spawner = CobblemonWorldSpawnerManager.INSTANCE.getSpawnersForPlayers().get(player.getUuid());
                    SpawnBucket bucket = Cobblemon.INSTANCE.getBestSpawner().getConfig().getBuckets().stream()
                            .filter(b -> bucketName.equalsIgnoreCase(b.name)).findFirst().orElse(null);
                    if (spawner == null || bucket == null) {
                        throw new NullPointerException("For some reason player spawner and/or bucket are null");
                    }

                    SpawnCause cause = new SpawnCause(spawner, bucket, spawner.getCauseEntity());
                    WorldSlice slice = spawner.getProspector().prospect(spawner, new SpawningArea(cause, (ServerWorld) player.getWorld(),
                            (int) Math.ceil(player.getX() - config.getWorldSliceDiameter() / 2f),
                            (int) Math.ceil(player.getY() - config.getWorldSliceHeight() / 2f),
                            (int) Math.ceil(player.getZ() - config.getWorldSliceDiameter() / 2f),
                            Cobblenav.CONFIG.checkSpawnWidth == -1 ? config.getWorldSliceDiameter() : Cobblenav.CONFIG.checkSpawnWidth,
                            Cobblenav.CONFIG.checkSpawnHeight == -1 ? config.getWorldSliceHeight() : Cobblenav.CONFIG.checkSpawnHeight,
                            Cobblenav.CONFIG.checkSpawnWidth == -1 ? config.getWorldSliceDiameter() : Cobblenav.CONFIG.checkSpawnWidth));

                    List<AreaSpawningContext> contexts = Cobblenav.RESOLVER.resolve(spawner, spawner.getContextCalculators(), slice);
                    Map<SpawnDetail, Float> spawnProbabilities = spawner.getSpawningSelector().getProbabilities(spawner, contexts);

                    spawnProbabilities.forEach((key, value) -> {
                        try {
                            if (key instanceof PokemonSpawnDetail pokemonSpawnDetail && pokemonSpawnDetail.isValid()) {
                                RenderablePokemon renderablePokemon = pokemonSpawnDetail.getPokemon().asRenderablePokemon();
                                boolean isIgnored = Cobblenav.CONFIG.ignoredLabels.stream().anyMatch(
                                        string -> renderablePokemon.getSpecies().getLabels().contains(string)
                                );
                                boolean isHidden = Cobblenav.CONFIG.hiddenPokemon.stream().anyMatch(
                                        string -> renderablePokemon.getForm().showdownId().equals(string)
                                );
                                boolean isSeen = true;
                                if (Cobblenav.CONFIG.useCounterIntegration && Cobblenav.CONFIG.onlySeenPokemonMode) {
                                    isSeen = EncounterApi.INSTANCE.check(player, renderablePokemon.getSpecies().getName().toLowerCase());
                                }

                                if (isIgnored || isHidden) {
                                    return;
                                }
                                if (isSeen) {
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
            catch (Throwable e) {
                Cobblenav.LOGGER.error(e.getMessage(), e);
            }
            finally {
                PacketByteBuf responseBuf = PacketByteBufs.create();
                responseBuf.writeString(bucketName);
                PacketByteBuf.PacketWriter<RenderablePokemon> renderablePokemonPacketWriter = (packetByteBuf, pokemon) -> pokemon.saveToBuffer(packetByteBuf);
                PacketByteBuf.PacketWriter<Float> floatPacketWriter = PacketByteBuf::writeFloat;
                responseBuf.writeMap(spawnMap, renderablePokemonPacketWriter, floatPacketWriter);

                responseSender.sendPacket(SPAWN_MAP_PACKET, responseBuf);
            }
        });
    }
}
