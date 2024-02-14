package com.metacontent.cobblenav.client.screen.pokenav;

import com.cobblemon.mod.common.CobblemonSounds;
import com.cobblemon.mod.common.client.gui.summary.widgets.ModelWidget;
import com.cobblemon.mod.common.pokemon.RenderablePokemon;
import com.metacontent.cobblenav.Cobblenav;
import com.metacontent.cobblenav.client.CobblenavClient;
import com.metacontent.cobblenav.client.screen.AbstractPokenavItemScreen;
import com.metacontent.cobblenav.client.widget.PokenavItemButton;
import com.metacontent.cobblenav.networking.CobblenavPackets;
import com.metacontent.cobblenav.util.FoundPokemon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import static com.cobblemon.mod.common.api.gui.GuiUtilsKt.blitk;
import static com.cobblemon.mod.common.client.render.RenderHelperKt.drawScaledText;

@Environment(EnvType.CLIENT)
public class FinderScreen extends AbstractPokenavItemScreen {
    private static final Identifier FINDER_ASSETS = new Identifier(Cobblenav.ID, "textures/gui/pokenav_item_gui_finder_assets.png");
    private static final int HEIGHT = 80;
    private static final int WIDTH = 80;
    private static final int ANIM_DURATION = 40;

    private int borderX;
    private int borderY;
    private final RenderablePokemon pokemon;
    private final String bucket;
    private int ticker = 0;
    private boolean isLoading = true;
    private FoundPokemon foundPokemon;

    private ModelWidget pokemonModel;
    private PokenavItemButton backButton;
    private PokenavItemButton trackButton;

    public FinderScreen(RenderablePokemon pokemon, String bucket) {
        super(Text.literal("Finder"));
        this.pokemon = pokemon;
        this.bucket = bucket;
    }

    public FinderScreen(RenderablePokemon pokemon) {
        this(pokemon, null);
    }

