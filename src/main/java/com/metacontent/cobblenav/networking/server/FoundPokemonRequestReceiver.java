package com.metacontent.cobblenav.networking.server;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.metacontent.cobblenav.config.util.PokemonFinderType;
import com.metacontent.cobblenav.networking.CobblenavPackets;
import com.metacontent.cobblenav.util.finder.BestPokemonFinder;
import com.metacontent.cobblenav.util.FoundPokemon;
import com.metacontent.cobblenav.util.finder.PokemonFinder;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.*;

public class FoundPokemonRequestReceiver {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        PacketByteBuf responseBuf = PacketByteBufs.create();
        PokemonFinderType finderType = buf.readEnumConstant(PokemonFinderType.class);
        String name = buf.readString();

        server.execute(() -> {
            ServerWorld world = player.getServerWorld();

            PokemonFinder finder = PokemonFinder.get(finderType, player, world);
            List<PokemonEntity> pokemonEntities = finder.find(name);

            if (!pokemonEntities.isEmpty()) {
                FoundPokemon pokemon = finder.select(pokemonEntities);
                if (pokemon != null) {
                    responseBuf.writeBoolean(true);
                    pokemon.saveToBuf(responseBuf);
                }
                else {
                    responseBuf.writeBoolean(false);
                }
            }
            else {
                responseBuf.writeBoolean(false);
            }
            responseSender.sendPacket(CobblenavPackets.FOUND_POKEMON_PACKET, responseBuf);
        });
    }
}
