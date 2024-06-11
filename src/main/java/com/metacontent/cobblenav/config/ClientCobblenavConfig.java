package com.metacontent.cobblenav.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.metacontent.cobblenav.Cobblenav;
import com.metacontent.cobblenav.config.util.MainScreenWidgetType;
import com.metacontent.cobblenav.config.util.PercentageDisplayType;
import com.metacontent.cobblenav.config.util.PokemonFinderType;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Map;

public class ClientCobblenavConfig {
    public final float screenScale;
    public final PokemonFinderType pokenavFinderType;
    public final PokemonFinderType pokefinderFinderType;
    public final MainScreenWidgetType mainScreenWidget;
    public final PercentageDisplayType percentageDisplayType;
    public final boolean bucketWisePercentageCalculation;
    public final int reverseSortingButtonCooldown;
    public final int actionButtonAreaExpansion;
    public final int scrollSize;
    public final boolean showButtonTooltips;
    public final boolean notifyIfPokemonIsNotFound;
    public final int trackArrowVerticalOffset;
    public final Map<String, Double> partyWidgetAdjustments;

    private ClientCobblenavConfig(
            float screenScale,
            PokemonFinderType pokemonFinderType,
            PokemonFinderType pokefinderFinderType,
            MainScreenWidgetType mainScreenWidget,
            PercentageDisplayType percentageDisplayType,
            boolean bucketWisePercentageCalculation,
            int reverseSortingButtonCooldown,
            int actionButtonAreaExpansion,
            int scrollSize,
            boolean showButtonTooltips,
            boolean notifyIfPokemonIsNotFound,
            int trackArrowVerticalOffset,
            Map<String, Double> partyWidgetAdjustments
    ) {
        this.screenScale = screenScale;
        this.pokenavFinderType = pokemonFinderType;
        this.pokefinderFinderType = pokefinderFinderType;
        this.mainScreenWidget = mainScreenWidget;
        this.percentageDisplayType = percentageDisplayType;
        this.bucketWisePercentageCalculation = bucketWisePercentageCalculation;
        this.reverseSortingButtonCooldown = reverseSortingButtonCooldown;
        this.actionButtonAreaExpansion = actionButtonAreaExpansion;
        this.scrollSize = scrollSize;
        this.showButtonTooltips = showButtonTooltips;
        this.notifyIfPokemonIsNotFound = notifyIfPokemonIsNotFound;
        this.trackArrowVerticalOffset = trackArrowVerticalOffset;
        this.partyWidgetAdjustments = partyWidgetAdjustments;
    }

    private ClientCobblenavConfig() {
        this(1f, PokemonFinderType.BEST, PokemonFinderType.BEST, MainScreenWidgetType.PARTY, PercentageDisplayType.PERCENT_ONLY, false, 100, 20, 20, false, true, 70, Map.of());
    }

    public static ClientCobblenavConfig init() {
        Cobblenav.LOGGER.info("Initializing client " + Cobblenav.ID + " config");
        ClientCobblenavConfig config;
        Gson gson = (new GsonBuilder()).disableHtmlEscaping().setPrettyPrinting().create();
        File file = new File(FabricLoader.getInstance().getConfigDir() + Cobblenav.CLIENT_CONFIG_PATH);
        file.getParentFile().mkdirs();
        if (file.exists()) {
            try (FileReader fileReader = new FileReader(file)) {
                config = gson.fromJson(fileReader, ClientCobblenavConfig.class);
            }
            catch (Throwable throwable) {
                Cobblenav.LOGGER.error(throwable.getMessage(), throwable);
                config = new ClientCobblenavConfig();
            }
        }
        else {
            config = new ClientCobblenavConfig();
        }

        try (PrintWriter printWriter = new PrintWriter(file)) {
            gson.toJson(config, printWriter);
        }
        catch (Throwable throwable) {
            Cobblenav.LOGGER.error(throwable.getMessage(), throwable);
        }

        return config;
    }
}
