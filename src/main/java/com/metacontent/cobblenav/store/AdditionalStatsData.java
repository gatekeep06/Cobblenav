package com.metacontent.cobblenav.store;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.player.PlayerData;
import com.cobblemon.mod.common.api.storage.player.PlayerDataExtension;
import com.cobblemon.mod.common.api.storage.player.PlayerDataExtensionRegistry;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

public class AdditionalStatsData implements PlayerDataExtension {
    public static final String NAME = "cobblenavPlayerStatsData";

    private int totalPvpCount = 0;
    private Date startDate = new Date();
    private final Set<String> gymBadges = new HashSet<>();
    private final Map<UUID, Integer> pokemonUsage = new HashMap<>();

    public int getTotalPvpCount() {
        return totalPvpCount;
    }

    public void updateTotalPvpCount() {
        totalPvpCount++;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Set<String> getGymBadges() {
        return gymBadges;
    }

    public void addBadge(String badge) {
        gymBadges.add(badge);
    }

    public Map<UUID, Integer> getPokemonUsage() {
        return pokemonUsage;
    }

    public void updatePokemonUsage(UUID pokemonUuid) {
        Integer usage = pokemonUsage.get(pokemonUuid);
        if (usage == null) {
            usage = 0;
        }
        usage++;
        pokemonUsage.put(pokemonUuid, usage);
    }

    public void removePokemonUsage(UUID pokemonUuid) {
        pokemonUsage.remove(pokemonUuid);
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

    public static void executeForDataOf(ServerPlayerEntity player, Consumer<AdditionalStatsData> action) {
        PlayerData data = Cobblemon.playerData.get(player);
        AdditionalStatsData statsData = getFromData(data);
        action.accept(statsData);
        Cobblemon.playerData.saveSingle(data);
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
        pokemonUsageObject.entrySet().forEach(entry -> pokemonUsage.put(UUID.fromString(entry.getKey()), entry.getValue().getAsInt()));

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

        JsonObject pokemonUsageObject = new JsonObject();
        pokemonUsage.forEach((uuid, integer) -> pokemonUsageObject.addProperty(uuid.toString(), integer));
        jsonObject.add("pokemonUsage", pokemonUsageObject);

        return jsonObject;
    }

    public static void register() {
        PlayerDataExtensionRegistry.INSTANCE.register(NAME, AdditionalStatsData.class, false);
    }
}
