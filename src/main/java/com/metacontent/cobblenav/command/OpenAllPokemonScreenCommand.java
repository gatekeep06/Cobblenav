package com.metacontent.cobblenav.command;

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.pokemon.RenderablePokemon;
import com.metacontent.cobblenav.networking.CobblenavPackets;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class OpenAllPokemonScreenCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("pokenav")
                .then(CommandManager.literal("open").requires(source -> source.hasPermissionLevel(2))
                        .then(CommandManager.literal("allPokemonScreen")
                                .executes(OpenAllPokemonScreenCommand::run))));
    }

    private static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
        List<RenderablePokemon> allPokemon = new ArrayList<>();
        PokemonSpecies.INSTANCE.getSpecies().forEach(species ->
                species.getForms().forEach(formData ->
                        allPokemon.add(new RenderablePokemon(species, new HashSet<>(formData.getAspects())))));
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeCollection(allPokemon, (buf1, pokemon) -> pokemon.saveToBuffer(buf1));
        ServerPlayNetworking.send(player, CobblenavPackets.ALL_POKEMON_PACKET, buf);
        return 1;
    }
}
