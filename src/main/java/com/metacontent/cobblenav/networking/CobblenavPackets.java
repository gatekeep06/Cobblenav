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
        ServerPlayNetworking.registerGlobalReceiver(SPAWN_MAP_PACKET_SERVER, SpawnMapPacketServerReceiver::receive);
    }

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(SPAWN_MAP_PACKET_CLIENT, SpawnMapPacketClientReceiver::receive);
    }
}
