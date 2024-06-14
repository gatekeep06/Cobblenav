package com.metacontent.cobblenav;

import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;

public class IntegrationManager {
    private final boolean trainers;
    private final boolean counter;
    private final boolean cobbledex;

    public IntegrationManager() {
        trainers = FabricLoader.getInstance().isModLoaded("cobblemontrainers") && Cobblenav.CONFIG.useCobblemonTrainersIntegration;
        log(trainers, Cobblenav.CONFIG.useCobblemonTrainersIntegration, "CobblemonTrainers");
        counter = FabricLoader.getInstance().isModLoaded("cobblemon_counter") && Cobblenav.CONFIG.useCounterIntegration;
        log(counter, Cobblenav.CONFIG.useCounterIntegration, "Cobblemon Counter");
        cobbledex = FabricLoader.getInstance().isModLoaded("cobbledex") && Cobblenav.CONFIG.useCobbledexIntegration;
        log(cobbledex, Cobblenav.CONFIG.useCobbledexIntegration, "Cobbledex");
    }

    private void log(boolean integration, boolean configSetting, String modName) {
        if (integration) {
            Cobblenav.LOGGER.info(modName + " Integration is enabled");
        }
        else if (configSetting) {
            Cobblenav.LOGGER.warn(modName + " is not installed, integration will not be used");
        }
    }

    public boolean trainers() {
        return trainers;
    }

    public boolean counter() {
        return counter;
    }

    public boolean cobbledex() {
        return cobbledex;
    }
}
