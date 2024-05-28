package com.metacontent.cobblenav.client.screen;

import com.cobblemon.mod.common.CobblemonSounds;
import com.metacontent.cobblenav.Cobblenav;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static com.cobblemon.mod.common.api.gui.GuiUtilsKt.blitk;
import static com.cobblemon.mod.common.client.render.RenderHelperKt.drawScaledText;

@Environment(EnvType.CLIENT)
public class AbstractPokenavItemScreen extends Screen {
    public static final Identifier FONT = new Identifier("uniform");
    public static final Identifier BACKGROUND = new Identifier(Cobblenav.ID, "textures/gui/pokenav_item_gui_background.png");
    public static final Identifier BORDERS = new Identifier(Cobblenav.ID, "textures/gui/pokenav_item_gui_borders.png");
    public static final MutableText BASE_TITLE = Text.translatable("gui.cobblenav.pokenav_item.title");
    public static final int BORDER_WIDTH = 240;
    public static final int BORDER_HEIGHT = 180;
    public static final int BORDER_DEPTH = 15;
    public final PlayerEntity player;
    private int borderX;
    private int borderY;

    protected AbstractPokenavItemScreen(Text text) {
        super(text);
        this.player = MinecraftClient.getInstance().player;
    }

    @Override
    protected void init() {
        super.init();

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

        blitk(matrixStack, BORDERS,
                borderX, borderY, BORDER_HEIGHT, BORDER_WIDTH, 0, 0, 256,
                256, 0, 1,1,1,1,false,1);

        drawScaledText(drawContext, FONT, getTitle().setStyle(Style.EMPTY.withBold(true).withColor(0xFFFFFF)),
                borderX + BORDER_DEPTH + 4, borderY + BORDER_DEPTH + 2, 1, 1, (int) (BORDER_WIDTH / 1.5), 0, false, false, i, j);
    }

    @Override
    public void close() {
        player.playSound(CobblemonSounds.PC_OFF, 0.1f, 1.25f);
        super.close();
    }

    public int getBorderX() {
        return borderX;
    }

    public int getBorderY() {
        return borderY;
    }

    @Override
    public MutableText getTitle() {
        return BASE_TITLE;
    }
}
