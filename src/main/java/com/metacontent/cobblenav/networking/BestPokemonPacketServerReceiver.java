package com.metacontent.cobblenav.networking;

import com.cobblemon.mod.common.api.abilities.PotentialAbility;
import com.cobblemon.mod.common.api.moves.BenchedMove;
import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.moves.MoveTemplate;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.abilities.HiddenAbility;
import com.metacontent.cobblenav.util.FoundPokemon;
import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class BestPokemonPacketServerReceiver {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        String name = buf.readString();
        ServerWorld world = player.getServerWorld();

        List<PokemonEntity> pokemonEntities = world.getEntitiesByClass(
                PokemonEntity.class,
                Box.of(
                    player.getPos(),
                    200,
                    100,
                    200
                ),
                (pokemonEntity -> pokemonEntity.getPokemon().isWild() && pokemonEntity.getPokemon().showdownId().equals(name))
        );

        PacketByteBuf responseBuf = PacketByteBufs.create();

        if (!pokemonEntities.isEmpty()) {
            Map<FoundPokemon, Integer> ratedFoundPokemonMap = new HashMap<>();

            pokemonEntities.forEach(pokemonEntity -> {
                Pokemon pokemon = pokemonEntity.getPokemon();
                AtomicInteger rating = new AtomicInteger(0);
                AtomicInteger potentialStarsAmount = new AtomicInteger(0);
                boolean hasHiddenAbility = false;
                String eggMoveName = "";
                pokemon.getIvs().forEach(entry -> {
                    int value = entry.getValue();
                    if (value == 31) {
                        potentialStarsAmount.getAndIncrement();
                        rating.getAndIncrement();
                    }
                });
                for (PotentialAbility potentialAbility : pokemon.getForm().getAbilities()) {
                    if (potentialAbility instanceof HiddenAbility && pokemon.getAbility().getTemplate() == potentialAbility.getTemplate()) {
                        rating.getAndIncrement();
                        hasHiddenAbility = true;
                        break;
                    }
                }

                List<MoveTemplate> eggMoves = pokemon.getForm().getMoves().getEggMoves();
                for (Move move : pokemon.getMoveSet()) {
                    if (eggMoves.contains(move.getTemplate())) {
                        rating.getAndIncrement();
                        eggMoveName = move.getName();
                        break;
                    }
                }
                if (eggMoveName.isEmpty()) {
                    for (BenchedMove benchedMove : pokemon.getBenchedMoves()) {
                        if (eggMoves.contains(benchedMove.getMoveTemplate())) {
                            rating.getAndIncrement();
                            eggMoveName = benchedMove.getMoveTemplate().getName();
                            break;
                        }
                    }
                }

                FoundPokemon foundPokemon = new FoundPokemon(pokemonEntity.getId(), pokemonEntity.getBlockPos(), pokemon.getLevel(),
                        potentialStarsAmount.get(), pokemon.getAbility().getName(), eggMoveName, hasHiddenAbility);

                ratedFoundPokemonMap.put(foundPokemon, rating.get());
            });

            Map.Entry<FoundPokemon, Integer> entry = ratedFoundPokemonMap.entrySet().stream()
                    .max(Map.Entry.comparingByValue()).orElse(null);
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
        responseSender.sendPacket(CobblenavPackets.BEST_POKEMON_PACKET_CLIENT, responseBuf);
    }
}
