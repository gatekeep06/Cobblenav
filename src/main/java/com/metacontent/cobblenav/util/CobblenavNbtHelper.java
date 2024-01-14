package com.metacontent.cobblenav.util;

import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class CobblenavNbtHelper {
    private CobblenavNbtHelper() {

    }

    public static void updateContact(ServerPlayerEntity player, ServerPlayerEntity contact, PokemonBattle battle, boolean isWinner) {
        BattleActor actor = battle.getActor(contact);
        if (actor != null && player instanceof ContactSaverEntity contactSaverEntity) {
            NbtCompound cobblenavNbt = contactSaverEntity.cobblenav$getContactData();
            NbtCompound contactNbt = cobblenavNbt.getCompound(contact.getUuidAsString());
            player.sendMessage(Text.literal(cobblenavNbt.toString()));
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
