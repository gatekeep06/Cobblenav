package com.metacontent.cobblenav.client.widget;

import com.cobblemon.mod.common.CobblemonSounds;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import static com.cobblemon.mod.common.api.gui.GuiUtilsKt.blitk;
import static com.cobblemon.mod.common.client.render.RenderHelperKt.drawScaledText;

public class PokenavItemButton extends ClickableWidget {
    private static final Identifier FONT = new Identifier("uniform");
    private final MutableText message;
    private final Identifier buttonTexture;
    private final Identifier buttonTextureHovered;
    private final int offsetX;
    private final int offsetY;
    private final int pullout;
    private final int cooldown;
    private final OnPressed action;
    private final PlayerEntity player;
    private boolean isSoundPlayed = false;
    private int timer = 0;

    public PokenavItemButton(int x, int y, int width, int height, int offsetX, int offsetY, int pullout, int cooldown, Text text, Identifier texture, @Nullable Identifier textureHovered, OnPressed action) {
        super(x, y, width, height, text);
        this.message = text.copyContentOnly().setStyle(Style.EMPTY.withColor(0xFFFFFF).withFont(FONT));
        this.buttonTexture = texture;
        this.buttonTextureHovered = textureHovered;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.pullout = pullout;
        this.cooldown = cooldown;
        this.action = action;
        this.player = MinecraftClient.getInstance().player;
    }

    @Override
    protected void renderButton(DrawContext drawContext, int i, int j, float f) {
        MatrixStack poseStack = drawContext.getMatrices();

        if (timer > 0) {
            blitk(poseStack, buttonTexture,
                    getX() - pullout, getY(), getHeight(), getWidth() + pullout, offsetX, offsetY, 256,
                    256, 0, 1, 1, 1, 1, false, 1);
            blitk(poseStack, buttonTextureHovered,
                    getX() - pullout, (int) ((1 - (double) timer / (double) cooldown) * getHeight() + getY()), (int) ((double) timer / (double) cooldown * getHeight() + 1), getWidth() + pullout, offsetX, (int) ((1 - (double) timer / (double) cooldown) * getHeight() + offsetY), 256,
                    256, 0, 1, 1, 1, 1, false, 1);
            timer--;
            return;
        }

        if (isHovered() && buttonTextureHovered != null) {
            if (!isSoundPlayed) {
                isSoundPlayed = true;
                if (player != null) {
                    player.playSound(CobblemonSounds.PC_GRAB, 0.05f, 0.5f);
                }
            }
            blitk(poseStack, buttonTextureHovered,
                    getX() - pullout, getY(), getHeight(), getWidth() + pullout, offsetX, offsetY, 256,
                    256, 0, 1, 1, 1, 1, false, 1);
        }
        else {
            isSoundPlayed = false;
            blitk(poseStack, buttonTexture,
                    getX(), getY(), getHeight(), getWidth(), offsetX, offsetY, 256,
                    256, 0, 1, 1, 1, 1, false, 1);
        }
        drawScaledText(drawContext, FONT, message,
                getX() + (isHovered() ? 12 : 12 + pullout), getY() + 3, 1, 1, getWidth(), 0, false, false, i, j);
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder narrationMessageBuilder) {

    }

    @Override
    public void onClick(double d, double e) {
        if (timer <= 0) {
            timer = cooldown;
            action.onPressed();
        }
    }

    @Override
    public boolean mouseClicked(double d, double e, int i) {
        if (this.active && this.visible) {
            if (this.isValidClickButton(i)) {
                boolean bl = this.clicked(d, e);
                if (bl) {
                    this.onClick(d, e);
                    return true;
                }
            }
        }
        return false;
    }

    public interface OnPressed {
        void onPressed();
    }
}
