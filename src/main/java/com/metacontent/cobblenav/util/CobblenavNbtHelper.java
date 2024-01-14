package com.metacontent.cobblenav.util;

import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

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
        List<String> team = new ArrayList<>();

        nbt.getList("team", 10).forEach(nbtElement -> {
            if (nbtElement instanceof NbtCompound nbtCompound) {
                team.add(nbtCompound.getString("pokemon"));
            }
        });

        return new PokenavContact(name, title, winnings, losses, team);
    }

    public static void updateContact(ServerPlayerEntity player, ServerPlayerEntity contact, PokemonBattle battle, boolean isWinner) {
        BattleActor actor = battle.getActor(contact);
        if (actor != null && player instanceof ContactSaverEntity contactSaverEntity && contact instanceof ContactSaverEntity contactSaverEntity1) {
            NbtCompound cobblenavNbt = contactSaverEntity.cobblenav$getContactData();
            NbtCompound contactNbt = cobblenavNbt.getCompound(contact.getUuidAsString());

            contactNbt.putString("name", contact.getEntityName());

            String title = contactSaverEntity1.cobblenav$getContactData().getString("title");
            contactNbt.putString("title", title);

            NbtList pokemonNbtList = new NbtList();
            for (BattlePokemon pokemon : actor.getPokemonList()) {
                NbtCompound pokemonNbt = new NbtCompound();
                pokemonNbt.putString("pokemon", pokemon.getOriginalPokemon().showdownId());
                pokemonNbtList.add(pokemonNbt);
            }
            contactNbt.put("team", pokemonNbtList);

            if (isWinner) {
                int winnings = contactNbt.getInt("winnings");
                player.sendMessage(Text.literal(String.valueOf(winnings)));
                winnings++;
                player.sendMessage(Text.literal(String.valueOf(winnings)));
                contactNbt.putInt("winnings", winnings);
            }
            else {
                int losses = contactNbt.getInt("losses");
                losses++;
                contactNbt.putInt("losses", losses);
            }

            cobblenavNbt.put(contact.getUuidAsString(), contactNbt);
        }
    }
}
