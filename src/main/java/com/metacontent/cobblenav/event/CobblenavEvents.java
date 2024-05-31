package com.metacontent.cobblenav.event;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.events.battles.BattleStartedPostEvent;
import com.cobblemon.mod.common.api.events.battles.BattleVictoryEvent;
import com.cobblemon.mod.common.api.events.pokemon.TradeCompletedEvent;
import com.cobblemon.mod.common.api.events.starter.StarterChosenEvent;
import com.cobblemon.mod.common.api.events.storage.ReleasePokemonEvent;
import com.metacontent.cobblenav.Cobblenav;
import com.metacontent.cobblenav.mixin.TrainerBattleListenerAccessor;
import com.metacontent.cobblenav.store.AdditionalStatsData;
import com.metacontent.cobblenav.store.ContactData;
import com.selfdot.cobblemontrainers.trainer.Trainer;
import com.selfdot.cobblemontrainers.trainer.TrainerBattleListener;
import kotlin.Unit;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.*;

public class CobblenavEvents {
    private static Unit addPlayersToContacts(BattleVictoryEvent event) {
        List<ServerPlayerEntity> players = event.getBattle().getPlayers();
        if (players.size() > 1) {
            players.forEach(player -> {
                for (ServerPlayerEntity p : players) {
                    if (Objects.equals(p, player)) {
                        continue;
                    }
                    boolean isWinner = event.getWinners().contains(event.getBattle().getActor(player));
                    boolean isAlly;
                    if (event.getWinners().size() > 1 && isWinner) {
                        isAlly = event.getWinners().contains(event.getBattle().getActor(player)) &&
                                event.getWinners().contains(event.getBattle().getActor(p));
                    }
                    else if (event.getLosers().size() > 1) {
                        isAlly = event.getLosers().contains(event.getBattle().getActor(player)) &&
                                event.getLosers().contains(event.getBattle().getActor(p));
                    }
                    else {
                        isAlly = false;
                    }
                    ContactData.executeForDataOf(player, contactData -> contactData.updateContact(p, event.getBattle(), isWinner, isAlly));
                }
                player.sendMessage(Text.translatable("message.cobblenav.updating_contacts")
                        .setStyle(Style.EMPTY.withItalic(true).withColor(0xff9a38)));
            });
        }
        return Unit.INSTANCE;
    }

    private static Unit addTrainerToContacts(BattleVictoryEvent event) {
        PokemonBattle battle = event.getBattle();
        Map<PokemonBattle, Trainer> trainerBattles = ((TrainerBattleListenerAccessor) TrainerBattleListener.getInstance()).getOnBattleVictory();
        if (trainerBattles.containsKey(battle)) {
            Trainer trainer = trainerBattles.get(battle);
            battle.getPlayers().forEach(player -> {
                boolean isWinner = event.getWinners().contains(battle.getActor(player));
                ContactData.executeForDataOf(player, contactData -> contactData.updateContact(trainer, isWinner));
                player.sendMessage(Text.translatable("message.cobblenav.updating_contacts")
                        .setStyle(Style.EMPTY.withItalic(true).withColor(0xff9a38)));
            });
        }
        return Unit.INSTANCE;
    }

    private static Unit updateTotalPvpCount(BattleStartedPostEvent event) {
        PokemonBattle battle = event.getBattle();
        if (battle.isPvP()) {
            battle.getPlayers().forEach(player -> AdditionalStatsData.executeForDataOf(player, AdditionalStatsData::updateTotalPvpCount));
        }
        return Unit.INSTANCE;
    }

    private static Unit updatePokemonUsage(BattleStartedPostEvent event) {
        PokemonBattle battle = event.getBattle();
        battle.getPlayers().forEach(player -> {
            BattleActor actor = battle.getActor(player);
            if (actor != null) {
                AdditionalStatsData.executeForDataOf(player, statsData -> actor.getPokemonList().forEach(battlePokemon -> statsData.updatePokemonUsage(battlePokemon.getOriginalPokemon().getUuid())));
            }
        });
        return Unit.INSTANCE;
    }

    private static Unit setStartDate(StarterChosenEvent event) {
        ServerPlayerEntity player = event.getPlayer();
        AdditionalStatsData.executeForDataOf(player, statsData -> statsData.setStartDate(new Date()));
        return Unit.INSTANCE;
    }

    private static Unit removePokemonUsage(ReleasePokemonEvent event) {
        UUID pokemonUuid = event.getPokemon().getUuid();
        ServerPlayerEntity player = event.getPlayer();
        AdditionalStatsData.executeForDataOf(player, statsData -> statsData.removePokemonUsage(pokemonUuid));
        return Unit.INSTANCE;
    }

    private static Unit removePokemonUsage(TradeCompletedEvent event) {
        UUID firstPlayerUuid = event.getTradeParticipant1().getUuid();
        UUID firstPokemonUuid = event.getTradeParticipant2Pokemon().getUuid();
        AdditionalStatsData.executeForDataOf(firstPlayerUuid, statsData -> statsData.removePokemonUsage(firstPokemonUuid));

        UUID secondPlayerUuid = event.getTradeParticipant2().getUuid();
        UUID secondPokemonUuid = event.getTradeParticipant1Pokemon().getUuid();
        AdditionalStatsData.executeForDataOf(secondPlayerUuid, statsData -> statsData.removePokemonUsage(secondPokemonUuid));

        return Unit.INSTANCE;
    }

    public static void subscribeEvents() {
        CobblemonEvents.BATTLE_VICTORY.subscribe(Priority.NORMAL, CobblenavEvents::addPlayersToContacts);
        CobblemonEvents.BATTLE_STARTED_POST.subscribe(Priority.NORMAL, CobblenavEvents::updateTotalPvpCount);
        CobblemonEvents.BATTLE_STARTED_POST.subscribe(Priority.NORMAL, CobblenavEvents::updatePokemonUsage);
        CobblemonEvents.STARTER_CHOSEN.subscribe(Priority.NORMAL, CobblenavEvents::setStartDate);
        CobblemonEvents.POKEMON_RELEASED_EVENT_POST.subscribe(Priority.NORMAL, CobblenavEvents::removePokemonUsage);
        CobblemonEvents.TRADE_COMPLETED.subscribe(Priority.NORMAL, CobblenavEvents::removePokemonUsage);
        if (FabricLoader.getInstance().isModLoaded("cobblemontrainers") && Cobblenav.CONFIG.useCobblemonTrainersIntegration) {
            Cobblenav.LOGGER.info("CobblemonTrainers Integration is enabled");
            CobblemonEvents.BATTLE_VICTORY.subscribe(Priority.NORMAL, CobblenavEvents::addTrainerToContacts);
        }
        else if (!FabricLoader.getInstance().isModLoaded("cobblemontrainers") && Cobblenav.CONFIG.useCobblemonTrainersIntegration) {
            Cobblenav.LOGGER.warn("CobblemonTrainers is not installed, integration will not be used");
        }
    }
}
