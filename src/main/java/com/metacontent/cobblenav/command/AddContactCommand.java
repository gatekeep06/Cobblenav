package com.metacontent.cobblenav.command;

import com.metacontent.cobblenav.util.CobblenavNbtHelper;
import com.metacontent.cobblenav.util.ContactSaverEntity;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;

public class AddContactCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("pokenav").requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.argument("player", EntityArgumentType.players())
                        .then(CommandManager.literal("addContact")
                                .then(CommandManager.argument("contact", EntityArgumentType.player())
                                        .executes(AddContactCommand::run)))));
    }

    private static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity contact = context.getArgument("contact", EntitySelector.class).getPlayer(context.getSource());
        if (contact != null) {
            List<ServerPlayerEntity> players = context.getArgument("player", EntitySelector.class).getPlayers(context.getSource());
            if (!players.isEmpty()) {
                players.forEach(p -> {
                    CobblenavNbtHelper.updateContact(p, contact, null, false, true);
                });
                return 1;
            }
            return -1;
        }
        return -1;
    }
}
