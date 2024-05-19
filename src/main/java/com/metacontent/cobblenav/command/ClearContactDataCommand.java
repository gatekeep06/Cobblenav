package com.metacontent.cobblenav.command;

import com.metacontent.cobblenav.store.ContactData;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;

public class ClearContactDataCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("pokenav")
                .then(CommandManager.argument("player", EntityArgumentType.players()).requires(source -> source.hasPermissionLevel(2))
                        .then(CommandManager.literal("clearContacts")
                                .executes(ClearContactDataCommand::run))));
    }

    private static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        List<ServerPlayerEntity> players = context.getArgument("player", EntitySelector.class).getPlayers(context.getSource());
        if (!players.isEmpty()) {
            players.forEach(player -> {
                ContactData.executeForDataOf(player, contactData -> contactData.getContacts().clear());
            });
            return 1;
        }
        return -1;
    }
}
