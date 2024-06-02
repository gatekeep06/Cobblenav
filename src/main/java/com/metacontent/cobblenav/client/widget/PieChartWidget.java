package com.metacontent.cobblenav.client.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.RotationAxis;

import java.awt.*;

public class PieChartWidget implements Drawable {
    private static final int GRAY = Color.GRAY.getRGB();
    private float ratio = -1;
    private final int x;
    private final int y;
    private final int radius;
    private final int animDuration;
    private final int firstColor;
    private final int secondColor;
    private final TextRenderer textRenderer;
    private int animProgress;

    public PieChartWidget(int x, int y, int radius, int animDuration, int firstColor, int secondColor) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.animDuration = animDuration;
        this.firstColor = firstColor;
        this.secondColor = secondColor;
        this.textRenderer = MinecraftClient.getInstance().textRenderer;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
        this.animProgress = animDuration;
    }

    @Override
    public void render(DrawContext drawContext, int i, int j, float f) {
        MatrixStack matrixStack = drawContext.getMatrices();
        matrixStack.push();
        matrixStack.translate(x - radius, y + radius, 0f);
        drawContext.fill(-12, -12, 12, 12, ColorHelper.Argb.getArgb(90, 0, 0, 0));
        drawContext.drawCenteredTextWithShadow(textRenderer, ratio == -1 ? "-" : (int) (ratio * 100) + "%", 0, -3, 0xffffff);
        for (int k = 0; k < 180; k++) {
            matrixStack.push();
            matrixStack.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(k * 2));
            int color;
            if (k < (float) animProgress / (float) animDuration * 180 || ratio == -1) {
                color = GRAY;
            }
            else {
                color = k < ratio * 180 ? firstColor : secondColor;
            }
            drawContext.drawVerticalLine(0, 10, radius, color);
            //drawContext.drawVerticalLine(0, 23, 25, ColorHelper.Argb.getArgb(100, 0, 0, 0));
            matrixStack.pop();
        }
        matrixStack.pop();
        if (animProgress > 0) {
            --animProgress;
        }
    }
}
