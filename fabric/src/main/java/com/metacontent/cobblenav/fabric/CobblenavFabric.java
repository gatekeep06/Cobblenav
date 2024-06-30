package com.metacontent.cobblenav.fabric;

import com.metacontent.cobblenav.Cobblenav;
import net.fabricmc.api.ModInitializer;

public class CobblenavFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Cobblenav.init();
    }
}