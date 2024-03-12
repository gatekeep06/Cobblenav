package com.metacontent.cobblenav.client.screen.pokenav;

import com.cobblemon.mod.common.CobblemonSounds;
import com.cobblemon.mod.common.pokemon.RenderablePokemon;
import com.metacontent.cobblenav.Cobblenav;
import com.metacontent.cobblenav.client.screen.AbstractPokenavItemScreen;
import com.metacontent.cobblenav.client.widget.PokemonSpawnInfoWidget;
import com.metacontent.cobblenav.client.widget.PokenavItemButton;
import com.metacontent.cobblenav.client.widget.ScrollableViewWidget;
import com.metacontent.cobblenav.client.widget.TableWidget;
import com.metacontent.cobblenav.networking.CobblenavPackets;
import com.metacontent.cobblenav.util.BorderBox;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.cobblemon.mod.common.api.gui.GuiUtilsKt.blitk;
import static com.cobblemon.mod.common.client.render.RenderHelperKt.drawScaledText;

@Environment(EnvType.CLIENT)
public class LocationScreen extends AbstractPokenavItemScreen {
    public static final List<String> BUCKET_NAMES = List.of("common", "uncommon", "rare", "ultra-rare");
    private static final Identifier LOADING_ANIMATION = new Identifier(Cobblenav.ID, "textures/gui/loading_animation.png");
    private final PlayerEntity player;
    private Map<RenderablePokemon, Float> spawnMap = new HashMap<>();
    private List<PokemonSpawnInfoWidget> spawnInfoWidgets = new ArrayList<>();
    private TableWidget<PokemonSpawnInfoWidget> spawnTable;
    private ScrollableViewWidget<TableWidget<PokemonSpawnInfoWidget>> scrollableSpawnTable;
    private int bucketIndex = 0;
    private boolean isLoading = false;
    private int ticker = 0;
    private int animProgress = 0;
    private int sortingMark = 1;
    private int listPage;
    private int borderX;
    private int borderY;

    private PokenavItemButton backButton;
    private PokenavItemButton refreshButton;
    private PokenavItemButton decreaseBucketIndexButton;
    private PokenavItemButton increaseBucketIndexButton;
    private PokenavItemButton decreaseListPageButton;
    private PokenavItemButton increaseListPageButton;
    private PokenavItemButton reverseSortingButton;

    protected LocationScreen(@Nullable String bucket) {
        super(Text.literal("Location"));
        this.player = MinecraftClient.getInstance().player;
        if (bucket != null) {
            this.bucketIndex = BUCKET_NAMES.indexOf(bucket);
        }
    }

