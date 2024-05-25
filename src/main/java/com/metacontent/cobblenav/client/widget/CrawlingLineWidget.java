package com.metacontent.cobblenav.client.widget;

import com.metacontent.cobblenav.util.BorderBox;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.widget.AbstractTextWidget;
import net.minecraft.text.Text;

public class CrawlingLineWidget extends AbstractTextWidget {
    private static final int DELAY = 40;
    private float delayed = 0f;
    private Text text;
    private final boolean shadow;
    private final float scale;
    private final BorderBox textOffsets;
    private int textWidth;
    private float textX;
    private boolean shouldMoveToLeft = true;

    public CrawlingLineWidget(Text text, int x, int y, int width, int height, float scale, BorderBox textOffsets, boolean shadow) {
        super(x, y, width, height, text, MinecraftClient.getInstance().textRenderer);
        setText(text);
        this.scale = scale;
        this.textOffsets = textOffsets;
        this.shadow = shadow;
    }

    public CrawlingLineWidget(int x, int y, int width, int height, float scale, BorderBox textOffsets) {
        this(Text.empty(), x, y, width, height, scale, textOffsets, false);
    }

    public void setText(Text text) {
        this.text = text;
        this.textWidth = getTextRenderer().getWidth(text);
    }

    @Override
    protected void renderButton(DrawContext drawContext, int i, int j, float f) {
        drawContext.enableScissor(getX(), getY(), getX() + width, getY() + height);
        drawContext.getMatrices().push();
        drawContext.getMatrices().scale(scale, scale, 1f);
        drawContext.drawText(getTextRenderer(), text, (int) (textX / scale) + textOffsets.left, (int) (getY() / scale) + textOffsets.top, 0xffffff, shadow);
        drawContext.getMatrices().pop();
        drawContext.disableScissor();
        crawl(f);
    }

    public void renderDynamic(DrawContext drawContext, Text text, boolean shadow, float f) {
        this.textWidth = (int) (getTextRenderer().getWidth(text) * scale);
        drawContext.enableScissor(getX(), getY(), getX() + width, getY() + height);
        drawContext.getMatrices().push();
        drawContext.getMatrices().scale(scale, scale, 1f);
        drawContext.drawText(getTextRenderer(), text, (int) (textX / scale) + textOffsets.left, (int) (getY() / scale) + textOffsets.top, 0xffffff, shadow);
        drawContext.getMatrices().pop();
        drawContext.disableScissor();
        crawl(f);
    }

    private void crawl(float f) {
        if (textWidth <= width) {
            textX = getX();
            return;
        }
        if (delayed >= DELAY) {
            if (shouldMoveToLeft) {
                textX -= 0.1f;
            }
            else {
                textX += 0.1f;
            }
            if (textX >= (float) getX() || textX <= (float) (getX() - (textWidth - width))) {
                delayed = 0f;
                shouldMoveToLeft = !shouldMoveToLeft;
            }
        }
        else {
            delayed += f;
        }
    }

    @Override
    public void setX(int i) {
        super.setX(i);
        this.textX = (float) getX();
    }
}
