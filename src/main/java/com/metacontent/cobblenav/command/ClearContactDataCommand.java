package com.metacontent.cobblenav.command;

import com.metacontent.cobblenav.Cobblenav;
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

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class ClearContactDataCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("pokenav")
                .then(CommandManager.argument("player", EntityArgumentType.players()).requires(source -> source.hasPermissionLevel(2))
                        .then(CommandManager.literal("clearContacts")
                                .executes(ClearContactDataCommand::run))));
    }

    private static int run(CommandContext<ServerCommandSource> context) {
        try {
            List<ServerPlayerEntity> players = context.getArgument("player", EntitySelector.class).getPlayers(context.getSource());
            if (!players.isEmpty()) {
                players.forEach(player -> {
                    if (player instanceof ContactSaverEntity contactSaverEntity) {
                        NbtCompound nbt = contactSaverEntity.cobblenav$getContactData();
                        for (Iterator<String> iterator = nbt.getKeys().iterator(); iterator.hasNext(); ) {
                            String key = iterator.next();
                            if (!key.equals("title")) {
                                nbt.remove(key);
                            }
                        }
                    }
                });
                return 1;
            }
        }
        catch (Throwable e) {
            Cobblenav.LOGGER.error(e.getMessage(), e);
        }
        return -1;
    }
}
