package com.metacontent.cobblenav;

import com.metacontent.cobblenav.command.CobblenavCommands;
import com.metacontent.cobblenav.config.CobblenavConfig;
import com.metacontent.cobblenav.event.CobblenavEvents;
import com.metacontent.cobblenav.item.CobblenavItems;
import com.metacontent.cobblenav.networking.CobblenavPackets;
import com.metacontent.cobblenav.store.ContactData;
import com.metacontent.cobblenav.util.PokenavAreaContextResolver;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Cobblenav implements ModInitializer {
    public static final String ID = "cobblenav";
    public static final Logger LOGGER = LoggerFactory.getLogger(ID);
    public static final String CONFIG_PATH = "/" + ID + "/cobblenav-config.json";
    public static final String CLIENT_CONFIG_PATH = "/" + ID + "/client-cobblenav-config.json";
    public static final CobblenavConfig CONFIG = CobblenavConfig.init();

    public static final PokenavAreaContextResolver RESOLVER = new PokenavAreaContextResolver();

    @Override
    public void onInitialize() {
        ContactData.register();
        CobblenavItems.registerCobblenavItems();
        CobblenavPackets.registerC2SPackets();
        CobblenavCommands.registerCommands();
        CobblenavEvents.subscribeEvents();
    }
}
