package com.metacontent.cobblenav.event;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.metacontent.cobblenav.Cobblenav;
import com.metacontent.cobblenav.event.handler.*;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.loader.api.FabricLoader;

public class CobblenavEvents {
    public static void subscribeEvents() {
        CobblemonEvents.BATTLE_VICTORY.subscribe(Priority.NORMAL, ContactRecordHandler::handleBattleVictory);
        CobblemonEvents.BATTLE_STARTED_POST.subscribe(Priority.NORMAL, TotalPvpCountHandler::handleBattleStart);
        CobblemonEvents.BATTLE_STARTED_POST.subscribe(Priority.NORMAL, PokemonUsageCountHandler::handleBattleStart);
        CobblemonEvents.STARTER_CHOSEN.subscribe(Priority.NORMAL, StartDateRecordHandler::handleStarterChoice);
        CobblemonEvents.POKEMON_RELEASED_EVENT_POST.subscribe(Priority.NORMAL, PokemonUsageDeletionHandler::handlePokemonRelease);
        CobblemonEvents.TRADE_COMPLETED.subscribe(Priority.NORMAL, PokemonUsageDeletionHandler::handleTradeCompletion);
        AttackEntityCallback.EVENT.register(SecretPokenavEventHandler::handleAttack);

        if (FabricLoader.getInstance().isModLoaded("cobblemontrainers") && Cobblenav.CONFIG.useCobblemonTrainersIntegration) {
            Cobblenav.LOGGER.info("CobblemonTrainers Integration is enabled");
            CobblemonEvents.BATTLE_VICTORY.subscribe(Priority.NORMAL, ContactRecordHandler::handleTrainerBattleVictory);
        }
        else if (!FabricLoader.getInstance().isModLoaded("cobblemontrainers") && Cobblenav.CONFIG.useCobblemonTrainersIntegration) {
            Cobblenav.LOGGER.warn("CobblemonTrainers is not installed, integration will not be used");
        }

        if (FabricLoader.getInstance().isModLoaded("cobblemon_counter") && Cobblenav.CONFIG.useCounterIntegration) {
            Cobblenav.LOGGER.info("Cobblemon Counter Integration is enabled");
            CobblemonEvents.POKEMON_ENTITY_SPAWN.subscribe(Priority.NORMAL, EggMoveHandler::possiblyGiveEggMove);
        }
        else if (!FabricLoader.getInstance().isModLoaded("cobblemon_counter") && Cobblenav.CONFIG.useCounterIntegration) {
            Cobblenav.LOGGER.warn("Cobblemon Counter is not installed, integration will not be used");
        }
    }
}
