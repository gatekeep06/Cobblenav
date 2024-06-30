package com.metacontent.cobblenav.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PokefinderItem extends Item {
    private static final String TRANSLATION_KEY = "item.cobblenav.pokefinder_item";

    public PokefinderItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, @Nullable World world, List<Text> list, TooltipContext tooltipContext) {
        if (world != null) {
            NbtCompound nbt = itemStack.getNbt();
            if (nbt != null && nbt.contains("saved_pokemon")) {
                list.add(Text.literal(nbt.getString("saved_pokemon")).formatted(Formatting.AQUA));
            }
        }
    }

    @Override
    public String getTranslationKey(ItemStack itemStack) {
        return TRANSLATION_KEY;
    }
}
