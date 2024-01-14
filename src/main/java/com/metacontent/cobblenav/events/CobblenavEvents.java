package com.metacontent.cobblenav.events;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.events.battles.BattleVictoryEvent;
import com.metacontent.cobblenav.util.CobblenavNbtHelper;
import com.metacontent.cobblenav.util.ContactSaverEntity;
import kotlin.Unit;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.List;

public class CobblenavEvents {
    private static Unit addPlayersToContacts(BattleVictoryEvent event) {
        List<ServerPlayerEntity> players = event.getBattle().getPlayers();
        if (players.size() > 0) {
            players.forEach(player -> {
                for (ServerPlayerEntity p : players) {
                    //TODO:обработка ситуации 2х2 или более
                    boolean isWinner = event.getWinners().contains(event.getBattle().getActor(player));
                    CobblenavNbtHelper.updateContact(player, p, event.getBattle(), isWinner);
                    player.sendMessage(Text.literal(((ContactSaverEntity) player).cobblenav$getContactData().toString()));
                }
                player.sendMessage(Text.translatable("message.cobblenav.updating_contacts")
                        .setStyle(Style.EMPTY.withItalic(true).withColor(0xff9a38)));
            });
        }
        return Unit.INSTANCE;
    }

    public static void subscribeEvents() {
        CobblemonEvents.BATTLE_VICTORY.subscribe(Priority.NORMAL, CobblenavEvents::addPlayersToContacts);
    }
}
