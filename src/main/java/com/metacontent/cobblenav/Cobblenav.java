package com.metacontent.cobblenav;

import com.metacontent.cobblenav.command.CobblenavCommands;
import com.metacontent.cobblenav.config.CobblenavConfig;
import com.metacontent.cobblenav.events.CobblenavEvents;
import com.metacontent.cobblenav.item.CobblenavItems;
import com.metacontent.cobblenav.networking.CobblenavPackets;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Cobblenav implements ModInitializer {
    public static final String ID = "cobblenav";
    public static final Logger LOGGER = LoggerFactory.getLogger(ID);

    @Override
    public void onInitialize() {
        CobblenavConfig.initConfig();
        CobblenavItems.registerCobblenavItems();
        CobblenavPackets.registerC2SPackets();
        CobblenavCommands.registerCommands();
        CobblenavEvents.subscribeEvents();
    }
}
