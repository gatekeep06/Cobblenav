package com.metacontent.cobblenav.client.screen.pokenav;

import com.cobblemon.mod.common.api.types.ElementalType;
import com.cobblemon.mod.common.api.types.ElementalTypes;
import com.metacontent.cobblenav.client.screen.AbstractPokenavItemScreen;
import com.metacontent.cobblenav.networking.CobblenavPackets;
import com.metacontent.cobblenav.util.PlayerStats;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.RotationAxis;

import java.awt.*;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.cobblemon.mod.common.api.gui.GuiUtilsKt.blitk;

public class StatsScreen extends AbstractPokenavItemScreen {
    private static final int RED = ColorHelper.Argb.getArgb(255, 219, 67, 76);
    private static final int GREEN = ColorHelper.Argb.getArgb(255, 82, 214, 48);
    private static final int GRAY = Color.GRAY.getRGB();
    private static final int ANIM_DURATION = 50;

    private int animProgress;
    private PlayerStats stats;
    private float winRatio;

    protected StatsScreen() {
        super(Text.literal("Stats"));
    }

    @Override
    protected void init() {
        super.init();
        ClientPlayNetworking.send(CobblenavPackets.REQUEST_PLAYER_STATS_PACKET, PacketByteBufs.create());
    }

    public void setStats(PlayerStats stats) {
        //TODO: replace test data
        this.stats = new PlayerStats(46, 32, 87, 4, 98, Map.of(), Date.from(Instant.now()), List.of("dark", "fairy"));
        if (this.stats.totalPvp() != 0) {
            this.winRatio = (float) this.stats.pvpWinnings() / (float) this.stats.totalPvp();
        }
        animProgress = ANIM_DURATION;
    }

    @Override
    public void render(DrawContext drawContext, int i, int j, float f) {
        MatrixStack matrixStack = drawContext.getMatrices();

        renderBackground(drawContext);

        blitk(matrixStack, BACKGROUND,
                getBorderX() + BORDER_DEPTH, getBorderY() + BORDER_DEPTH + 20, BORDER_HEIGHT - 2 * BORDER_DEPTH - 20, BORDER_WIDTH - 2 * BORDER_DEPTH, 0, 0, 256,
                256, 0, 1, 1, 1, 1, false, 1);

        if (stats != null) {
            renderPieChart(drawContext);
        }

        if (animProgress > 0) {
            --animProgress;
        }

        super.render(drawContext, i, j, f);
    }

    private void renderPieChart(DrawContext drawContext) {
        MatrixStack matrixStack = drawContext.getMatrices();
        matrixStack.push();
        matrixStack.translate(width / 2f,height / 2f, 0f);
        drawContext.fill(-12, -12, 12, 12, ColorHelper.Argb.getArgb(90, 0, 0, 0));
        drawContext.drawCenteredTextWithShadow(textRenderer, (int) (winRatio * 100) + "%", 0, -3, 0xffffff);
        for (int k = 0; k < 180; k++) {
            matrixStack.push();
            matrixStack.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(k * 2));
            int color;
            if (k < (float) animProgress / (float) ANIM_DURATION * 180 || stats.totalPvp() == 0) {
                color = GRAY;
            }
            else {
                color = k < winRatio * 180 ? GREEN : RED;
            }
            drawContext.drawVerticalLine(0, 10, 25, color);
            //drawContext.drawVerticalLine(0, 23, 25, ColorHelper.Argb.getArgb(100, 0, 0, 0));
            matrixStack.pop();
        }
        matrixStack.pop();
    }
}
