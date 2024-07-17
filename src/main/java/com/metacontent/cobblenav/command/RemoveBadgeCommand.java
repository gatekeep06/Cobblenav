package com.metacontent.cobblenav.command;

import com.metacontent.cobblenav.Cobblenav;
import com.metacontent.cobblenav.command.suggestion.BadgeSuggestionProvider;
import com.metacontent.cobblenav.config.util.Badge;
import com.metacontent.cobblenav.store.AdditionalStatsData;
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

import java.util.concurrent.atomic.AtomicBoolean;

public class RemoveBadgeCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("badge")
                .then(CommandManager.literal("remove").requires(source -> source.hasPermissionLevel(2))
                        .then(CommandManager.literal("all")
                                .then(CommandManager.argument("player", EntityArgumentType.player())
                                        .executes(RemoveBadgeCommand::runForAll)))
                        .then(CommandManager.argument("badge", StringArgumentType.string()).suggests(new BadgeSuggestionProvider())
                                .then(CommandManager.argument("player", EntityArgumentType.player())
                                        .executes(RemoveBadgeCommand::run)))));
    }

    private static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getArgument("player", EntitySelector.class).getPlayer(context.getSource());
        String type = context.getArgument("badge", String.class);
        Badge badge = Cobblenav.CONFIG.badges.getByType(type);
        AtomicBoolean result = new AtomicBoolean(false);
        if (badge != null) {
            AdditionalStatsData.executeForDataOf(player, data -> result.set(data.removeBadge(badge)));
        }
        return result.get() ? 1 : -1;
    }

    private static int runForAll(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getArgument("player", EntitySelector.class).getPlayer(context.getSource());
        AdditionalStatsData.executeForDataOf(player, AdditionalStatsData::removeAllBadges);
        return 1;
    }
}
