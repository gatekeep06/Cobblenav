package com.metacontent.cobblenav.client.screen.pokenav;

import com.cobblemon.mod.common.CobblemonSounds;
import com.metacontent.cobblenav.client.screen.AbstractPokenavItemScreen;
import com.metacontent.cobblenav.client.widget.CrawlingLineWidget;
import com.metacontent.cobblenav.client.widget.IconButton;
import com.metacontent.cobblenav.client.widget.PieChartWidget;
import com.metacontent.cobblenav.client.widget.TableWidget;
import com.metacontent.cobblenav.networking.CobblenavPackets;
import com.metacontent.cobblenav.util.BorderBox;
import com.metacontent.cobblenav.util.PlayerStats;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.AbstractTextWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
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
    private static final int STAT_NAME_WIDTH = 52;
    private static final int STAT_VALUE_WIDTH = 18;
    private static final List<AbstractTextWidget> LINE_WIDGETS = new ArrayList<>(List.of(
            new CrawlingLineWidget(Text.literal("Total PvP:"), 0, 0, STAT_NAME_WIDTH, 10, 0.6f, new BorderBox(2), true),
            new CrawlingLineWidget(Text.literal("PvP Winnings:"), 0, 0, STAT_NAME_WIDTH, 10, 0.6f, new BorderBox(2), true),
            new CrawlingLineWidget(Text.literal("Captures:"), 0, 0, STAT_NAME_WIDTH, 10, 0.6f, new BorderBox(2), true),
            new CrawlingLineWidget(Text.literal("Shiny Captures:"), 0, 0, STAT_NAME_WIDTH, 10, 0.6f, new BorderBox(2), true),
            new CrawlingLineWidget(Text.literal("Evolutions:"), 0, 0, STAT_NAME_WIDTH, 10, 0.6f, new BorderBox(2), true)));

    private PlayerStats stats;
    private PieChartWidget pieChart;
    private TableWidget<AbstractTextWidget> statsTable;
    private IconButton backButton;

    protected StatsScreen() {
        super(Text.literal("Stats"));
    }

    @Override
    protected void init() {
        super.init();
        ClientPlayNetworking.send(CobblenavPackets.REQUEST_PLAYER_STATS_PACKET, PacketByteBufs.create());
        int x = getBorderX() + BORDER_WIDTH - BORDER_DEPTH - 12;
        int y = getBorderY() + BORDER_DEPTH + 24;
        pieChart = new PieChartWidget(x, y, 25, ANIM_DURATION, GREEN, RED);
        statsTable = new TableWidget<>(x - 58, y + 56, 2, 0, new BorderBox(0, 1));
        backButton = new IconButton(getBorderX() + BORDER_DEPTH + 3, getBorderY() + BORDER_HEIGHT - BORDER_DEPTH - 12,
                11, 11, 73, 0, 0,
                () -> {
                    player.playSound(CobblemonSounds.PC_CLICK, 0.1f, 1.25f);
                    MinecraftClient.getInstance().setScreen(new MainScreen());
                }
        );
    }

    public void createStats(PlayerStats stats) {
        if (stats.totalPvp() != 0) {
            float winRatio = (float) stats.pvpWinnings() / (float) stats.totalPvp();
            pieChart.setRatio(winRatio);
        }
        //Extremely hardcode, it's worth coming up with something else
        List<AbstractTextWidget> textWidgets = new ArrayList<>(10);
        textWidgets.add(LINE_WIDGETS.get(0));
        textWidgets.add(new CrawlingLineWidget(Text.literal(String.valueOf(stats.totalPvp())), 0, 0, STAT_VALUE_WIDTH, 10, 0.6f, new BorderBox(0, 2), true));
        textWidgets.add(LINE_WIDGETS.get(1));
        textWidgets.add(new CrawlingLineWidget(Text.literal(String.valueOf(stats.pvpWinnings())), 0, 0, STAT_VALUE_WIDTH, 10, 0.6f, new BorderBox(2), true));
        textWidgets.add(LINE_WIDGETS.get(2));
        textWidgets.add(new CrawlingLineWidget(Text.literal(String.valueOf(stats.captures())), 0, 0, STAT_VALUE_WIDTH, 10, 0.6f, new BorderBox(2), true));
        textWidgets.add(LINE_WIDGETS.get(3));
        textWidgets.add(new CrawlingLineWidget(Text.literal(String.valueOf(stats.shinyCaptures())), 0, 0, STAT_VALUE_WIDTH, 10, 0.6f, new BorderBox(2), true));
        textWidgets.add(LINE_WIDGETS.get(4));
        textWidgets.add(new CrawlingLineWidget(Text.literal(String.valueOf(stats.evolutions())), 0, 0, STAT_VALUE_WIDTH, 10, 0.6f, new BorderBox(2), true));
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

        drawContext.fill(getBorderX() + BORDER_WIDTH - BORDER_DEPTH - 72, getBorderY() + BORDER_DEPTH + 20,
                getBorderX() + BORDER_WIDTH - BORDER_DEPTH, getBorderY() + BORDER_HEIGHT - BORDER_DEPTH,
                ColorHelper.Argb.getArgb(100, 0, 0, 0));

        if (stats != null) {
            pieChart.render(drawContext, i, j, f);
            statsTable.render(drawContext, i, j, f);
        }

        backButton.render(drawContext, i, j, f);

        super.render(drawContext, i, j, f);
    }

    @Override
    public boolean mouseClicked(double d, double e, int i) {
        backButton.mouseClicked(d, e, i);
        return super.mouseClicked(d, e, i);
    }
}
