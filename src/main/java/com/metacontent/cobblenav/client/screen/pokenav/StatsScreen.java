package com.metacontent.cobblenav.client.screen.pokenav;

import com.cobblemon.mod.common.CobblemonSounds;
import com.cobblemon.mod.common.client.CobblemonClient;
import com.cobblemon.mod.common.client.storage.ClientPC;
import com.cobblemon.mod.common.client.storage.ClientParty;
import com.cobblemon.mod.common.client.storage.ClientStorageManager;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.metacontent.cobblenav.client.screen.AbstractPokenavItemScreen;
import com.metacontent.cobblenav.client.widget.*;
import com.metacontent.cobblenav.client.widget.main_screen.PartyWidget;
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
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.math.ColorHelper;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.cobblemon.mod.common.api.gui.GuiUtilsKt.blitk;

public class StatsScreen extends AbstractPokenavItemScreen {
    private static final int RED = ColorHelper.Argb.getArgb(255, 219, 67, 76);
    private static final int GREEN = ColorHelper.Argb.getArgb(255, 82, 214, 48);
    private static final int ANIM_DURATION = 50;
    private static final int STAT_NAME_WIDTH = 52;
    private static final int STAT_VALUE_WIDTH = 18;
    private final List<AbstractTextWidget> LINE_WIDGETS = new ArrayList<>(List.of(
            new CrawlingLineWidget(Text.translatable("gui.cobblenav.pokenav_item.total_pvp"), 0, 0, STAT_NAME_WIDTH, 10, 0.6f, new BorderBox(2), true),
            new CrawlingLineWidget(Text.translatable("gui.cobblenav.pokenav_item.pvp_winnings"), 0, 0, STAT_NAME_WIDTH, 10, 0.6f, new BorderBox(2), true),
            new CrawlingLineWidget(Text.translatable("gui.cobblenav.pokenav_item.captures"), 0, 0, STAT_NAME_WIDTH, 10, 0.6f, new BorderBox(2), true),
            new CrawlingLineWidget(Text.translatable("gui.cobblenav.pokenav_item.shiny_captures"), 0, 0, STAT_NAME_WIDTH, 10, 0.6f, new BorderBox(2), true),
            new CrawlingLineWidget(Text.translatable("gui.cobblenav.pokenav_item.evolutions"), 0, 0, STAT_NAME_WIDTH, 10, 0.6f, new BorderBox(2), true)));

    private PlayerStats stats;
    private PieChartWidget pieChart;
    private TableWidget<AbstractTextWidget> statsTable;
    private TextWidget startDateWidget;
    private PartyWidget favoritePokemonWidget;
    private TextWidget favoritePokemonUsageWidget;
    private BadgeDisplayWidget badgeDisplay;
    private IconButton backButton;

    protected StatsScreen() {
        super(Text.literal("Stats"));
    }

    @Override
    protected void init() {
        super.init();
        int x = getBorderX() + BORDER_WIDTH - BORDER_DEPTH - 12;
        int y = getBorderY() + BORDER_DEPTH + 24;
        pieChart = new PieChartWidget(x, y, 25, ANIM_DURATION, GREEN, RED);
        statsTable = new TableWidget<>(x - 58, y + 56, 2, 0, new BorderBox(0, 1));
        startDateWidget = new TextWidget((int) ((x - 58) / 0.5f), (int) ((getBorderY() + BORDER_HEIGHT - BORDER_DEPTH - 5) / 0.5f),
                50, 10, Text.empty(), textRenderer).alignLeft();
        favoritePokemonWidget = new PartyWidget(getBorderX() + BORDER_DEPTH + 50, getBorderY() + BORDER_HEIGHT - BORDER_DEPTH - 105,
                getBorderX(), getBorderY(), 1.6f, List.of());
        favoritePokemonUsageWidget = new TextWidget(0, 0, 50, 10, Text.empty(), textRenderer).alignLeft();
        badgeDisplay = new BadgeDisplayWidget(getBorderX() + BORDER_DEPTH + 3, getBorderY() + BORDER_DEPTH + 23);
        backButton = new IconButton(getBorderX() + BORDER_DEPTH + 3, getBorderY() + BORDER_HEIGHT - BORDER_DEPTH - 12,
                11, 11, 73, 0, Text.translatable("gui.cobblenav.pokenav_item.button_tooltip.back"),
                () -> {
                    player.playSound(CobblemonSounds.PC_CLICK, 0.1f, 1.25f);
                    MinecraftClient.getInstance().setScreen(new MainScreen());
                }
        );
        ClientPlayNetworking.send(CobblenavPackets.PLAYER_STATS_REQUEST_PACKET, PacketByteBufs.create());
    }

