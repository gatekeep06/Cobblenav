package com.metacontent.cobblenav.client.screen.pokenav;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.CobblemonSounds;
import com.cobblemon.mod.common.api.spawning.SpawnBucket;
import com.cobblemon.mod.common.pokemon.RenderablePokemon;
import com.metacontent.cobblenav.Cobblenav;
import com.metacontent.cobblenav.client.CobblenavClient;
import com.metacontent.cobblenav.client.screen.AbstractPokenavItemScreen;
import com.metacontent.cobblenav.client.widget.*;
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

import java.util.*;

import static com.cobblemon.mod.common.api.gui.GuiUtilsKt.blitk;
import static com.cobblemon.mod.common.client.render.RenderHelperKt.drawScaledText;

@Environment(EnvType.CLIENT)
public class LocationScreen extends AbstractPokenavItemScreen {
    public final List<SpawnBucket> buckets;
    private static final Identifier LOADING_ANIMATION = new Identifier(Cobblenav.ID, "textures/gui/loading_animation.png");
    private final PlayerEntity player;
    private Map<RenderablePokemon, Float> spawnMap = new HashMap<>();
    private List<PokemonSpawnInfoWidget> spawnInfoWidgets = new ArrayList<>();
    private TableWidget<PokemonSpawnInfoWidget> spawnTable;
    private ScrollableViewWidget<TableWidget<PokemonSpawnInfoWidget>> scrollableSpawnTable;
    private int bucketIndex = -1;
    private boolean isLoading = false;
    private int ticker = 0;
    private int animProgress = 0;
    private int sortingMark = 0;
    private int borderX;
    private int borderY;

    private IconButton backButton;
    private IconButton refreshButton;
    private IconButton decreaseBucketIndexButton;
    private IconButton increaseBucketIndexButton;
    private IconButton reverseSortingButton;

    public LocationScreen() {
        super(Text.literal("Location"));
        this.player = MinecraftClient.getInstance().player;
        this.buckets = Cobblemon.INSTANCE.getBestSpawner().getConfig().getBuckets();
    }

    private void requestSavedPreferences() {
        spawnMap.clear();
        isLoading = true;
        ClientPlayNetworking.send(CobblenavPackets.SAVED_PREFERENCES_REQUEST_PACKET, PacketByteBufs.create());
    }

    public void setPreferences(int bucketIndex, int sortingMark) {
        this.bucketIndex = Math.max(bucketIndex, 0);
        this.sortingMark = sortingMark == 0 ? 1 : sortingMark;
    }

