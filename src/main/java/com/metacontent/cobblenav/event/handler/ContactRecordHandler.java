package com.metacontent.cobblenav.event.handler;

import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.api.events.battles.BattleVictoryEvent;
import com.metacontent.cobblenav.Cobblenav;
import com.metacontent.cobblenav.config.util.Badge;
import com.metacontent.cobblenav.mixin.TrainerBattleListenerAccessor;
import com.metacontent.cobblenav.store.AdditionalStatsData;
import com.metacontent.cobblenav.store.ContactData;
import com.metacontent.cobblenav.util.GrantedBadge;
import com.selfdot.cobblemontrainers.trainer.Trainer;
import com.selfdot.cobblemontrainers.trainer.TrainerBattleListener;
import kotlin.Unit;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ContactRecordHandler {
    public static Unit handleBattleVictory(BattleVictoryEvent event) {
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

    public static Unit handleTrainerBattleVictory(BattleVictoryEvent event) {
        PokemonBattle battle = event.getBattle();
        Map<PokemonBattle, Trainer> trainerBattles = ((TrainerBattleListenerAccessor) TrainerBattleListener.getInstance()).getOnBattleVictory();
        if (trainerBattles.containsKey(battle)) {
            Trainer trainer = trainerBattles.get(battle);
            battle.getPlayers().forEach(player -> {
                boolean isWinner = event.getWinners().contains(battle.getActor(player));
                ContactData.executeForDataOf(player, contactData -> contactData.updateContact(trainer, isWinner));
                if (isWinner) {
                    String trainerName = trainer.getName();
                    List<GrantedBadge> badges = Cobblenav.CONFIG.badges.getByTrainer(trainerName)
                            .stream().map(badge -> new GrantedBadge(badge.type(), trainerName, new Date())).toList();
                    if (!badges.isEmpty()) {
                        AdditionalStatsData.executeForDataOf(player, statsData -> badges.forEach(statsData::addBadge));
                    }
                }
                player.sendMessage(Text.translatable("message.cobblenav.updating_contacts")
                        .setStyle(Style.EMPTY.withItalic(true).withColor(0xff9a38)));
            });
        }
        return Unit.INSTANCE;
    }
}
