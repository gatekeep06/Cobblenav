package com.metacontent.cobblenav.store;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.player.PlayerData;
import com.cobblemon.mod.common.api.storage.player.PlayerDataExtension;
import com.cobblemon.mod.common.api.storage.player.PlayerDataExtensionRegistry;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class AdditionalStatsData implements PlayerDataExtension {
    public static final String NAME = "cobblenavPlayerStatsData";

    private int totalPvpCount = 0;
    private final Map<String, Integer> pvpTypeUsageCounts = new HashMap<>(18);

    public int getTotalPvpCount() {
        return totalPvpCount;
    }

    public Map<String, Integer> getPvpTypeUsageCounts() {
        return pvpTypeUsageCounts;
    }

    public static AdditionalStatsData getFromData(PlayerData data) {
        AdditionalStatsData statsData = (AdditionalStatsData) data.getExtraData().get(AdditionalStatsData.NAME);
        if (statsData == null) {
            statsData = new AdditionalStatsData();
            data.getExtraData().put(AdditionalStatsData.NAME, statsData);
            Cobblemon.playerData.saveSingle(data);
        }
        return statsData;
    }

    @NotNull
    @Override
    public AdditionalStatsData deserialize(@NotNull JsonObject jsonObject) {
        totalPvpCount = jsonObject.getAsJsonPrimitive("totalPvpCount").getAsInt();

        JsonObject typeUsageObject = jsonObject.getAsJsonObject("pvpTypeUsageCounts");
        pvpTypeUsageCounts.clear();
        typeUsageObject.entrySet().forEach(entry -> pvpTypeUsageCounts.put(entry.getKey(), entry.getValue().getAsJsonPrimitive().getAsInt()));

        return this;
    }

    @NotNull
    @Override
    public String name() {
        return NAME;
    }

    @NotNull
    @Override
    public JsonObject serialize() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(PlayerDataExtension.Companion.getNAME_KEY(), NAME);

        jsonObject.addProperty("totalPvpCount", totalPvpCount);

        JsonObject typeUsageObject = new JsonObject();
        pvpTypeUsageCounts.forEach(typeUsageObject::addProperty);
        jsonObject.add("pvpTypeUsageCounts", typeUsageObject);

        return jsonObject;
    }

    public static void register() {
        PlayerDataExtensionRegistry.INSTANCE.register(NAME, AdditionalStatsData.class, false);
    }
}
