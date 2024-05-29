package com.metacontent.cobblenav.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.metacontent.cobblenav.Cobblenav;
import com.metacontent.cobblenav.config.util.Badge;
import com.metacontent.cobblenav.util.PokemonFeatureWeights;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.List;

public class CobblenavConfig {
    public final boolean useCounterIntegration;
    public final boolean useCobblemonTrainersIntegration;
    public final List<Badge> badges;
    public final int checkSpawnWidth;
    public final int checkSpawnHeight;
    public final int findingAreaWidth;
    public final int findingAreaHeight;
    public final PokemonFeatureWeights pokemonFeatureWeights;
    public final List<String> ignoredLabels;
    public final List<String> hiddenPokemon;
    public final int titleCommandsPermissionLevel;
    public final List<Integer> customPokenavPredicates;
    public final CounterIntegrationConfig counterIntegrationConfig;
    public final boolean enableDebugMode;

    private CobblenavConfig(
            boolean useCounterIntegration,
            boolean useCobblemonTrainersIntegration,
            List<Badge> badges,
            int checkSpawnWidth,
            int checkSpawnHeight,
            int findingAreaWidth,
            int findingAreaHeight,
            PokemonFeatureWeights pokemonFeatureWeights,
            List<String> ignoredLabels,
            List<String> hiddenPokemon,
            int titleCommandsPermissionLevel,
            List<Integer> customPokenavPredicates,
            CounterIntegrationConfig counterIntegrationConfig,
            boolean enableDebugMode
    ) {
        this.useCounterIntegration = useCounterIntegration;
        this.useCobblemonTrainersIntegration = useCobblemonTrainersIntegration;
        this.badges = badges;
        this.checkSpawnWidth = checkSpawnWidth;
        this.checkSpawnHeight = checkSpawnHeight;
        this.findingAreaWidth = findingAreaWidth;
        this.findingAreaHeight = findingAreaHeight;
        this.pokemonFeatureWeights = pokemonFeatureWeights;
        this.ignoredLabels = ignoredLabels;
        this.hiddenPokemon = hiddenPokemon;
        this.titleCommandsPermissionLevel = titleCommandsPermissionLevel;
        this.customPokenavPredicates = customPokenavPredicates;
        this.counterIntegrationConfig = counterIntegrationConfig;
        this.enableDebugMode = enableDebugMode;
    }

    private CobblenavConfig() {
        this(false, false, List.of(), -1, -1, 100, 100, PokemonFeatureWeights.BASE_WEIGHTS, List.of("not_modeled"), List.of(), 2, List.of(0), new CounterIntegrationConfig(), false);
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
                "useCounterIntegration=" + useCounterIntegration +
                ", useCobblemonTrainersIntegration=" + useCobblemonTrainersIntegration +
                ", badges=" + badges +
                ", checkSpawnWidth=" + checkSpawnWidth +
                ", checkSpawnHeight=" + checkSpawnHeight +
                ", findingAreaWidth=" + findingAreaWidth +
                ", findingAreaHeight=" + findingAreaHeight +
                ", pokemonFeatureWeights=" + pokemonFeatureWeights +
                ", ignoredLabels=" + ignoredLabels +
                ", hiddenPokemon=" + hiddenPokemon +
                ", titleCommandsPermissionLevel=" + titleCommandsPermissionLevel +
                ", customPokenavPredicates=" + customPokenavPredicates +
                ", counterIntegrationConfig=" + counterIntegrationConfig +
                ", enableDebugMode=" + enableDebugMode +
                '}';
    }

    public static class CounterIntegrationConfig {
        public final int levelOneStreak;
        public final int levelTwoStreak;
        public final int levelThreeStreak;
        public final int levelFourStreak;
        public final int levelZeroEggMoveChance;
        public final int levelOneEggMoveChance;
        public final int levelTwoEggMoveChance;
        public final int levelThreeEggMoveChance;
        public final int levelFourEggMoveChance;

        private CounterIntegrationConfig(
                int levelOneStreak,
                int levelTwoStreak,
                int levelThreeStreak,
                int levelFourStreak,
                int levelZeroEggMoveChance,
                int levelOneEggMoveChance,
                int levelTwoEggMoveChance,
                int levelThreeEggMoveChance,
                int levelFourEggMoveChance
        ) {
            this.levelOneStreak = levelOneStreak;
            this.levelTwoStreak = levelTwoStreak;
            this.levelThreeStreak = levelThreeStreak;
            this.levelFourStreak = levelFourStreak;
            this.levelZeroEggMoveChance = levelZeroEggMoveChance;
            this.levelOneEggMoveChance = levelOneEggMoveChance;
            this.levelTwoEggMoveChance = levelTwoEggMoveChance;
            this.levelThreeEggMoveChance = levelThreeEggMoveChance;
            this.levelFourEggMoveChance = levelFourEggMoveChance;
        }

        private CounterIntegrationConfig() {
            this(5, 10, 20, 30, 0, 21, 46, 58, 65);
        }

    }
}
