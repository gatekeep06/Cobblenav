package com.metacontent.cobblenav.util;

import com.cobblemon.mod.common.api.spawning.WorldSlice;
import com.cobblemon.mod.common.api.spawning.context.AreaContextResolver;
import com.cobblemon.mod.common.api.spawning.context.AreaSpawningContext;
import com.cobblemon.mod.common.api.spawning.context.calculators.AreaSpawningContextCalculator;
import com.cobblemon.mod.common.api.spawning.context.calculators.AreaSpawningInput;
import com.cobblemon.mod.common.api.spawning.spawner.Spawner;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PokenavAreaContextResolver implements AreaContextResolver {
    @NotNull
    @Override
    public List<AreaSpawningContext> resolve(@NotNull Spawner spawner, @NotNull List<? extends AreaSpawningContextCalculator<?>> contextCalculators, @NotNull WorldSlice slice) {
        BlockPos.Mutable pos = new BlockPos.Mutable(1, 2, 3);
        AreaSpawningInput input = new AreaSpawningInput(spawner, pos, slice);
        List<AreaSpawningContext> contexts = new ArrayList<>();

        int x = slice.getBaseX();
        int y = slice.getBaseY();
        int z = slice.getBaseZ();

        while (x < slice.getBaseX() + slice.getLength()) {
            while (y < slice.getBaseY() + slice.getHeight()) {
                while (z < slice.getBaseZ() + slice.getWidth()) {
                    pos.set(x, y, z);
                    AreaSpawningContextCalculator<?> fittedContextCalculator = contextCalculators
                            .stream().filter(calculator -> calculator.fits(input)).findFirst().orElse(null);
                    if (fittedContextCalculator != null) {
                        AreaSpawningContext context = fittedContextCalculator.calculate(input);
                        if (context != null) {
                            contexts.add(context);
                            pos = new BlockPos.Mutable(1, 2, 3);
                            input.setPosition(pos);
                        }
                    }
                    z++;
                }
                y++;
                z = slice.getBaseZ();
            }
            x++;
            y = slice.getBaseY();
            z = slice.getBaseZ();
        }
        return contexts;
    }
}
