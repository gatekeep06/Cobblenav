package com.metacontent.cobblenav.event.handler;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.metacontent.cobblenav.item.CobblenavItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

public class SecretPokenavEventHandler {
    public static ActionResult handleAttack(PlayerEntity player, World world, Hand hand, Entity entity, EntityHitResult entityHitResult) {
        ItemStack stack = player.getStackInHand(hand);
        if (stack.isOf(CobblenavItems.POKENAV_ITEM_GHOLDENGO) && entity instanceof PokemonEntity pokemonEntity) {
            if (pokemonEntity.getPokemon().getSpecies().showdownId().equals("gholdengo")) {
                int randomInt = world.getRandom().nextBetween(1, 10);
                if (randomInt >= 9) {
                    player.setStackInHand(hand, CobblenavItems.POKENAV_ITEM_INVISIBLE_GHOLDENGO.getDefaultStack());
                }
            }
        }
        return ActionResult.PASS;
    }
}
