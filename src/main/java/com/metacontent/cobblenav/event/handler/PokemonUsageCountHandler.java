package com.metacontent.cobblenav.event.handler;

import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.api.events.battles.BattleStartedPostEvent;
import com.metacontent.cobblenav.store.AdditionalStatsData;
import kotlin.Unit;

public class PokemonUsageCountHandler {
    public static Unit handleBattleStart(BattleStartedPostEvent event) {
        PokemonBattle battle = event.getBattle();
        battle.getPlayers().forEach(player -> {
            BattleActor actor = battle.getActor(player);
            if (actor != null) {
                AdditionalStatsData.executeForDataOf(player, statsData -> actor.getPokemonList().forEach(battlePokemon -> statsData.updatePokemonUsage(battlePokemon.getOriginalPokemon().getUuid())));
            }
        });
        return Unit.INSTANCE;
    }
}
