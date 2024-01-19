package com.metacontent.cobblenav.command;

import com.metacontent.cobblenav.util.ContactSaverEntity;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.List;

public class GetContactDataCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("pokenav").requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.argument("player", EntityArgumentType.players())
                        .then(CommandManager.literal("getContacts")
                                .executes(GetContactDataCommand::run))));
    }

    private static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player != null) {
            List<ServerPlayerEntity> players = context.getArgument("player", EntitySelector.class).getPlayers(context.getSource());
            players.forEach(p -> {
                if (p instanceof ContactSaverEntity contactSaverEntity) {
                    player.sendMessage(Text.literal(p.getEntityName() + ": " + contactSaverEntity.cobblenav$getContactData().toString()));
                }
            });
            return 1;
        }
        return -1;
    }
}
