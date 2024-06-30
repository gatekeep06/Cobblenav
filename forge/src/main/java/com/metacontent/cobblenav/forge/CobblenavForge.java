package com.metacontent.cobblenav.forge;

import dev.architectury.platform.forge.EventBuses;
import com.metacontent.cobblenav.Cobblenav;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Cobblenav.ID)
public class CobblenavForge {
    public CobblenavForge() {
		// Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(Cobblenav.ID, FMLJavaModLoadingContext.get().getModEventBus());
        Cobblenav.init();
    }
}