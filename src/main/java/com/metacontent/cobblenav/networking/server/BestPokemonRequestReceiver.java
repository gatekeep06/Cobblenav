package com.metacontent.cobblenav.networking.server;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.metacontent.cobblenav.networking.CobblenavPackets;
import com.metacontent.cobblenav.util.BestPokemonFinder;
import com.metacontent.cobblenav.util.FoundPokemon;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.*;

public class BestPokemonRequestReceiver {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        PacketByteBuf responseBuf = PacketByteBufs.create();
        String name = buf.readString();
        ServerWorld world = player.getServerWorld();

        BestPokemonFinder finder = new BestPokemonFinder(player, world);
        List<PokemonEntity> pokemonEntities = finder.find(name);

        if (!pokemonEntities.isEmpty()) {
            Map.Entry<FoundPokemon, Float> entry = BestPokemonFinder.selectBest(pokemonEntities);

            if (entry != null) {
                responseBuf.writeBoolean(true);
                FoundPokemon bestFoundPokemon = entry.getKey();
                bestFoundPokemon.saveToBuf(responseBuf);
            }
            else {
                responseBuf.writeBoolean(false);
            }
        }
        else {
            responseBuf.writeBoolean(false);
        }
        responseSender.sendPacket(CobblenavPackets.BEST_POKEMON_PACKET, responseBuf);
    }
}
