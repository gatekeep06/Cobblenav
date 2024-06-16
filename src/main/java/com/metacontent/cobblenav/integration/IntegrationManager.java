package com.metacontent.cobblenav.integration;

import com.metacontent.cobblenav.Cobblenav;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.Nullable;

public class IntegrationManager {
    private final boolean trainers;
    private final boolean counter;
    private final boolean cobbledex;

    @Nullable
    private final SeenPokemonChecker checker;

    public IntegrationManager() {
        trainers = FabricLoader.getInstance().isModLoaded("cobblemontrainers") && Cobblenav.CONFIG.useCobblemonTrainersIntegration;
        log(trainers, Cobblenav.CONFIG.useCobblemonTrainersIntegration, "CobblemonTrainers");
        counter = FabricLoader.getInstance().isModLoaded("cobblemon_counter") && Cobblenav.CONFIG.useCounterIntegration;
        log(counter, Cobblenav.CONFIG.useCounterIntegration, "Cobblemon Counter");
        cobbledex = FabricLoader.getInstance().isModLoaded("cobbledex") && Cobblenav.CONFIG.useCobbledexIntegration;
        log(cobbledex, Cobblenav.CONFIG.useCobbledexIntegration, "Cobbledex");

        checker = chooseChecker();
    }

    private void log(boolean integration, boolean configSetting, String modName) {
        if (integration) {
            Cobblenav.LOGGER.info(modName + " Integration is enabled");
        }
        else if (configSetting) {
            Cobblenav.LOGGER.warn(modName + " is not installed, integration will not be used");
        }
    }

    @Nullable
    private SeenPokemonChecker chooseChecker() {
        if (!Cobblenav.CONFIG.onlySeenPokemonMode) {
            return null;
        }
        if (FabricLoader.getInstance().isModLoaded("cobblemon_counter")) {
            Cobblenav.LOGGER.info("Cobblemon Counter is installed, it will be used to implement the only seen pokemon mode");
            return new CounterChecker();
        }
        if (FabricLoader.getInstance().isModLoaded("cobbledex")) {
            Cobblenav.LOGGER.info("Cobbledex is installed, it will be used to implement the only seen pokemon mode");
            return new CobbledexChecker();
        }
        Cobblenav.LOGGER.warn("Neither Cobblemon Counter nor Cobbledex are installed, only seen pokemon mode is disabled");
        return null;
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

    @Nullable
    public SeenPokemonChecker getChecker() {
        return checker;
    }
}
