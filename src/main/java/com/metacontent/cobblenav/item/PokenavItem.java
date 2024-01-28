package com.metacontent.cobblenav.item;

import com.cobblemon.mod.common.CobblemonSounds;
import com.cobblemon.mod.common.client.CobblemonClient;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.metacontent.cobblenav.client.screen.pokenav.MainScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
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

    @Environment(EnvType.CLIENT)
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        if (world.isClient()) {
            if (CobblemonClient.INSTANCE.getBattle() == null) {
                playerEntity.playSound(CobblemonSounds.PC_ON, 0.1f, 1.25f);
                MinecraftClient.getInstance().setScreen(new MainScreen());
            }
            return  TypedActionResult.success(playerEntity.getStackInHand(hand), false);
        }
        return TypedActionResult.pass(playerEntity.getStackInHand(hand));
    }
}
