package com.metacontent.cobblenav.client.screen.pokenav;

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

public class StatsScreen extends AbstractPokenavItemScreen {
    private static final int RED = ColorHelper.Argb.getArgb(255, 219, 67, 76);
    private static final int GREEN = ColorHelper.Argb.getArgb(255, 82, 214, 48);
    private static final int GRAY = Color.GRAY.getRGB();
    private static final int ANIM_DURATION = 50;

    private int animProgress;
    private PlayerStats stats;
    private float winRatio = 0.34f;

    protected StatsScreen() {
        super(Text.literal("Stats"));
    }

    @Override
    protected void init() {
        super.init();
        ClientPlayNetworking.send(CobblenavPackets.REQUEST_PLAYER_STATS_PACKET, PacketByteBufs.create());
    }

    public void setStats(PlayerStats stats) {
        this.stats = stats;
//        if (stats.totalPvp() != 0) {
//            this.winRatio = (float) stats.pvpWinnings() / (float) stats.totalPvp();
//        }
        animProgress = ANIM_DURATION;
    }

    @Override
    public void render(DrawContext drawContext, int i, int j, float f) {
        renderBackground(drawContext);
        renderPieChart(drawContext);
        if (stats != null) {
            if (stats.totalPvp() != 0) {
                renderPieChart(drawContext);
            }
        }

        if (animProgress > 0) {
            --animProgress;
        }

        super.render(drawContext, i, j, f);
    }

    private void renderPieChart(DrawContext drawContext) {
        MatrixStack matrixStack = drawContext.getMatrices();
        matrixStack.push();
        matrixStack.translate((int) (width / 2f), (int) (height / 2f), 0);
        drawContext.drawCenteredTextWithShadow(textRenderer, (int) (winRatio * 100) + "%", 0, -3, 0xffffff);
        for (int k = 0; k < 360; k++) {
            matrixStack.push();
            matrixStack.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(k));
            int color;
            if (k < (float) animProgress / (float) ANIM_DURATION * 360) {
                color = GRAY;
            }
            else {
                color = k < winRatio * 360 ? GREEN : RED;
            }
            drawContext.drawVerticalLine(0, 10, 25, color);
            matrixStack.pop();
        }
        matrixStack.pop();
    }
}
