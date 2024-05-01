package com.metacontent.cobblenav.mixin;

import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.selfdot.cobblemontrainers.trainer.Trainer;
import com.selfdot.cobblemontrainers.trainer.TrainerBattleListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Pseudo
@Mixin(TrainerBattleListener.class)
public abstract class TrainerBattleListenerMixin {
    @Accessor
    abstract Map<PokemonBattle, Trainer> getOnBattleVictory();
}
