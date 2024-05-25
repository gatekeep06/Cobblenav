package com.metacontent.cobblenav.client.screen.pokenav;

import com.metacontent.cobblenav.client.screen.AbstractPokenavItemScreen;
import com.metacontent.cobblenav.client.widget.PieChartWidget;
import com.metacontent.cobblenav.client.widget.TableWidget;
import com.metacontent.cobblenav.networking.CobblenavPackets;
import com.metacontent.cobblenav.util.BorderBox;
import com.metacontent.cobblenav.util.PlayerStats;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.ColorHelper;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.cobblemon.mod.common.api.gui.GuiUtilsKt.blitk;

public class StatsScreen extends AbstractPokenavItemScreen {
    private static final int RED = ColorHelper.Argb.getArgb(255, 219, 67, 76);
    private static final int GREEN = ColorHelper.Argb.getArgb(255, 82, 214, 48);
    private static final int ANIM_DURATION = 50;

    private PlayerStats stats;
    private PieChartWidget pieChart;
    private TableWidget<TextWidget> statsTable;

    protected StatsScreen() {
        super(Text.literal("Stats"));
    }

    @Override
    protected void init() {
        super.init();
        ClientPlayNetworking.send(CobblenavPackets.REQUEST_PLAYER_STATS_PACKET, PacketByteBufs.create());
        int x = getBorderX() + BORDER_WIDTH - BORDER_DEPTH - 8;
        int y = getBorderY() + BORDER_DEPTH + 24;
        pieChart = new PieChartWidget(x, y, 25, ANIM_DURATION, GREEN, RED);
        statsTable = new TableWidget<>(x, y + 54, 2, 0, new BorderBox(4, 2));
    }

    public void createStats(PlayerStats stats) {
        if (stats.totalPvp() != 0) {
            float winRatio = (float) stats.pvpWinnings() / (float) stats.totalPvp();
            pieChart.setRatio(winRatio);
        }
        List<TextWidget> textWidgets = new ArrayList<>();
        textWidgets.add(new TextWidget(Text.literal("Total PvP:"), textRenderer));
        textWidgets.add(new TextWidget(Text.literal(String.valueOf(stats.totalPvp())), textRenderer));
        textWidgets.add(new TextWidget(Text.literal("PvP Winnings:"), textRenderer));
        textWidgets.add(new TextWidget(Text.literal(String.valueOf(stats.pvpWinnings())), textRenderer));
        textWidgets.add(new TextWidget(Text.literal("Captures:"), textRenderer));
        textWidgets.add(new TextWidget(Text.literal(String.valueOf(stats.captures())), textRenderer));
        textWidgets.add(new TextWidget(Text.literal("Shiny Captures:"), textRenderer));
        textWidgets.add(new TextWidget(Text.literal(String.valueOf(stats.shinyCaptures())), textRenderer));
        textWidgets.add(new TextWidget(Text.literal("Evolutions:"), textRenderer));
        textWidgets.add(new TextWidget(Text.literal(String.valueOf(stats.evolutions())), textRenderer));
        statsTable.calcRows(textWidgets.size());
        statsTable.setWidgets(textWidgets);
        this.stats = stats;
    }

    @Override
    public void render(DrawContext drawContext, int i, int j, float f) {
        MatrixStack matrixStack = drawContext.getMatrices();

        renderBackground(drawContext);

        blitk(matrixStack, BACKGROUND,
                getBorderX() + BORDER_DEPTH, getBorderY() + BORDER_DEPTH + 20, BORDER_HEIGHT - 2 * BORDER_DEPTH - 20, BORDER_WIDTH - 2 * BORDER_DEPTH, 0, 0, 256,
                256, 0, 1, 1, 1, 1, false, 1);

        if (stats != null) {
            pieChart.render(drawContext, i, j, f);
            statsTable.render(drawContext, i, j, f);
        }

        super.render(drawContext, i, j, f);
    }
}
