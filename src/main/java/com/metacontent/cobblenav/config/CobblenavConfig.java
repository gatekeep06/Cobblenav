package com.metacontent.cobblenav.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.metacontent.cobblenav.Cobblenav;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.List;

public class CobblenavConfig {
    public final int mainScreenWidget;
    public final int checkSpawnWidth;
    public final int checkSpawnHeight;
    public final int findingAreaWidth;
    public final int findingAreaHeight;
    public final List<String> ignoredLabels;
    public final int titleCommandsPermissionLevel;
    public final List<Integer> customPokenavPredicates;

    private CobblenavConfig(
            int mainScreenWidget,
            int checkSpawnWidth,
            int checkSpawnHeight,
            int findingAreaWidth,
            int findingAreaHeight,
            List<String> ignoredLabels,
            int titleCommandsPermissionLevel,
            List<Integer> customPokenavPredicates
    ) {
        this.mainScreenWidget = mainScreenWidget;
        this.checkSpawnWidth = checkSpawnWidth;
        this.checkSpawnHeight = checkSpawnHeight;
        this.findingAreaWidth = findingAreaWidth;
        this.findingAreaHeight = findingAreaHeight;
        this.ignoredLabels = ignoredLabels;
        this.titleCommandsPermissionLevel = titleCommandsPermissionLevel;
        this.customPokenavPredicates = customPokenavPredicates;
    }

    private CobblenavConfig() {
        this(1, -1, -1, 100, 100, List.of("not_modeled"), 2, List.of(0));
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
}
