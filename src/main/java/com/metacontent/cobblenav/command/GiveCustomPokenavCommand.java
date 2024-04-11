package com.metacontent.cobblenav.command;

import com.metacontent.cobblenav.Cobblenav;
import com.metacontent.cobblenav.config.CobblenavConfig;
import com.metacontent.cobblenav.item.CobblenavItems;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.CommandSource;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;

public class GiveCustomPokenavCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("giveCustomPokenav").requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.argument("players", EntityArgumentType.players())
                        .then(CommandManager.argument("predicate", IntegerArgumentType.integer()).suggests((context, builder) -> {
                            for (Integer integer : CobblenavConfig.CUSTOM_POKENAV_PREDICATES) {
                                if (CommandSource.shouldSuggest(builder.getRemaining(), integer.toString())) {
                                    builder.suggest(integer);
                                }
                            }
                            return builder.buildFuture();
                        }).executes(GiveCustomPokenavCommand::run))));
    }

    private static int run(CommandContext<ServerCommandSource> context) {
        try {
            List<ServerPlayerEntity> players = context.getArgument("players", EntitySelector.class).getPlayers(context.getSource());
            int predicate = context.getArgument("predicate", int.class);
            for (ServerPlayerEntity player : players) {
                ItemStack stack = CobblenavItems.CUSTOM_POKENAV_ITEM.getDefaultStack();
                stack.getOrCreateNbt().putInt("CustomModelData", predicate);
                player.getInventory().offerOrDrop(stack);
            }
            return 1;
        }
        catch (Throwable e) {
            Cobblenav.LOGGER.error(e.getMessage(), e);
        }
        return -1;
    }
}
