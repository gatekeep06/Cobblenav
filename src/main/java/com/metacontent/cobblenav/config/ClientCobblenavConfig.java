package com.metacontent.cobblenav.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.metacontent.cobblenav.Cobblenav;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Map;

public class ClientCobblenavConfig {
    public final int trackArrowVerticalOffset;
    public final Map<String, Double> partyWidgetAdjustments;

    private ClientCobblenavConfig(int trackArrowVerticalOffset, Map<String, Double> partyWidgetAdjustments) {
        this.trackArrowVerticalOffset = trackArrowVerticalOffset;
        this.partyWidgetAdjustments = partyWidgetAdjustments;
    }

    private ClientCobblenavConfig() {
        this(70, Map.of());
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
