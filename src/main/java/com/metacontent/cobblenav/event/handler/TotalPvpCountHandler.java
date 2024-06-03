package com.metacontent.cobblenav.event.handler;

import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.api.events.battles.BattleStartedPostEvent;
import com.metacontent.cobblenav.store.AdditionalStatsData;
import kotlin.Unit;

public class TotalPvpCountHandler {
    public static Unit handleBattleStart(BattleStartedPostEvent event) {
        PokemonBattle battle = event.getBattle();
        if (battle.isPvP()) {
            battle.getPlayers().forEach(player -> AdditionalStatsData.executeForDataOf(player, AdditionalStatsData::updateTotalPvpCount));
        }
        return Unit.INSTANCE;
    }
}
