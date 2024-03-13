package com.metacontent.cobblenav.client.widget;

import com.cobblemon.mod.common.CobblemonSounds;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import static com.cobblemon.mod.common.api.gui.GuiUtilsKt.blitk;
import static com.cobblemon.mod.common.client.render.RenderHelperKt.drawScaledText;

public class TextButton extends AbstractPokenavButton {
    private final MutableText message;
    private final OnPressed action;
    private boolean isSoundPlayed = false;

    public TextButton(int x, int y, int width, int height, int offsetX, int offsetY, Text text, OnPressed action) {
        super(x, y, width, height, offsetX, offsetY);
        this.message = text.copyContentOnly().setStyle(Style.EMPTY.withColor(0xFFFFFF).withFont(FONT));
        this.action = action;
    }

    @Override
    protected void renderButton(DrawContext drawContext, int i, int j, float f) {
        MatrixStack poseStack = drawContext.getMatrices();
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
        }
        else {
            isSoundPlayed = false;
            blitk(poseStack, TEXTURE,
                    getX(), getY(), getHeight(), getWidth(), offsetX, offsetY, 256,
                    256, 0, 1, 1, 1, 1, false, 1);
        }
        drawScaledText(drawContext, FONT, message,
                getX() + width / 2, getY() + height / 2 - 4, 1, 1, (int) (getWidth() * 0.75), 0, true, false, i, j);
    }

    @Override
    public void onClick(double d, double e) {
        action.onPressed();
    }
}
