package com.metacontent.cobblenav.item;

import com.cobblemon.mod.common.CobblemonSounds;
import com.cobblemon.mod.common.client.CobblemonClient;
import com.metacontent.cobblenav.client.CobblenavClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PokenavItem extends Item {
    private static final String TRANSLATION_KEY = "item.cobblenav.pokenav_item";

    private final String type;

    public PokenavItem(@Nullable String type, Settings settings) {
        super(settings);
        this.type = type;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        if (world.isClient()) {
            return openPokenavScreen(playerEntity, hand);
        }
        return TypedActionResult.pass(playerEntity.getStackInHand(hand));
    }

    private TypedActionResult<ItemStack> openPokenavScreen(PlayerEntity playerEntity, Hand hand) {
        if (CobblenavClient.TRACK_ARROW_HUD_OVERLAY.isTracking()) {
            CobblenavClient.TRACK_ARROW_HUD_OVERLAY.resetTracking();
        }
        else if (CobblemonClient.INSTANCE.getBattle() == null) {
            playerEntity.playSound(CobblemonSounds.PC_ON, 0.1f, 1.25f);
//            MinecraftClient.getInstance().setScreen(new MainScreen());
        }
        return TypedActionResult.success(playerEntity.getStackInHand(hand), false);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, @Nullable World world, List<Text> list, TooltipContext tooltipContext) {
        if (type != null) {
            list.add(Text.translatable(TRANSLATION_KEY + ".type." + type).formatted(Formatting.GRAY));
        }
        super.appendTooltip(itemStack, world, list, tooltipContext);
    }

    @Override
    public String getTranslationKey(ItemStack itemStack) {
        return TRANSLATION_KEY;
    }
}
