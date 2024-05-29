package com.metacontent.cobblenav.client.screen;

import com.cobblemon.mod.common.CobblemonSounds;
import com.metacontent.cobblenav.Cobblenav;
import com.metacontent.cobblenav.client.CobblenavClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.awt.*;

import static com.cobblemon.mod.common.api.gui.GuiUtilsKt.blitk;
import static com.cobblemon.mod.common.client.render.RenderHelperKt.drawScaledText;

@Environment(EnvType.CLIENT)
public abstract class AbstractPokenavItemScreen extends Screen {
    public static final Identifier FONT = new Identifier("uniform");
    public static final Identifier BACKGROUND = new Identifier(Cobblenav.ID, "textures/gui/pokenav_item_gui_background.png");
    public static final Identifier BORDERS = new Identifier(Cobblenav.ID, "textures/gui/pokenav_item_gui_borders.png");
    public static final int BORDER_WIDTH = 240;
    public static final int BORDER_HEIGHT = 180;
    public static final int BORDER_DEPTH = 15;
    public final PlayerEntity player;
    private final float scale;
    private int borderX;
    private int borderY;

    protected AbstractPokenavItemScreen(Text text) {
        super(text);
        this.player = MinecraftClient.getInstance().player;
        this.scale = CobblenavClient.CONFIG.screenScale;
    }

    @Override
    protected void init() {
        super.init();

        width = (int) (width / scale);
        height = (int) (height / scale);

        borderX = (width - BORDER_WIDTH) / 2;
        borderY = (height - BORDER_HEIGHT) / 2 - 10;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void render(DrawContext drawContext, int i, int j, float f) {
        MatrixStack matrixStack = drawContext.getMatrices();
        matrixStack.push();
        matrixStack.scale(scale, scale, 1f);

        renderBackground(drawContext);

//        drawContext.enableScissor((borderX + BORDER_DEPTH) * 2, (borderY + BORDER_DEPTH + 20) * 2,
//                (borderX + BORDER_WIDTH - BORDER_DEPTH) * 2, (borderY + BORDER_HEIGHT - BORDER_DEPTH) * 2);
        renderScreen(drawContext, (int) (i / scale), (int) (j / scale), f);
//        drawContext.disableScissor();

        blitk(matrixStack, BORDERS,
                borderX, borderY, BORDER_HEIGHT, BORDER_WIDTH, 0, 0, 256,
                256, 0, 1,1,1,1,false,1);
        drawScaledText(drawContext, FONT, Text.translatable("gui.cobblenav.pokenav_item.title").setStyle(Style.EMPTY.withBold(true).withColor(0xFFFFFF)),
                borderX + BORDER_DEPTH + 4, borderY + BORDER_DEPTH + 2, 1, 1, (int) (BORDER_WIDTH / 1.5), 0, false, false, i, j);

        matrixStack.pop();
    }

    abstract public void renderScreen(DrawContext drawContext, int i, int j, float f);

    @Override
    public void close() {
        player.playSound(CobblemonSounds.PC_OFF, 0.1f, 1.25f);
        super.close();
    }

    @Override
    public boolean mouseClicked(double d, double e, int i) {
        onMouseClicked(d / scale, e / scale, i);
        return super.mouseClicked(d / scale, e / scale, i);
    }

    @Override
    public boolean mouseDragged(double d, double e, int i, double f, double g) {
        onMouseDragged(d / scale, e / scale, i, f / scale, g / scale);
        return super.mouseDragged(d, e, i, f, g);
    }

    @Override
    public boolean mouseScrolled(double d, double e, double f) {
        onMouseScrolled(d / scale, e / scale, f);
        return super.mouseScrolled(d, e, f);
    }

    abstract public void onMouseClicked(double d, double e, int i);

    abstract public void onMouseDragged(double d, double e, int i, double f, double g);

    abstract public void onMouseScrolled(double d, double e, double f);
}
