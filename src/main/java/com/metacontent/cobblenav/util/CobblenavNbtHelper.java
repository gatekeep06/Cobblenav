package com.metacontent.cobblenav.util;

import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CobblenavNbtHelper {
    private CobblenavNbtHelper() {

    }

    public static PokenavContact toPokenavContact(NbtCompound nbt) {
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

        return new PokenavContact(name, title, winnings, losses, team);
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
}
