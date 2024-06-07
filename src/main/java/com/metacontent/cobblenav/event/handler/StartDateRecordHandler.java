package com.metacontent.cobblenav.event.handler;

import com.cobblemon.mod.common.api.events.starter.StarterChosenEvent;
import com.metacontent.cobblenav.store.AdditionalStatsData;
import kotlin.Unit;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Date;

public class StartDateRecordHandler {
    public static Unit handleStarterChoice(StarterChosenEvent event) {
        ServerPlayerEntity player = event.getPlayer();
        AdditionalStatsData.executeForDataOf(player, statsData -> statsData.setStartDate(new Date()));
        return Unit.INSTANCE;
    }
}
