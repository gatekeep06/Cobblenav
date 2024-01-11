package com.metacontent.cobblenav.item;

import com.cobblemon.mod.common.CobblemonSounds;
import com.metacontent.cobblenav.client.screen.pokenav.MainScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class PokenavItem extends Item {
    public PokenavItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        if (world.isClient()) {
            playerEntity.playSound(CobblemonSounds.PC_ON, 0.1f, 1.25f);
            MinecraftClient.getInstance().setScreen(new MainScreen());
            return  TypedActionResult.success(playerEntity.getStackInHand(hand));
        }
        return TypedActionResult.pass(playerEntity.getStackInHand(hand));
    }
}