    public void createStatsDisplay(PlayerStats stats, Set<String> badges) {
        if (stats.totalPvp() != 0) {
            float winRatio = (float) stats.pvpWinnings() / (float) stats.totalPvp();
            pieChart.setRatio(winRatio);
        }

        //Extremely hardcoded, it's worth coming up with something else
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

        startDateWidget.setMessage(Text.translatable("gui.cobblenav.pokenav_item.start_date", stats.startDate().toInstant().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_LOCAL_DATE)));

        stats.pokemonUsage().entrySet().stream().max(Map.Entry.comparingByValue()).ifPresent(entry -> {
            int favoritePokemonUsage = entry.getValue();
            favoritePokemonUsageWidget.setMessage(Text.literal("Usage: " + favoritePokemonUsage));

            UUID favoritePokemonUuid = entry.getKey();
            ClientStorageManager storageManager = CobblemonClient.INSTANCE.getStorage();
            Pokemon favoritePokemon = null;
            for (ClientParty party : storageManager.getPartyStores().values()) {
                favoritePokemon = party.findByUUID(favoritePokemonUuid);
                if (favoritePokemon != null) {
                    break;
                }
            }
            if (favoritePokemon == null) {
                for (ClientPC pc : storageManager.getPcStores().values()) {
                    favoritePokemon = pc.findByUUID(favoritePokemonUuid);
                    if (favoritePokemon != null) {
                        break;
                    }
                }
            }
            if (favoritePokemon != null) {
                favoritePokemonWidget.createPartyModels(List.of(favoritePokemon));
            }

        });

        badgeDisplay.setBadges(badges, stats.grantedBadges());

        this.stats = stats;
    }

    @Override
    public void renderScreen(DrawContext drawContext, int i, int j, float f) {
        MatrixStack matrixStack = drawContext.getMatrices();

        blitk(matrixStack, BACKGROUND,
                getBorderX() + BORDER_DEPTH, getBorderY() + BORDER_DEPTH + 20, BORDER_HEIGHT - 2 * BORDER_DEPTH - 20, BORDER_WIDTH - 2 * BORDER_DEPTH, 0, 0, 256,
                256, 0, 1, 1, 1, 1, false, 1);

        if (stats != null) {
            favoritePokemonWidget.render(drawContext, i, j, f);

            matrixStack.push();
            matrixStack.translate(0f, 0f, 3000f);
            drawContext.fill(getBorderX() + BORDER_WIDTH - BORDER_DEPTH - 72, getBorderY() + BORDER_DEPTH + 20,
                    getBorderX() + BORDER_WIDTH - BORDER_DEPTH, getBorderY() + BORDER_HEIGHT - BORDER_DEPTH,
                    ColorHelper.Argb.getArgb(100, 0, 0, 0));
            pieChart.render(drawContext, i, j, f);
            statsTable.render(drawContext, i, j, f);

            badgeDisplay.render(drawContext, i, j, f);

            matrixStack.push();
            matrixStack.scale(0.5f, 0.5f, 1f);
            startDateWidget.render(drawContext, i, j, f);
            matrixStack.pop();
            matrixStack.pop();
        }

        backButton.render(drawContext, i, j, f);
    }

    @Override
    public void onMouseClicked(double d, double e, int i) {
        backButton.mouseClicked(d, e, i);
    }

    @Override
    public void onMouseDragged(double d, double e, int i, double f, double g) {

    }

    @Override
    public void onMouseScrolled(double d, double e, double f) {

    }

    @Override
    public MutableText getTitle() {
        return Text.literal(player.getEntityName());
    }
}
