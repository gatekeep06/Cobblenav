package com.metacontent.cobblenav.event.handler;

import com.cobblemon.mod.common.api.events.pokemon.TradeCompletedEvent;
import com.cobblemon.mod.common.api.events.storage.ReleasePokemonEvent;
import com.metacontent.cobblenav.store.AdditionalStatsData;
import kotlin.Unit;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.UUID;

public class PokemonUsageDeletionHandler {
    public static Unit handlePokemonRelease(ReleasePokemonEvent event) {
        UUID pokemonUuid = event.getPokemon().getUuid();
        ServerPlayerEntity player = event.getPlayer();
        AdditionalStatsData.executeForDataOf(player, statsData -> statsData.removePokemonUsage(pokemonUuid));
        return Unit.INSTANCE;
    }

    public static Unit handleTradeCompletion(TradeCompletedEvent event) {
        UUID firstPlayerUuid = event.getTradeParticipant1().getUuid();
        UUID firstPokemonUuid = event.getTradeParticipant2Pokemon().getUuid();
        AdditionalStatsData.executeForDataOf(firstPlayerUuid, statsData -> statsData.removePokemonUsage(firstPokemonUuid));

        UUID secondPlayerUuid = event.getTradeParticipant2().getUuid();
        UUID secondPokemonUuid = event.getTradeParticipant1Pokemon().getUuid();
        AdditionalStatsData.executeForDataOf(secondPlayerUuid, statsData -> statsData.removePokemonUsage(secondPokemonUuid));

        return Unit.INSTANCE;
    }
}
