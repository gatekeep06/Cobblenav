package com.metacontent.cobblenav.command;

import com.metacontent.cobblenav.util.ContactSaverEntity;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class SetPlayerTitleCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("pokenav").requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.literal("title")
                        .then(CommandManager.literal("set")
                                .then(CommandManager.argument("title", StringArgumentType.string())
                                        .executes(context -> run((ContactSaverEntity) context.getSource().getPlayer(), context.getArgument("title", String.class)))))
                        .then(CommandManager.literal("setFor")
                                .then(CommandManager.argument("player", EntityArgumentType.player())
                                        .then(CommandManager.argument("title", StringArgumentType.string())
                                                .executes(context -> run((ContactSaverEntity) context.getArgument("player", EntitySelector.class).getPlayer(context.getSource()), context.getArgument("title", String.class))))))));
    }

    private static int run(ContactSaverEntity player, String title) {
        if (player != null) {
            NbtCompound nbt = player.cobblenav$getContactData();
            nbt.putString("title", title);
            return 1;
        }
        return -1;
    }
}
