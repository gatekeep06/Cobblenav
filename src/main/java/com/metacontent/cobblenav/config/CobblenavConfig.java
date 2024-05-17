package com.metacontent.cobblenav.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.metacontent.cobblenav.Cobblenav;
import com.metacontent.cobblenav.util.PokemonFeatureWeights;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.List;

public class CobblenavConfig {
    public final boolean useCounterIntegration;
    public final boolean useCobblemonTrainersIntegration;
    public final int checkSpawnWidth;
    public final int checkSpawnHeight;
    public final int findingAreaWidth;
    public final int findingAreaHeight;
    public final PokemonFeatureWeights pokemonFeatureWeights;
    public final List<String> ignoredLabels;
    public final List<String> hiddenPokemon;
    public final int titleCommandsPermissionLevel;
    public final List<Integer> customPokenavPredicates;

    private CobblenavConfig(
            boolean useCounterIntegration,
            boolean useCobblemonTrainersIntegration,
            int checkSpawnWidth,
            int checkSpawnHeight,
            int findingAreaWidth,
            int findingAreaHeight,
            PokemonFeatureWeights pokemonFeatureWeights,
            List<String> ignoredLabels,
            List<String> hiddenPokemon,
            int titleCommandsPermissionLevel,
            List<Integer> customPokenavPredicates
    ) {
        this.useCounterIntegration = useCounterIntegration;
        this.useCobblemonTrainersIntegration = useCobblemonTrainersIntegration;
        this.checkSpawnWidth = checkSpawnWidth;
        this.checkSpawnHeight = checkSpawnHeight;
        this.findingAreaWidth = findingAreaWidth;
        this.findingAreaHeight = findingAreaHeight;
        this.pokemonFeatureWeights = pokemonFeatureWeights;
        this.ignoredLabels = ignoredLabels;
        this.hiddenPokemon = hiddenPokemon;
        this.titleCommandsPermissionLevel = titleCommandsPermissionLevel;
        this.customPokenavPredicates = customPokenavPredicates;
    }

    private CobblenavConfig() {
        this(false, false, -1, -1, 100, 100, PokemonFeatureWeights.BASE_WEIGHTS, List.of("not_modeled"), List.of(), 2, List.of(0));
    }

    public static CobblenavConfig init() {
        Cobblenav.LOGGER.info("Initializing " + Cobblenav.ID + " config");
        CobblenavConfig config;
        Gson gson = (new GsonBuilder()).disableHtmlEscaping().setPrettyPrinting().create();
        File file = new File(FabricLoader.getInstance().getConfigDir() + Cobblenav.CONFIG_PATH);
        file.getParentFile().mkdirs();
        if (file.exists()) {
            try (FileReader fileReader = new FileReader(file)) {
                config = gson.fromJson(fileReader, CobblenavConfig.class);
            }
            catch (Throwable throwable) {
                Cobblenav.LOGGER.error(throwable.getMessage(), throwable);
                config = new CobblenavConfig();
            }
        }
        else {
            config = new CobblenavConfig();
        }

        try (PrintWriter printWriter = new PrintWriter(file)) {
            gson.toJson(config, printWriter);
        }
        catch (Throwable throwable) {
            Cobblenav.LOGGER.error(throwable.getMessage(), throwable);
        }

        return config;
    }

    @Override
    public String toString() {
        return "CobblenavConfig{" +
                "\nuseCobblemonTrainersIntegration=" + useCobblemonTrainersIntegration +
                ", \ncheckSpawnWidth=" + checkSpawnWidth +
                ", \ncheckSpawnHeight=" + checkSpawnHeight +
                ", \nfindingAreaWidth=" + findingAreaWidth +
                ", \nfindingAreaHeight=" + findingAreaHeight +
                ", \npokemonFeatureWeights=" + pokemonFeatureWeights +
                ", \nignoredLabels=" + ignoredLabels +
                ", \nhiddenPokemon=" + hiddenPokemon +
                ", \ntitleCommandsPermissionLevel=" + titleCommandsPermissionLevel +
                ", \ncustomPokenavPredicates=" + customPokenavPredicates +
                "\n}";
    }
}
