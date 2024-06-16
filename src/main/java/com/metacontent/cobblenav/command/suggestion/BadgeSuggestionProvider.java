package com.metacontent.cobblenav.command.suggestion;

import com.metacontent.cobblenav.Cobblenav;
import com.metacontent.cobblenav.config.util.Badge;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.server.command.ServerCommandSource;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BadgeSuggestionProvider implements SuggestionProvider<ServerCommandSource> {
    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) throws CommandSyntaxException {
        List<Badge> permittedBadges = Cobblenav.CONFIG.badges.getPermitted(context.getSource().getPlayerOrThrow());
        permittedBadges.forEach(badge -> builder.suggest(badge.type()));
        return builder.buildFuture();
    }
}
