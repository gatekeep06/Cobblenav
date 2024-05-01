package com.metacontent.cobblenav.event;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.events.battles.BattleVictoryEvent;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.metacontent.cobblenav.Cobblenav;
import com.metacontent.cobblenav.mixin.TrainerBattleListenerAccessor;
import com.metacontent.cobblenav.util.CobblenavNbtHelper;
import com.selfdot.cobblemontrainers.trainer.Trainer;
import com.selfdot.cobblemontrainers.trainer.TrainerBattleListener;
import kotlin.Unit;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Map;
import java.util.Objects;

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
                    boolean isAlly = false;
                    if (event.getWinners().size() > 1 && isWinner) {
                        isAlly = event.getWinners().contains(event.getBattle().getActor(player)) &&
                                event.getWinners().contains(event.getBattle().getActor(p));
                    }
                    else if (event.getLosers().size() > 1) {
                        isAlly = event.getLosers().contains(event.getBattle().getActor(player)) &&
                                event.getLosers().contains(event.getBattle().getActor(p));
                    }
                    CobblenavNbtHelper.updateContact(player, p, event.getBattle(), isWinner, isAlly);
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
                if (event.getWinners().contains(battle.getActor(player))) {
                    CobblenavNbtHelper.updateContact(player, trainer, true);
                }
                else if (event.getLosers().contains(battle.getActor(player))) {
                    CobblenavNbtHelper.updateContact(player, trainer, false);
                }
                player.sendMessage(Text.translatable("message.cobblenav.updating_contacts")
                        .setStyle(Style.EMPTY.withItalic(true).withColor(0xff9a38)));
            });
        }
        return Unit.INSTANCE;
    }

    public static void subscribeEvents() {
        CobblemonEvents.BATTLE_VICTORY.subscribe(Priority.NORMAL, CobblenavEvents::addPlayersToContacts);
        if (FabricLoader.getInstance().isModLoaded("cobblemontrainers") && Cobblenav.CONFIG.useCobblemonTrainersIntegration) {
            Cobblenav.LOGGER.info("CobblemonTrainers Integration is enabled");
            CobblemonEvents.BATTLE_VICTORY.subscribe(Priority.NORMAL, CobblenavEvents::addTrainerToContacts);
        }
        else if (!FabricLoader.getInstance().isModLoaded("cobblemontrainers") && Cobblenav.CONFIG.useCobblemonTrainersIntegration) {
            Cobblenav.LOGGER.warn("CobblemonTrainers is not installed, integration will not be used");
        }
    }
}
