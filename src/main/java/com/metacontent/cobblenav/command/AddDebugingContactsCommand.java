package com.metacontent.cobblenav.command;

import com.metacontent.cobblenav.store.ContactData;
import com.metacontent.cobblenav.util.PokenavContact;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;
import java.util.UUID;

public class AddDebugingContactsCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("cobblenavdebug")
                .then(CommandManager.literal("contacts").then(CommandManager.argument("amount", IntegerArgumentType.integer(1))
                        .executes(AddDebugingContactsCommand::run))));
    }

    private static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
        int amount = context.getArgument("amount", Integer.class);
        ContactData.executeForDataOf(player, contactData -> {
            for (int i = 0; i < amount; i++) {
                GameProfile profile = new GameProfile(UUID.randomUUID(), "Player" + i);
                PokenavContact contact = new PokenavContact(profile.getId().toString(), profile, profile.getName(), "damn that's very long title", 100, 100, List.of(), false);
                contactData.getContacts().put(contact.getKey(), contact);
            }
        });
        return 1;
    }
}
