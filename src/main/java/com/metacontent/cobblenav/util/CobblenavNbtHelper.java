package com.metacontent.cobblenav.util;

import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.pokemon.FormData;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.RenderablePokemon;
import com.cobblemon.mod.common.pokemon.Species;
import com.google.gson.Gson;
import com.selfdot.cobblemontrainers.trainer.Trainer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class CobblenavNbtHelper {

    @Nullable
    public static RenderablePokemon getRenderablePokemonByNbtData(NbtCompound nbt) {
        if (nbt.contains("name")) {
            String name = nbt.getString("name");
            String formAspectsString = nbt.getString("form");
            Species species = PokemonSpecies.INSTANCE.getByName(name);
            if (species != null) {
                Pokemon pokemon = species.create(10);
                pokemon.setAspects(new HashSet<>(Arrays.stream((new Gson()).fromJson(formAspectsString, String[].class)).toList()));
                return pokemon.asRenderablePokemon();
            }
        }
        return null;
    }

    public static void saveRenderablePokemonData(RenderablePokemon pokemon, NbtCompound nbt) {
        String name = pokemon.getSpecies().showdownId();
        List<String> formName = pokemon.getForm().getAspects();
        nbt.putString("name", name);
        nbt.putString("form", formName.toString());
    }

    public static void clearRenderablePokemonData(NbtCompound nbt) {
        nbt.remove("name");
        nbt.remove("form");
    }

    public static PokenavContact toPokenavContact(NbtCompound nbt, String uuid) {
        String name = nbt.getString("name");
        String title = nbt.getString("title");
        int winnings = nbt.getInt("winnings");
        int losses = nbt.getInt("losses");
        List<ContactTeamMember> team = new ArrayList<>();

        nbt.getList("team", 10).forEach(nbtElement -> {
            if (nbtElement instanceof NbtCompound nbtCompound) {
                team.add(new ContactTeamMember(nbtCompound.getString("pokemon"), nbtCompound.getInt("level")));
            }
        });

        return new PokenavContact(uuid, name, title, winnings, losses, team);
    }

    public static void deleteContact(ServerPlayerEntity player, String uuid) {
        if (player instanceof ContactSaverEntity contactSaverEntity) {
            NbtCompound nbt = contactSaverEntity.cobblenav$getContactData();
            nbt.remove(uuid);
        }
    }

    public static void updateContact(ServerPlayerEntity player, ServerPlayerEntity contact, @Nullable PokemonBattle battle, boolean isWinner, boolean isAlly) {
        if (player instanceof ContactSaverEntity contactSaverEntity && contact instanceof ContactSaverEntity contactSaverEntity1) {
            NbtCompound cobblenavNbt = contactSaverEntity.cobblenav$getContactData();
            NbtCompound contactNbt = cobblenavNbt.getCompound(contact.getUuidAsString());

            contactNbt.putString("name", contact.getEntityName());

            String title = contactSaverEntity1.cobblenav$getContactData().getString("title");
            contactNbt.putString("title", title);

            if (battle != null) {
                BattleActor actor = battle.getActor(contact);
                if (actor != null) {
                    NbtList pokemonNbtList = new NbtList();
                    for (BattlePokemon pokemon : actor.getPokemonList()) {
                        NbtCompound pokemonNbt = new NbtCompound();
                        pokemonNbt.putString("pokemon", pokemon.getOriginalPokemon().showdownId());
                        pokemonNbt.putInt("level", pokemon.getOriginalPokemon().getLevel());
                        pokemonNbtList.add(pokemonNbt);
                    }
                    contactNbt.put("team", pokemonNbtList);
                }
            }

            if (!isAlly) {
                if (isWinner) {
                    int winnings = contactNbt.getInt("winnings");
                    winnings++;
                    contactNbt.putInt("winnings", winnings);
                } else {
                    int losses = contactNbt.getInt("losses");
                    losses++;
                    contactNbt.putInt("losses", losses);
                }
            }

            cobblenavNbt.put(contact.getUuidAsString(), contactNbt);
        }
    }

    public static void updateContact(ServerPlayerEntity player, Trainer contact, boolean isWinner) {
        if (player instanceof ContactSaverEntity contactSaverEntity) {
            String contactKey = contact.getGroup() + "." + contact.getName();
            NbtCompound cobblenavNbt = contactSaverEntity.cobblenav$getContactData();
            NbtCompound contactNbt = cobblenavNbt.getCompound(contactKey);

            contactNbt.putString("name", contact.getName());
            contactNbt.putString("title", "Trainer");

            NbtList pokemonNbtList = new NbtList();
            for (BattlePokemon pokemon : contact.getBattleTeam()) {
                NbtCompound pokemonNbt = new NbtCompound();
                pokemonNbt.putString("pokemon", pokemon.getOriginalPokemon().showdownId());
                pokemonNbt.putInt("level", pokemon.getOriginalPokemon().getLevel());
                pokemonNbtList.add(pokemonNbt);
            }
            contactNbt.put("team", pokemonNbtList);

            if (isWinner) {
                int winnings = contactNbt.getInt("winnings");
                winnings++;
                contactNbt.putInt("winnings", winnings);
            } else {
                int losses = contactNbt.getInt("losses");
                losses++;
                contactNbt.putInt("losses", losses);
            }

            cobblenavNbt.put(contactKey, contactNbt);
        }
    }
}
