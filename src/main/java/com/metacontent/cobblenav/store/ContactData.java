package com.metacontent.cobblenav.store;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.api.storage.player.PlayerData;
import com.cobblemon.mod.common.api.storage.player.PlayerDataExtension;
import com.cobblemon.mod.common.api.storage.player.PlayerDataExtensionRegistry;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.google.gson.*;
import com.metacontent.cobblenav.util.ContactTeamMember;
import com.metacontent.cobblenav.util.PokenavContact;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ContactData implements PlayerDataExtension {
    public static final String NAME = "cobblenavContactData";
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();

    private String title = "";
    private final Map<String, PokenavContact> contacts = new HashMap<>();

    public static void executeForDataOf(ServerPlayerEntity player, Consumer<ContactData> action) {
        PlayerData data = Cobblemon.playerData.get(player);
        ContactData contactData = (ContactData) data.getExtraData().getOrDefault(ContactData.NAME, null);
        if (contactData == null) {
            contactData = new ContactData();
            data.getExtraData().put(ContactData.NAME, contactData);
        }
        action.accept(contactData);
        Cobblemon.playerData.saveSingle(data);
    }

    public void updateContact(ServerPlayerEntity contact, @Nullable PokemonBattle battle, boolean isWinner, boolean isAlly) {
        PokenavContact pokenavContact = contacts.getOrDefault(contact.getUuidAsString(), new PokenavContact(contact.getUuidAsString(), contact.getEntityName()));
        pokenavContact.setName(contact.getEntityName());

        ContactData contactData = (ContactData) Cobblemon.playerData.get(contact).getExtraData().getOrDefault(NAME, null);
        if (contactData != null) {
            pokenavContact.setTitle(contactData.getTitle());
        }

        if (battle != null) {
            BattleActor actor = battle.getActor(contact);
            if (actor != null) {
                pokenavContact.getTeam().clear();
                for (BattlePokemon pokemon : actor.getPokemonList()) {
                    ContactTeamMember teamMember = new ContactTeamMember(pokemon.getOriginalPokemon().getSpecies().getTranslatedName().getString(), pokemon.getOriginalPokemon().getLevel());
                    pokenavContact.getTeam().add(teamMember);
                }
            }
        }

        if (!isAlly) {
            if (isWinner) {
                pokenavContact.updateWinnings();
            }
            else {
                pokenavContact.updateLosses();
            }
        }

        contacts.put(contact.getUuidAsString(), pokenavContact);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @NotNull
    @Override
    public ContactData deserialize(@NotNull JsonObject jsonObject) {
        JsonArray jsonArray = jsonObject.getAsJsonArray("contacts");
        if (!jsonArray.isJsonNull()) {
            for (JsonElement jsonElement: jsonArray) {
                PokenavContact contact = GSON.fromJson(jsonElement, PokenavContact.class);
                contacts.put(contact.getUuid(), contact);
            }
        }

        JsonPrimitive jsonPrimitive = jsonObject.getAsJsonPrimitive("title");
        title = jsonPrimitive.getAsString();

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
        JsonObject json = new JsonObject();
        json.addProperty(PlayerDataExtension.Companion.getNAME_KEY(), NAME);

        JsonArray jsonArray = new JsonArray();
        for (PokenavContact contact : contacts.values()) {
            jsonArray.add(GSON.toJsonTree(contact));
        }
        json.add("contacts", jsonArray);

        JsonPrimitive jsonPrimitive = new JsonPrimitive(title);
        json.add("title", jsonPrimitive);

        return json;
    }

    public static void register() {
        PlayerDataExtensionRegistry.INSTANCE.register(NAME, ContactData.class, false);
    }

    public String getTitle() {
        return title;
    }

    public Map<String, PokenavContact> getContacts() {
        return contacts;
    }
}
