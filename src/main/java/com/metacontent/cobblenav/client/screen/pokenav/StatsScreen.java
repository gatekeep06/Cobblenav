package com.metacontent.cobblenav.client.screen.pokenav;

import com.cobblemon.mod.common.api.types.ElementalType;
import com.cobblemon.mod.common.api.types.ElementalTypes;
import com.metacontent.cobblenav.Cobblenav;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class StatsScreen extends AbstractPokenavItemScreen {
    private static final int RED = ColorHelper.Argb.getArgb(255, 219, 67, 76);
    private static final int GREEN = ColorHelper.Argb.getArgb(255, 82, 214, 48);
    private static final int GRAY = Color.GRAY.getRGB();
    private static final int ANIM_DURATION = 50;

    private int animProgress;
    private PlayerStats stats;
    private float winRatio;
    private final List<String> types;
    private final int rosePeriod;
    private int maxTypeUsage;

    protected StatsScreen() {
        super(Text.literal("Stats"));
        types = ElementalTypes.INSTANCE.all().stream().map(ElementalType::getName).toList();
        rosePeriod = 360 / ElementalTypes.INSTANCE.count();
    }

    @Override
    protected void init() {
        super.init();
        ClientPlayNetworking.send(CobblenavPackets.REQUEST_PLAYER_STATS_PACKET, PacketByteBufs.create());
    }

    public void setStats(PlayerStats stats) {
        //TODO: replace test data
        this.stats = new PlayerStats(46, 33, 87, 4, 98, Map.of(
                "fire", 6,
                "water", 18,
                "flying", 36,
                "rock", 4,
                "dark", 7,
                "fairy", 10
        ));
        if (this.stats.totalPvp() != 0) {
            this.winRatio = (float) this.stats.pvpWinnings() / (float) this.stats.totalPvp();
        }
        maxTypeUsage = this.stats.pvpTypeUsage().values().stream().max(Integer::compareTo).orElse(0);
        animProgress = ANIM_DURATION;
    }

    @Override
    public void render(DrawContext drawContext, int i, int j, float f) {
        renderBackground(drawContext);

        if (stats != null) {
            renderPieChart(drawContext);
            renderTypeChart(drawContext);
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
        drawContext.drawCenteredTextWithShadow(textRenderer, (int) (winRatio * 100) + "%", 0, -3, 0xffffff);
        for (int k = 0; k < 360; k++) {
            matrixStack.push();
            matrixStack.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(k));
            int color;
            if (k < (float) animProgress / (float) ANIM_DURATION * 360 || stats.totalPvp() == 0) {
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

    private void renderTypeChart(DrawContext drawContext) {
        MatrixStack matrixStack = drawContext.getMatrices();
        matrixStack.push();
        matrixStack.pop();
    }
}