    private void checkSpawns() {
        listPage = 0;
        spawnMap.clear();
        isLoading = true;
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(bucketIndex);
        ClientPlayNetworking.send(CobblenavPackets.SPAWN_MAP_PACKET_SERVER, buf);
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public void resetAnimationProgress() {
        ticker = 0;
        animProgress = 0;
    }

    public void createSpawnInfoWidgets() {
        spawnInfoWidgets = new ArrayList<>();
        RenderablePokemon[] renderablePokemonArray = spawnMap.keySet().toArray(new RenderablePokemon[0]);
        Float[] probabilityArray = spawnMap.values().toArray(new Float[0]);
        for (int i = 0; i < spawnMap.size(); i++) {
            PokemonSpawnInfoWidget widget = new PokemonSpawnInfoWidget(0, 0, renderablePokemonArray[i], probabilityArray[i], BUCKET_NAMES.get(bucketIndex));
            spawnInfoWidgets.add(widget);
        }
        scrollableSpawnTable.resetScrollY();
        spawnTable.calcRows(spawnInfoWidgets.size());
        spawnTable.setWidgets(spawnInfoWidgets);
    }

    public void setSpawnMap(Map<RenderablePokemon, Float> spawnMap) {
        this.spawnMap = spawnMap;
    }

    @Override
    protected void init() {
        super.init();

        checkSpawns();

        borderX = (width - BORDER_WIDTH) / 2;
        borderY = (height - BORDER_HEIGHT) / 2 - 10;

        spawnTable = new TableWidget<>(borderX + BORDER_DEPTH + 3, borderY + BORDER_DEPTH + 30,
                7, 3, new BorderBox(4, 2));

        scrollableSpawnTable = new ScrollableViewWidget<>(spawnTable, 200, 106, 20);

        backButton = new PokenavItemButton(borderX + BORDER_DEPTH + 3, borderY + BORDER_HEIGHT - BORDER_DEPTH - 12, 11, 11, 73, 0, 0, 0,
                Text.literal(""),
                BUTTONS,
                BUTTONS_HOVERED,
                () -> {
                    player.playSound(CobblemonSounds.PC_CLICK, 0.1f, 1.25f);
                    MinecraftClient.getInstance().setScreen(new MainScreen());
                }
        );
        refreshButton = new PokenavItemButton(borderX + BORDER_DEPTH + 18, borderY + BORDER_HEIGHT - BORDER_DEPTH - 12, 11, 11, 85, 0, 0, 400,
                Text.literal(""),
                BUTTONS,
                BUTTONS_HOVERED,
                () -> {
                    player.playSound(CobblemonSounds.PC_CLICK, 0.1f, 1.25f);
                    checkSpawns();
                }
        );
        decreaseBucketIndexButton = new PokenavItemButton(width / 2 - 25, borderY + BORDER_DEPTH + 22, 5, 7, 97, 0, 0, 0,
                Text.literal(""),
                BUTTONS,
                BUTTONS_HOVERED,
                () -> {
                    player.playSound(CobblemonSounds.PC_CLICK, 0.05f, 1.25f);
                    if (bucketIndex - 1 >= 0) {
                        bucketIndex--;
                        checkSpawns();
                    }
                }
        );
        increaseBucketIndexButton = new PokenavItemButton(width / 2 + 25, borderY + BORDER_DEPTH + 22, 5, 7, 102, 0, 0, 0,
                Text.literal(""),
                BUTTONS,
                BUTTONS_HOVERED,
                () -> {
                    player.playSound(CobblemonSounds.PC_CLICK, 0.05f, 1.25f);
                    if (bucketIndex + 1 < BUCKET_NAMES.size()) {
                        bucketIndex++;
                        checkSpawns();
                    }
                }
        );
        decreaseListPageButton = new PokenavItemButton(borderX + BORDER_WIDTH - BORDER_DEPTH - 7, height / 2 - 20, 7, 5, 114, 0, 0, 0,
                Text.literal(""),
                BUTTONS,
                BUTTONS_HOVERED,
                () -> {
                    player.playSound(CobblemonSounds.PC_CLICK, 0.05f, 1.25f);
                    if (listPage - 1 >= 0) {
                        listPage--;
                        createSpawnInfoWidgets();
                    }
                }
        );
        increaseListPageButton = new PokenavItemButton(borderX + BORDER_WIDTH - BORDER_DEPTH - 7, height / 2 + 20, 7, 5, 107, 0, 0, 0,
                Text.literal(""),
                BUTTONS,
                BUTTONS_HOVERED,
                () -> {
                    player.playSound(CobblemonSounds.PC_CLICK, 0.05f, 1.25f);
                    int pagesAmount = spawnMap.size() % 21 > 0 ? spawnMap.size() / 21 + 1 : spawnMap.size() / 21;
                    if (listPage + 1 < pagesAmount) {
                        listPage++;
                        createSpawnInfoWidgets();
                    }
                }
        );
        reverseSortingButton = new PokenavItemButton(borderX + BORDER_WIDTH - BORDER_DEPTH - 14, borderY + BORDER_HEIGHT - BORDER_DEPTH - 12, 11, 11, 97, 12, 0, 100,
                Text.literal(""),
                BUTTONS,
                BUTTONS_HOVERED,
                () -> {
                    player.playSound(CobblemonSounds.PC_CLICK, 0.05f, 1.25f);
                    if (!spawnMap.isEmpty()) {
                        sortingMark = -sortingMark;
                        List<Map.Entry<RenderablePokemon, Float>> sortingList = new ArrayList<>(spawnMap.entrySet());
                        Comparator<Map.Entry<RenderablePokemon, Float>> comparator = Map.Entry.comparingByValue(
                                (c1, c2) -> sortingMark * Float.compare(c1, c2)
                        );
                        sortingList.sort(comparator);
                        spawnMap.clear();
                        sortingList.forEach(entry -> spawnMap.put(entry.getKey(), entry.getValue()));
                        createSpawnInfoWidgets();
                    }
                }
        );
    }

    @Override
    public void render(DrawContext drawContext, int i, int j, float f) {
        renderBackground(drawContext);

        MatrixStack matrixStack = drawContext.getMatrices();

        blitk(matrixStack, BACKGROUND,
                borderX + BORDER_DEPTH, borderY + BORDER_DEPTH + 20, BORDER_HEIGHT - 2 * BORDER_DEPTH - 32, BORDER_WIDTH - 2 * BORDER_DEPTH, 0, 131, 256,
                256, 0, 1, 1, 1, 1, false, 1);

        blitk(matrixStack, BORDERS,
                borderX, borderY + BORDER_HEIGHT - BORDER_DEPTH - 14, 14, BORDER_WIDTH, 0, BORDER_HEIGHT + 1, 256,
                256, 0, 1,1,1,1,false,1);

        renderBucketSelector(drawContext, i, j, f);
        renderPageSelector(drawContext, i, j, f);

        backButton.render(drawContext, i, j, f);
        refreshButton.render(drawContext, i, j, f);
        reverseSortingButton.render(drawContext, i, j, f);

        if (isLoading) {
            renderLoadingAnimation(drawContext, i, j, f);
        }
        else {
            if (!spawnInfoWidgets.isEmpty()) {
                try {
                    scrollableSpawnTable.render(drawContext, i, j, f);
                }
                catch (Throwable e) {
                    Cobblenav.LOGGER.error(e.getMessage(), e);
                }
            }
            else {
                drawScaledText(drawContext, FONT, Text.translatable("gui.cobblenav.pokenav_item.empty_spawns_message.part_1"),
                        borderX + BORDER_WIDTH / 2, borderY + BORDER_HEIGHT / 2, 1, 1, BORDER_WIDTH, 0xFFFFFF, true, false, i, j);
                drawScaledText(drawContext, FONT, Text.translatable("gui.cobblenav.pokenav_item.empty_spawns_message.part_2"),
                        borderX + BORDER_WIDTH / 2, borderY + BORDER_HEIGHT / 2 + 8, 1, 1, BORDER_WIDTH / 2, 0xFFFFFF, true, false, i, j);
            }
        }

        super.render(drawContext, i, j, f);
    }

    @Override
    public boolean mouseClicked(double d, double e, int i) {
        backButton.mouseClicked(d, e, i);
        refreshButton.mouseClicked(d, e, i);
        decreaseBucketIndexButton.mouseClicked(d, e, i);
        increaseBucketIndexButton.mouseClicked(d, e, i);
        decreaseListPageButton.mouseClicked(d, e, i);
        increaseListPageButton.mouseClicked(d, e, i);
        scrollableSpawnTable.mouseClicked(d, e, i);
        reverseSortingButton.mouseClicked(d, e, i);
        return super.mouseClicked(d, e, i);
    }

    @Override
    public boolean mouseScrolled(double d, double e, double f) {
        scrollableSpawnTable.mouseScrolled(d, e, f);
        return super.mouseScrolled(d, e, f);
    }

    private void renderBucketSelector(DrawContext drawContext, int i, int j, float f) {
        decreaseBucketIndexButton.render(drawContext, i, j, f);
        drawScaledText(drawContext, FONT, Text.translatable("gui.cobblenav.pokenav_item.bucket." + BUCKET_NAMES.get(bucketIndex)).setStyle(Style.EMPTY.withBold(true).withColor(0xFFFFFF)),
                width / 2 + 3, borderY + BORDER_DEPTH + 20, 1, 1, 40, 0, true, false, i, j);
        increaseBucketIndexButton.render(drawContext, i, j, f);
    }

    private void renderPageSelector(DrawContext drawContext, int i, int j, float f) {
        decreaseListPageButton.render(drawContext, i, j, f);
        drawScaledText(drawContext, FONT, Text.literal(String.valueOf(listPage + 1)).setStyle(Style.EMPTY.withBold(true).withColor(0xFFFFFF)),
                borderX + BORDER_WIDTH - BORDER_DEPTH - 4, height / 2 - 3, 1, 1, 5, 0, true, false, i, j);
        increaseListPageButton.render(drawContext, i, j, f);
    }

    private void renderLoadingAnimation(DrawContext drawContext, int i, int j, float f) {
        MatrixStack matrixStack = drawContext.getMatrices();
        if (ticker % 10 == 0) {
            animProgress++;
        }
        blitk(matrixStack, LOADING_ANIMATION,
                borderX + BORDER_WIDTH / 2 - 9, borderY + BORDER_HEIGHT / 2, 22, 18, 18 * animProgress, 0, 256,
                256, 0,1, 1, 1, 1, false, 1);
        if (animProgress == 8) {
            resetAnimationProgress();
        }
        ticker++;
    }

    public int getBucketIndex() {
        return bucketIndex;
    }

    public int getSortingMark() {
        return sortingMark;
    }
}
