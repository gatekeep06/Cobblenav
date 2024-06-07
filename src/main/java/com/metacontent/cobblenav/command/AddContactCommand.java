package com.metacontent.cobblenav.command;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.player.PlayerData;
import com.metacontent.cobblenav.Cobblenav;
import com.metacontent.cobblenav.store.ContactData;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;

public class AddContactCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("pokenav")
                .then(CommandManager.argument("player", EntityArgumentType.players()).requires(source -> source.hasPermissionLevel(2))
                        .then(CommandManager.literal("addContact")
                                .then(CommandManager.literal("byName")
                                        .then(CommandManager.argument("contact", StringArgumentType.string())
                                                .executes(AddContactCommand::runUsingName)))
                                .then(CommandManager.argument("contact", EntityArgumentType.player())
                                        .executes(AddContactCommand::runUsingPlayer)))));
    }

    private static int runUsingPlayer(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity contact = context.getArgument("contact", EntitySelector.class).getPlayer(context.getSource());
        if (contact != null) {
            List<ServerPlayerEntity> players = context.getArgument("player", EntitySelector.class).getPlayers(context.getSource());
            if (!players.isEmpty()) {
                players.forEach(p -> {
                    ContactData.executeForDataOf(p, contactData -> contactData.updateContact(contact, null, false, true));
                });
                return 1;
            }
            return -1;
        }
        return -1;
    }

    private static int runUsingName(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        try {
            String contact = context.getArgument("contact", String.class);
            List<ServerPlayerEntity> players = context.getArgument("player", EntitySelector.class).getPlayers(context.getSource());
            if (!players.isEmpty()) {
                players.forEach(p -> {
                    PlayerData playerData = Cobblemon.playerData.get(p);
                    ContactData contactData = ContactData.getFromData(playerData);
                    contactData.updateContact(new GameProfile(null, contact), playerData);
                });
                return 1;
            }
        }
        catch (Exception e) {
            Cobblenav.LOGGER.error(e.getMessage(), e);
        }
        return -1;
    }
}