    public void setFoundPokemon(@Nullable FoundPokemon foundPokemon) {
        this.foundPokemon = foundPokemon;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    private void requestBestPokemon() {
        PacketByteBuf buf = PacketByteBufs.create();
        if (pokemon.getForm().formOnlyShowdownId().equals("normal")) {
            buf.writeString(pokemon.getSpecies().showdownId());
        }
        else {
            buf.writeString(pokemon.getForm().showdownId());
        }
        ClientPlayNetworking.send(CobblenavPackets.BEST_POKEMON_PACKET_SERVER, buf);
    }

    private void saveLastFoundPokemon() {
        PacketByteBuf buf = PacketByteBufs.create();
        pokemon.saveToBuffer(buf);
        ClientPlayNetworking.send(CobblenavPackets.SAVE_FOUND_POKEMON_PACKET, buf);
    }

    @Override
    protected void init() {
        super.init();

        requestBestPokemon();

        borderX = (width - BORDER_WIDTH) / 2;
        borderY = (height - BORDER_HEIGHT) / 2 - 10;

        pokemonModel = new ModelWidget(borderX + BORDER_WIDTH / 2 - WIDTH / 2, borderY + BORDER_HEIGHT / 2 + 20 - HEIGHT / 2,
                WIDTH, HEIGHT, pokemon, 1.5f, 340, 0);

        backButton = new PokenavItemButton(borderX + BORDER_DEPTH + 3, borderY + BORDER_HEIGHT - BORDER_DEPTH - 12, 11, 11, 73, 0, 0, 0,
                Text.literal(""),
                BUTTONS,
                BUTTONS_HOVERED,
                () -> {
                    player.playSound(CobblemonSounds.PC_CLICK, 0.1f, 1.25f);
                    MinecraftClient.getInstance().setScreen(bucket != null ? new LocationScreen(bucket) : new MainScreen());
                }
        );
        trackButton = new PokenavItemButton(borderX + BORDER_WIDTH / 2 - 34, borderY + BORDER_HEIGHT - BORDER_DEPTH - 27, 70, 23, 0, 80, 0, 0,
                Text.translatable("gui.cobblenav.pokenav_item.track_button").setStyle(Style.EMPTY.withBold(true)),
                BUTTONS,
                BUTTONS_HOVERED,
                () -> {
                    saveLastFoundPokemon();
                    player.playSound(CobblemonSounds.POKE_BALL_CAPTURE_SUCCEEDED, 0.5f, 0.75f);
                    CobblenavClient.TRACK_ARROW_HUD_OVERLAY.setTrackedEntityId(foundPokemon.getEntityId());
                    close();
                }
        );
    }

    @Override
    public void render(DrawContext drawContext, int i, int j, float f) {
        renderBackground(drawContext);

        MatrixStack matrixStack = drawContext.getMatrices();

        blitk(matrixStack, BORDERS,
                borderX, borderY + BORDER_HEIGHT - BORDER_DEPTH - 14, 14, BORDER_WIDTH, 0, BORDER_HEIGHT + 1, 256,
                256, 0, 1,1,1,1,false,1);

        backButton.render(drawContext, i, j, f);

        renderBackgroundImage(matrixStack);

        if (!isLoading) {
            if (foundPokemon != null) {
                trackButton.render(drawContext, i, j, f);

                blitk(matrixStack, FINDER_ASSETS,
                        borderX + BORDER_DEPTH, borderY + BORDER_DEPTH + 20, 116, 210, 0, 0, 256,
                        256, 0, 1, 1, 1, 1, false, 1);

                pokemonModel.render(drawContext, i, j, f);

                int starsAmount = foundPokemon.getPotentialStarsAmount();

                for (int starIndex = 0; starIndex < starsAmount; starIndex++) {
                    blitk(matrixStack, FINDER_ASSETS,
                            borderX + BORDER_WIDTH - BORDER_DEPTH - 70 + 8 * starIndex, borderY + BORDER_DEPTH + 28, 20, 20, 0, 233, 256,
                            256, 0,
                            0.5 + 0.25 * starsAmount,
                            0.25 + 0.25 * starsAmount,
                            0.3,
                            1, false, 1);
                }

                renderAbilityName(drawContext, i, j);
                renderLevel(drawContext, i, j);
                renderEggMoveName(drawContext, i, j);

                if (ticker < ANIM_DURATION) {
                    ticker++;
                }
            }
            else {
                drawScaledText(drawContext, FONT, Text.translatable("gui.cobblenav.pokenav_item.not_found_message"),
                        borderX + BORDER_WIDTH / 2, borderY + BORDER_HEIGHT / 2, 1, 1, BORDER_WIDTH, 0xFFFFFF, true, true, i, j);
            }
        }

        super.render(drawContext, i, j, f);
    }

    private void renderBackgroundImage(MatrixStack matrixStack) {
        blitk(matrixStack, FINDER_ASSETS,
                borderX + BORDER_DEPTH, borderY + BORDER_DEPTH + 20, 116, 210, 0, 116, 256,
                256, 0, 1, 1, 1, 1, false, 1);
    }

    private void renderAbilityName(DrawContext drawContext, int i, int j) {
        drawScaledText(drawContext, FONT, Text.translatable("cobblemon.ui.info.ability").setStyle(Style.EMPTY.withBold(true)),
                borderX + BORDER_WIDTH - BORDER_DEPTH - 53, borderY + BORDER_HEIGHT - BORDER_DEPTH - 38, 1f, 1, 40, 0xFFFFFF, false, true, i, j);

        if (foundPokemon.isAbilityHidden()) {
            blitk(drawContext.getMatrices(), FINDER_ASSETS,
                    borderX + BORDER_WIDTH - BORDER_DEPTH - 14, borderY + BORDER_HEIGHT - BORDER_DEPTH - 39, 9, 9, 21, 233, 256,
                    256, 0, 1, 1, 1, 1, false, 1);
        }

        drawScaledText(drawContext, FONT, Text.translatable("cobblemon.ability." + foundPokemon.getAbilityName()),
                borderX + BORDER_WIDTH - BORDER_DEPTH - 53, borderY + BORDER_HEIGHT - BORDER_DEPTH - 29, 1f, 1, 47, 0xFFFFFF, false, true, i, j);
    }

    private void renderLevel(DrawContext drawContext, int i, int j) {
        drawScaledText(drawContext, FONT, Text.literal(pokemon.getSpecies().getTranslatedName().getString() + " " + Text.translatable("gui.cobblenav.pokenav_item.level_value", foundPokemon.getLevel()).getString()),
                borderX + BORDER_DEPTH + 38, borderY + BORDER_DEPTH + 27, 1f, 1, 55, 0xFFFFFF, true, true, i, j);
    }

    private void renderEggMoveName(DrawContext drawContext, int i, int j) {
        boolean hasEggMove = !foundPokemon.getEggMoveName().isEmpty();

        drawScaledText(drawContext, FONT, Text.translatable("gui.cobblenav.pokenav_item.egg_move").setStyle(Style.EMPTY.withBold(true)),
                borderX + BORDER_DEPTH + 8, borderY + BORDER_HEIGHT - BORDER_DEPTH - 38, 1f, 1, 40, 0xFFFFFF, false, true, i, j);

        if (hasEggMove) {
            blitk(drawContext.getMatrices(), FINDER_ASSETS,
                    borderX + BORDER_DEPTH + 50, borderY + BORDER_HEIGHT - BORDER_DEPTH - 39, 9, 9, 21, 233, 256,
                    256, 0, 1, 1, 1, 1, false, 1);
        }

        drawScaledText(drawContext, FONT, Text.translatable(hasEggMove ? "cobblemon.move." + foundPokemon.getEggMoveName() : "gui.cobblenav.pokenav_item.egg_move_none"),
                borderX + BORDER_DEPTH + 8, borderY + BORDER_HEIGHT - BORDER_DEPTH - 29, 1f, 1, 47, 0xFFFFFF, false, true, i, j);
    }

    @Override
    public boolean mouseClicked(double d, double e, int i) {
        backButton.mouseClicked(d, e, i);
        trackButton.mouseClicked(d, e, i);
        return super.mouseClicked(d, e, i);
    }
}
