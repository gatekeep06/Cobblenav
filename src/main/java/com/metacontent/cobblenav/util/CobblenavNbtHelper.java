package com.metacontent.cobblenav.util;

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.RenderablePokemon;
import com.cobblemon.mod.common.pokemon.Species;
import com.google.gson.Gson;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.Nullable;

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
}
