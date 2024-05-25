package com.metacontent.cobblenav.client.widget;

import com.metacontent.cobblenav.util.BorderBox;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.text.Text;

public class CrawlingLineWidget implements Drawable {
    private static final int DELAY = 40;
    private float delayed = 0f;
    private final TextRenderer textRenderer;
    private Text text;
    private final boolean shadow;
    private int x;
    private int y;
    private int width;
    private int height;
    private final float scale;
    private final BorderBox textOffsets;
    private int textWidth;
    private float textX;
    private boolean shouldMoveToLeft = true;

    public CrawlingLineWidget(Text text, int x, int y, int width, int height, float scale, BorderBox textOffsets, boolean shadow) {
        this.textRenderer = MinecraftClient.getInstance().textRenderer;
        setText(text);
        setX(x);
        this.y = y;
        this.width = width;
        this.height = height;
        this.scale = scale;
        this.textOffsets = textOffsets;
        this.shadow = shadow;
    }

    public CrawlingLineWidget(int x, int y, int width, int height, float scale, BorderBox textOffsets) {
        this(Text.empty(), x, y, width, height, scale, textOffsets, false);
    }

    public void setText(Text text) {
        this.text = text;
        this.textWidth = textRenderer.getWidth(text);
    }

    @Override
    public void render(DrawContext drawContext, int i, int j, float f) {
        drawContext.enableScissor(x, y, x + width, y + height);
        drawContext.getMatrices().push();
        drawContext.getMatrices().scale(scale, scale, 1f);
        drawContext.drawText(textRenderer, text, (int) (textX / scale) + textOffsets.left, (int) (y / scale) + textOffsets.top, 0xffffff, shadow);
        drawContext.getMatrices().pop();
        drawContext.disableScissor();
        crawl(f);
    }

    public void renderDynamic(DrawContext drawContext, Text text, boolean shadow, float f) {
        this.textWidth = (int) (textRenderer.getWidth(text) * scale);
        drawContext.enableScissor(x, y, x + width, y + height);
        drawContext.getMatrices().push();
        drawContext.getMatrices().scale(scale, scale, 1f);
        drawContext.drawText(textRenderer, text, (int) (textX / scale) + textOffsets.left, (int) (y / scale) + textOffsets.top, 0xffffff, shadow);
        drawContext.getMatrices().pop();
        drawContext.disableScissor();
        crawl(f);
    }

    private void crawl(float f) {
        if (textWidth <= width) {
            textX = x;
            return;
        }
        if (delayed >= DELAY) {
            if (shouldMoveToLeft) {
                textX -= 0.1f;
            }
            else {
                textX += 0.1f;
            }
            if (textX >= (float) x || textX <= (float) (x - (textWidth - width))) {
                delayed = 0f;
                shouldMoveToLeft = !shouldMoveToLeft;
            }
        }
        else {
            delayed += f;
        }
    }

    public void setX(int x) {
        this.x = x;
        this.textX = (float) x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
