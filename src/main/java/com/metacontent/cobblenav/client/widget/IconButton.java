package com.metacontent.cobblenav.client.widget;

import com.cobblemon.mod.common.CobblemonSounds;
import com.metacontent.cobblenav.client.CobblenavClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import static com.cobblemon.mod.common.api.gui.GuiUtilsKt.blitk;

public class IconButton extends AbstractPokenavButton {
    private final int cooldown;
    private final OnPressed action;
    private boolean isSoundPlayed = false;
    private int timer = 0;
    private final boolean showTooltip;
    private final Text tooltip;
    private final TextRenderer textRenderer;

    public IconButton(int x, int y, int width, int height, int offsetX, int offsetY, int cooldown, @Nullable Text tooltip, OnPressed action) {
        super(x, y, width, height, offsetX, offsetY);
        this.cooldown = cooldown;
        this.action = action;
        this.tooltip = tooltip;
        this.showTooltip = CobblenavClient.CONFIG.showButtonTooltips && tooltip != null;
        this.textRenderer = MinecraftClient.getInstance().textRenderer;
    }

    public IconButton(int x, int y, int width, int height, int offsetX, int offsetY, Text tooltip, OnPressed action) {
        this(x, y, width, height, offsetX, offsetY, 0, tooltip, action);
    }

        @Override
    protected void renderButton(DrawContext drawContext, int i, int j, float f) {
        MatrixStack poseStack = drawContext.getMatrices();

        if (timer > 0) {
            blitk(poseStack, TEXTURE,
                    getX(), getY(), getHeight(), getWidth(), offsetX, offsetY, 256,
                    256, 0, 1, 1, 1, 1, false, 1);
            blitk(poseStack, HOVERED_TEXTURE,
                    getX(), (int) ((1 - (double) timer / (double) cooldown) * getHeight() + getY()), (int) ((double) timer / (double) cooldown * getHeight() + 1), getWidth(), offsetX, (int) ((1 - (double) timer / (double) cooldown) * getHeight() + offsetY), 256,
                    256, 0, 1, 1, 1, 1, false, 1);
            timer--;
            return;
        }

        if (isHovered()) {
            if (!isSoundPlayed) {
                isSoundPlayed = true;
                if (player != null) {
                    player.playSound(CobblemonSounds.PC_GRAB, 0.05f, 0.5f);
                }
            }
            blitk(poseStack, HOVERED_TEXTURE,
                    getX(), getY(), getHeight(), getWidth(), offsetX, offsetY, 256,
                    256, 0, 1, 1, 1, 1, false, 1);
            if (showTooltip) {
                drawContext.drawTooltip(textRenderer, tooltip, i, j);
            }
        }
        else {
            isSoundPlayed = false;
            blitk(poseStack, TEXTURE,
                    getX(), getY(), getHeight(), getWidth(), offsetX, offsetY, 256,
                    256, 0, 1, 1, 1, 1, false, 1);
        }
    }

    @Override
    public void onClick(double d, double e) {
        if (timer <= 0) {
            timer = cooldown;
            action.onPressed();
        }
    }
}
