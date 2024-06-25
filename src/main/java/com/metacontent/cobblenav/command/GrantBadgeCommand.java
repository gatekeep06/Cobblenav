package com.metacontent.cobblenav.command;

import com.metacontent.cobblenav.Cobblenav;
import com.metacontent.cobblenav.command.suggestion.BadgeSuggestionProvider;
import com.metacontent.cobblenav.config.util.Badge;
import com.metacontent.cobblenav.store.AdditionalStatsData;
import com.metacontent.cobblenav.util.GrantedBadge;
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
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Date;

public class GrantBadgeCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("badge")
                .then(CommandManager.literal("grant")
                        .then(CommandManager.argument("player", EntityArgumentType.player())
                                .requires(source -> {
                                    ServerPlayerEntity player = source.getPlayer();
                                    if (player != null) {
                                        return Cobblenav.CONFIG.badges.getPermitted(player).size() < 2;
                                    }
                                    return false;
                                })
                                .executes(GrantBadgeCommand::runWithoutBadge))
                        .then(CommandManager.argument("badge", StringArgumentType.string()).suggests(new BadgeSuggestionProvider())
                                .then(CommandManager.argument("player", EntityArgumentType.player())
                                        .executes(GrantBadgeCommand::run)))));
    }

    private static int runWithoutBadge(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity source = context.getSource().getPlayerOrThrow();
        Badge badge = Cobblenav.CONFIG.badges.getPermitted(source).stream().findFirst().orElse(null);
        if (badge == null) {
            source.sendMessage(Text.translatable("message.cobblenav.has_no_permitted_badges").formatted(Formatting.RED));
            return -1;
        }
        ServerPlayerEntity player = context.getArgument("player", EntitySelector.class).getPlayer(context.getSource());
        grantBadge(badge, source, player);
        return 1;
    }

    private static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        String badgeType = context.getArgument("badge", String.class).toLowerCase();
        Badge badge = Cobblenav.CONFIG.badges.getByType(badgeType);
        ServerPlayerEntity source = context.getSource().getPlayerOrThrow();
        if (badge == null) {
            source.sendMessage(Text.translatable("message.cobblenav.no_badge").formatted(Formatting.RED));
            return -1;
        }
        ServerPlayerEntity player = context.getArgument("player", EntitySelector.class).getPlayer(context.getSource());
        if (badge.hasPermissionToGrant(source)) {
            grantBadge(badge, source, player);
            return 1;
        }
        else {
            source.sendMessage(Text.translatable("message.cobblenav.has_no_permission").formatted(Formatting.RED));
        }
        return -1;
    }

    private static void grantBadge(Badge badge, ServerPlayerEntity source, ServerPlayerEntity player) {
        GrantedBadge grantedBadge = new GrantedBadge(badge.type(), source.getEntityName(), new Date());
        AdditionalStatsData.executeForDataOf(player, statsData -> statsData.addBadge(grantedBadge));
    }
}