    public void checkSpawns() {
        spawnMap.clear();
        isLoading = true;
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeString(buckets.get(bucketIndex).getName());
        ClientPlayNetworking.send(CobblenavPackets.SPAWN_MAP_REQUEST_PACKET, buf);
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public void resetAnimationProgress() {
        ticker = 0;
        animProgress = 0;
    }

    public void createSpawnInfoWidgets() {
        int y = borderY + BORDER_DEPTH + 30;
        spawnTable = new TableWidget<>(borderX + BORDER_DEPTH + 3, y,
                7, 3, new BorderBox(4, 2));
        scrollableSpawnTable = new ScrollableViewWidget<>(spawnTable, 200, 106, CobblenavClient.CONFIG.scrollSize);
        spawnInfoWidgets = new ArrayList<>();
        RenderablePokemon[] renderablePokemonArray = spawnMap.keySet().toArray(new RenderablePokemon[0]);
        Float[] probabilityArray = spawnMap.values().toArray(new Float[0]);
        for (int i = 0; i < spawnMap.size(); i++) {
            PokemonSpawnInfoWidget widget = new PokemonSpawnInfoWidget(0, 0, renderablePokemonArray[i], probabilityArray[i], this, y - 30, y + 106);
            spawnInfoWidgets.add(widget);
        }
        spawnTable.calcRows(spawnInfoWidgets.size());
        spawnTable.setWidgets(spawnInfoWidgets);
    }

    public void setSpawnMap(Map<RenderablePokemon, Float> spawnMap) {
        this.spawnMap = spawnMap;
    }

    @Override
    protected void init() {
        super.init();

        borderX = (width - BORDER_WIDTH) / 2;
        borderY = (height - BORDER_HEIGHT) / 2 - 10;

        if (sortingMark == 0 || bucketIndex == -1) {
            requestSavedPreferences();
        }
        else {
            checkSpawns();
        }

        backButton = new IconButton(borderX + BORDER_DEPTH + 3, borderY + BORDER_HEIGHT - BORDER_DEPTH - 12,
                11, 11, 73, 0, Text.translatable("gui.cobblenav.pokenav_item.button_tooltip.back"),
                () -> {
                    player.playSound(CobblemonSounds.PC_CLICK, 0.1f, 1.25f);
                    savePreferences();
                    MinecraftClient.getInstance().setScreen(new MainScreen());
                }
        );
        refreshButton = new IconButton(borderX + BORDER_DEPTH + 18, borderY + BORDER_HEIGHT - BORDER_DEPTH - 12,
                11, 11, 85, 0, 400, Text.translatable("gui.cobblenav.pokenav_item.button_tooltip.refresh"),
                () -> {
                    player.playSound(CobblemonSounds.PC_CLICK, 0.1f, 1.25f);
                    checkSpawns();
                }
        );
        decreaseBucketIndexButton = new IconButton(width / 2 - 25, borderY + BORDER_DEPTH + 22, 5, 7, 97, 0, null,
                this::decreaseBucket
        );
        increaseBucketIndexButton = new IconButton(width / 2 + 25, borderY + BORDER_DEPTH + 22, 5, 7, 102, 0, null,
                this::increaseBucket
        );
        reverseSortingButton = new IconButton(borderX + BORDER_WIDTH - BORDER_DEPTH - 14, borderY + BORDER_HEIGHT - BORDER_DEPTH - 12,
                11, 11, 97, 12, CobblenavClient.CONFIG.reverseSortingButtonCooldown, Text.translatable("gui.cobblenav.pokenav_item.button_tooltip.reverse"),
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
    public void renderScreen(DrawContext drawContext, int i, int j, float f) {
        MatrixStack matrixStack = drawContext.getMatrices();

        blitk(matrixStack, BACKGROUND,
                borderX + BORDER_DEPTH, borderY + BORDER_DEPTH + 20, BORDER_HEIGHT - 2 * BORDER_DEPTH - 32, BORDER_WIDTH - 2 * BORDER_DEPTH, 0, 131, 256,
                256, 0, 1, 1, 1, 1, false, 1);

        blitk(matrixStack, BORDERS,
                borderX, borderY + BORDER_HEIGHT - BORDER_DEPTH - 14, 14, BORDER_WIDTH, 0, BORDER_HEIGHT + 1, 256,
                256, 0, 1,1,1,1,false,1);

        backButton.render(drawContext, i, j, f);
        refreshButton.render(drawContext, i, j, f);
        reverseSortingButton.render(drawContext, i, j, f);

        if (isLoading) {
            renderLoadingAnimation(drawContext, i, j, f);
        }
        else {
            renderBucketSelector(drawContext, i, j, f);
            if (!spawnInfoWidgets.isEmpty()) {
                scrollableSpawnTable.render(drawContext, i, j, f);
                for (PokemonSpawnInfoWidget widget : spawnInfoWidgets) {
                    if (widget.isVisible() && widget.isHovered() && scrollableSpawnTable.isHovered()) {
                        drawContext.drawTooltip(textRenderer, List.of(widget.getPokemonModel().getPokemon().getSpecies().getTranslatedName(),
                                Text.literal(widget.getProbabilityString()).setStyle(Style.EMPTY.withColor(0x4D4D4D))), i, j);
                    }
                }
            }
            else {
                drawScaledText(drawContext, FONT, Text.translatable("gui.cobblenav.pokenav_item.empty_spawns_message.part_1"),
                        borderX + BORDER_WIDTH / 2, borderY + BORDER_HEIGHT / 2, 1, 1, BORDER_WIDTH, 0xFFFFFF, true, false, i, j);
                drawScaledText(drawContext, FONT, Text.translatable("gui.cobblenav.pokenav_item.empty_spawns_message.part_2"),
                        borderX + BORDER_WIDTH / 2, borderY + BORDER_HEIGHT / 2 + 8, 1, 1, BORDER_WIDTH / 2, 0xFFFFFF, true, false, i, j);
            }
        }
    }

    @Override
    public void onMouseClicked(double d, double e, int i) {
        backButton.mouseClicked(d, e, i);
        if (!isLoading) {
            refreshButton.mouseClicked(d, e, i);
            decreaseBucketIndexButton.mouseClicked(d, e, i);
            increaseBucketIndexButton.mouseClicked(d, e, i);
            scrollableSpawnTable.mouseClicked(d, e, i);
            reverseSortingButton.mouseClicked(d, e, i);
        }
    }

    @Override
    public void onMouseDragged(double d, double e, int i, double f, double g) {
        if (!isLoading) {
            scrollableSpawnTable.mouseDragged(d, e, i, f, g);
        }
    }

    @Override
    public void onMouseScrolled(double d, double e, double f) {
        if (!isLoading) {
            scrollableSpawnTable.mouseScrolled(d, e, f);
        }
    }

    private void renderBucketSelector(DrawContext drawContext, int i, int j, float f) {
        decreaseBucketIndexButton.render(drawContext, i, j, f);
        drawScaledText(drawContext, FONT, Text.translatable("gui.cobblenav.pokenav_item.bucket." + buckets.get(bucketIndex).getName()).setStyle(Style.EMPTY.withBold(true)),
                width / 2 + 3, borderY + BORDER_DEPTH + 20, 1, 1, 40, 0xFFFFFF, true, false, i, j);
        increaseBucketIndexButton.render(drawContext, i, j, f);
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

    public SpawnBucket getCurrentBucket() {
        return buckets.get(bucketIndex);
    }

    public int getSortingMark() {
        return sortingMark;
    }

    @Override
    public void close() {
        savePreferences();
        super.close();
    }

    public void savePreferences() {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(bucketIndex);
        buf.writeInt(sortingMark);
        ClientPlayNetworking.send(CobblenavPackets.SAVE_PREFERENCES_PACKET, buf);
    }

    public void decreaseBucket() {
        if (bucketIndex - 1 >= 0) {
            player.playSound(CobblemonSounds.PC_CLICK, 0.05f, 1.25f);
            bucketIndex--;
            checkSpawns();
        }
    }

    public void increaseBucket() {
        if (bucketIndex + 1 < buckets.size()) {
            player.playSound(CobblemonSounds.PC_CLICK, 0.05f, 1.25f);
            bucketIndex++;
            checkSpawns();
        }
    }

    @Override
    public boolean keyPressed(int i, int j, int k) {
        if (CobblenavClient.increaseBucketKey.matchesKey(i, j)) {
            increaseBucket();
        }
        else if (CobblenavClient.decreaseBucketKey.matchesKey(i, j)) {
            decreaseBucket();
        }
        return super.keyPressed(i, j, k);
    }
}
