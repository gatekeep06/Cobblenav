package com.metacontent.cobblenav.store;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.player.PlayerData;
import com.cobblemon.mod.common.api.storage.player.PlayerDataExtension;
import com.cobblemon.mod.common.api.storage.player.PlayerDataExtensionRegistry;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class AdditionalStatsData implements PlayerDataExtension {
    public static final String NAME = "cobblenavPlayerStatsData";

    private int totalPvpCount = 0;
    private Date startDate;
    private final List<String> gymBadges = new ArrayList<>();
    private final Map<String, Integer> pokemonUsage = new HashMap<>();

    public int getTotalPvpCount() {
        return totalPvpCount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public List<String> getGymBadges() {
        return gymBadges;
    }

    public Map<String, Integer> getPokemonUsage() {
        return pokemonUsage;
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

        startDate = new Date(jsonObject.getAsJsonPrimitive("startDate").getAsLong());

        JsonArray gymBadgesArray = jsonObject.getAsJsonArray("gymBadges");
        gymBadges.clear();
        gymBadgesArray.forEach(jsonElement -> gymBadges.add(jsonElement.getAsString()));

        JsonObject pokemonUsageObject = jsonObject.getAsJsonObject("pokemonUsage");
        pokemonUsage.clear();
        pokemonUsageObject.entrySet().forEach(entry -> pokemonUsage.put(entry.getKey(), entry.getValue().getAsInt()));

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

        jsonObject.addProperty("startDate", startDate.getTime());

        JsonArray gymBadgesArray = new JsonArray();
        gymBadges.forEach(gymBadgesArray::add);
        jsonObject.add("gymBadges", gymBadgesArray);

        JsonObject typeUsageObject = new JsonObject();
        pokemonUsage.forEach(typeUsageObject::addProperty);
        jsonObject.add("pokemonUsage", typeUsageObject);

        return jsonObject;
    }

    public static void register() {
        PlayerDataExtensionRegistry.INSTANCE.register(NAME, AdditionalStatsData.class, false);
    }
}