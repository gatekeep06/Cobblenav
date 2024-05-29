package com.metacontent.cobblenav.client.screen.pokenav;

import com.cobblemon.mod.common.CobblemonSounds;
import com.cobblemon.mod.common.pokemon.RenderablePokemon;
import com.metacontent.cobblenav.Cobblenav;
import com.metacontent.cobblenav.client.CobblenavClient;
import com.metacontent.cobblenav.client.screen.AbstractPokenavItemScreen;
import com.metacontent.cobblenav.client.widget.FinderShortcutWidget;
import com.metacontent.cobblenav.client.widget.MainScreenButton;
import com.metacontent.cobblenav.client.widget.main_screen.MainScreenWidget;
import com.metacontent.cobblenav.client.widget.main_screen.PartyWidget;
import com.metacontent.cobblenav.networking.CobblenavPackets;
import com.metacontent.cobblenav.config.util.MainScreenWidgetType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

import static com.cobblemon.mod.common.api.gui.GuiUtilsKt.blitk;
import static com.cobblemon.mod.common.client.render.RenderHelperKt.drawScaledText;

@Environment(EnvType.CLIENT)
public class MainScreen extends AbstractPokenavItemScreen {
    private static final Identifier BUTTONS = new Identifier(Cobblenav.ID, "textures/gui/pokenav_item_gui_buttons.png");
    private int borderX;
    private int borderY;
    private int playerX;
    private int playerY;
    private MainScreenButton closeButton;
    private MainScreenButton contactsButton;
    private MainScreenButton spawnCheckButton;
    private FinderShortcutWidget finderShortcutWidget = null;
    private MainScreenWidget mainScreenWidget;

    public MainScreen() {
        super(Text.literal("Pokenav"));
    }

    private void requestLastFoundPokemon() {
        ClientPlayNetworking.send(CobblenavPackets.RENDERABLE_POKEMON_PACKET_SERVER, PacketByteBufs.create());
    }

    public void createFinderShortcutWidget(RenderablePokemon pokemon) {
        finderShortcutWidget = new FinderShortcutWidget(borderX + BORDER_WIDTH - BORDER_DEPTH - 30,
                borderY + BORDER_DEPTH + 30, pokemon, this);
    }

    public void removeFinderShortcutWidget() {
        finderShortcutWidget = null;
    }

    @Override
    protected void init() {
        super.init();

        requestLastFoundPokemon();

        borderX = (width - BORDER_WIDTH) / 2;
        borderY = (height - BORDER_HEIGHT) / 2 - 10;
        int x = (width + BORDER_WIDTH) / 2 - 68 - BORDER_DEPTH;
        int y = (height) / 2 + 1;

        if (CobblenavClient.CONFIG.mainScreenWidget == MainScreenWidgetType.PARTY) {
            playerX = width / 2 - 50 + BORDER_DEPTH;
            playerY = borderY + 45;
            mainScreenWidget = new PartyWidget(playerX, playerY, borderX, borderY);
        }
        else {
            mainScreenWidget = new MainScreenWidget() {
                @Override
                protected void renderWidget(DrawContext drawContext, int i, int j, float f) {}
            };
        }

        spawnCheckButton = new MainScreenButton(x, y, 69, 14, 0, 0, 4,
                Text.translatable("gui.cobblenav.pokenav_item.spawn_check_button"),
                () -> {
                    player.playSound(CobblemonSounds.PC_CLICK, 0.1f, 1.25f);
                    MinecraftClient.getInstance().setScreen(new LocationScreen());
                }
        );
        contactsButton = new MainScreenButton(x, y + 16, 69, 14, 0, 0, 4,
                Text.translatable("gui.cobblenav.pokenav_item.contact_button"),
                () -> {
                    player.playSound(CobblemonSounds.PC_CLICK, 0.1f, 1.25f);
                    MinecraftClient.getInstance().setScreen(new ContactsScreen());
                }
        );
        closeButton = new MainScreenButton(x, y + 32, 69, 14, 0, 30, 4,
                Text.translatable("gui.cobblenav.pokenav_item.close_button"),
                this::close
        );
    }

    @Override
    public void renderScreen(DrawContext drawContext, int i, int j, float f) {
        MatrixStack matrixStack = drawContext.getMatrices();

        blitk(matrixStack, BACKGROUND,
                borderX + BORDER_DEPTH, borderY + BORDER_DEPTH + 20, BORDER_HEIGHT - 2 * BORDER_DEPTH - 20, BORDER_WIDTH - 2 * BORDER_DEPTH, 0, 0, 256,
                256, 0, 1, 1, 1, 1, false, 1);

        mainScreenWidget.render(drawContext, i, j, f);

        matrixStack.push();
        matrixStack.translate(0f, 0f, 2500f);
        closeButton.render(drawContext, i, j, f);
        contactsButton.render(drawContext, i, j, f);
        spawnCheckButton.render(drawContext, i, j, f);
        blitk(matrixStack, BUTTONS,
                borderX + BORDER_DEPTH, borderY + BORDER_DEPTH + 30, 14, 48, 0, 45, 256,
                256, 0, 1, 1, 1, 1, false, 1);
        drawScaledText(drawContext, FONT, Text.translatable("gui.cobblenav.pokenav_item.main_menu").setStyle(Style.EMPTY.withColor(0xFFFFFF)),
                borderX + BORDER_DEPTH + 6, borderY + BORDER_DEPTH + 33, 1, 1, 40, 0, false, false, i, j);
        matrixStack.pop();

        if (finderShortcutWidget != null) {
            finderShortcutWidget.render(drawContext, i, j, f);
            if (finderShortcutWidget.isHovered()) {
                drawContext.drawTooltip(textRenderer, List.of(finderShortcutWidget.getMessage(),
                        Text.translatable("gui.cobblenav.pokenav_item.last_found.tooltip")
                                .setStyle(Style.EMPTY.withItalic(true).withColor(0x4D4D4D))), i, j);
            }
        }
    }

    @Override
    public void onMouseClicked(double d, double e, int i) {
        if (finderShortcutWidget != null) {
            finderShortcutWidget.mouseClicked(d, e, i);
        }
        spawnCheckButton.mouseClicked(d, e, i);
        contactsButton.mouseClicked(d, e, i);
        closeButton.mouseClicked(d, e, i);
    }

    @Override
    public void onMouseDragged(double d, double e, int i, double f, double g) {

    }

    @Override
    public void onMouseScrolled(double d, double e, double f) {

    }
}
