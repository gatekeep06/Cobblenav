package com.metacontent.cobblenav.client.widget;

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
    private final int animDuration;
    private final int firstColor;
    private final int secondColor;
    private final TextRenderer textRenderer;
    private int animProgress;

    public PieChartWidget(int animDuration, int firstColor, int secondColor, TextRenderer textRenderer) {
        this.animDuration = animDuration;
        this.firstColor = firstColor;
        this.secondColor = secondColor;
        this.textRenderer = textRenderer;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
        this.animProgress = animDuration;
    }

    @Override
    public void render(DrawContext drawContext, int i, int j, float f) {
        MatrixStack matrixStack = drawContext.getMatrices();
        matrixStack.push();
        matrixStack.translate(0f, 0f, 0f);
        drawContext.fill(-12, -12, 12, 12, ColorHelper.Argb.getArgb(90, 0, 0, 0));
        drawContext.drawCenteredTextWithShadow(textRenderer, (int) (ratio * 100) + "%", 0, -3, 0xffffff);
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
            drawContext.drawVerticalLine(0, 10, 25, color);
            //drawContext.drawVerticalLine(0, 23, 25, ColorHelper.Argb.getArgb(100, 0, 0, 0));
            matrixStack.pop();
        }
        matrixStack.pop();
        ++animProgress;
    }
}
