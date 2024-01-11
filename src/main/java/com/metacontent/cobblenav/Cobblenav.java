package com.metacontent.cobblenav;

import com.metacontent.cobblenav.item.CobblenavItems;
import com.metacontent.cobblenav.networking.CobblenavPackets;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Cobblenav implements ModInitializer {
    public static final String ID = "cobblenav";
    public static final Logger LOGGER = LoggerFactory.getLogger(ID);

    @Override
    public void onInitialize() {
        CobblenavItems.registerCobblenavItems();
        CobblenavPackets.registerC2SPackets();
    }
}
