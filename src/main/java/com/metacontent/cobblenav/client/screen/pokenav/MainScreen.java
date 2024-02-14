package com.metacontent.cobblenav.client.screen.pokenav;

import com.cobblemon.mod.common.CobblemonSounds;
import com.cobblemon.mod.common.client.CobblemonClient;
import com.cobblemon.mod.common.client.gui.summary.widgets.ModelWidget;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.RenderablePokemon;
import com.cobblemon.mod.common.pokemon.activestate.ShoulderedState;
import com.metacontent.cobblenav.client.screen.AbstractPokenavItemScreen;
import com.metacontent.cobblenav.client.widget.FinderShortcutWidget;
import com.metacontent.cobblenav.client.widget.PokenavItemButton;
import com.metacontent.cobblenav.config.CobblenavConfig;
import com.metacontent.cobblenav.networking.CobblenavPackets;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;

import java.util.ArrayList;
import java.util.List;

import static com.cobblemon.mod.common.api.gui.GuiUtilsKt.blitk;
import static com.cobblemon.mod.common.client.render.RenderHelperKt.drawScaledText;

@Environment(EnvType.CLIENT)
public class MainScreen extends AbstractPokenavItemScreen {
    private int borderX;
    private int borderY;
    private int playerX;
    private int playerY;
    private List<ModelWidget> partyModels;
    private PokenavItemButton closeButton;
    private PokenavItemButton contactsButton;
    private PokenavItemButton spawnCheckButton;
    private FinderShortcutWidget finderShortcutWidget = null;

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

        partyModels = new ArrayList<>();
        borderX = (width - BORDER_WIDTH) / 2;
        borderY = (height - BORDER_HEIGHT) / 2 - 10;
        playerX = width / 2 - 50 + BORDER_DEPTH;
        playerY = borderY + 45;
        int x = (width + BORDER_WIDTH) / 2 - 68 - BORDER_DEPTH;
        int y = (height) / 2 + 1;

        List<Pokemon> party = CobblemonClient.INSTANCE.getStorage().getMyParty().getSlots();

        int index = 0;
        int pX = playerX;
        for (Pokemon pokemon : party) {
            if (pokemon != null) {
                if (pokemon.getState() instanceof ShoulderedState) {
                    continue;
                }
                pX += (int) (index * 18 * (index % 2 == 1 ? -1 : 1)) + (index % 2 == 1 ? -1 : 1) * 20;
                ModelWidget modelWidget = new ModelWidget(pX - 51, playerY + BORDER_HEIGHT / 2 - 35, 100, 100,
                        pokemon.asRenderablePokemon(), 1f - 0.05f * (index % 2 == 1 ? index - 1 : index), 350f + 20 * (index % 2 == 1 ? 1 : 0), 0);
                partyModels.add(modelWidget);
                index++;
            }
        }

        spawnCheckButton = new PokenavItemButton(x, y, 69, 14, 0, 0, 3, 0,
                Text.translatable("gui.cobblenav.pokenav_item.spawn_check_button"),
                BUTTONS,
                BUTTONS_HOVERED,
                () -> {
                    player.playSound(CobblemonSounds.PC_CLICK, 0.1f, 1.25f);
                    MinecraftClient.getInstance().setScreen(new LocationScreen(null));
                }
        );
        contactsButton = new PokenavItemButton(x, y + 16, 69, 14, 0, 0, 3, 0,
                Text.translatable("gui.cobblenav.pokenav_item.contact_button"),
                BUTTONS,
                BUTTONS_HOVERED,
                () -> {
                    player.playSound(CobblemonSounds.PC_CLICK, 0.1f, 1.25f);
                    MinecraftClient.getInstance().setScreen(new ContactsScreen());
                }
        );
        closeButton = new PokenavItemButton(x, y + 32, 69, 14, 0, 30, 3, 0,
                Text.translatable("gui.cobblenav.pokenav_item.close_button"),
                BUTTONS,
                BUTTONS_HOVERED,
                this::close
        );
    }

    @Override
    public void render(DrawContext drawContext, int i, int j, float f) {
        renderBackground(drawContext);

        MatrixStack matrixStack = drawContext.getMatrices();

        blitk(matrixStack, BACKGROUND,
                borderX + BORDER_DEPTH, borderY + BORDER_DEPTH + 20, BORDER_HEIGHT - 2 * BORDER_DEPTH - 20, BORDER_WIDTH - 2 * BORDER_DEPTH, 0, 0, 256,
                256, 0, 1, 1, 1, 1, false, 1);

        blitk(matrixStack, BUTTONS,
                borderX + BORDER_DEPTH, borderY + BORDER_DEPTH + 30, 14, 48, 0, 45, 256,
                256, 0, 1, 1, 1, 1, false, 1);

        if (CobblenavConfig.DISPLAY_TEAM_WIDGET) {
            for (ModelWidget modelWidget : partyModels) {
                modelWidget.render(drawContext, i, j, f);
            }

            renderPlayer(drawContext, playerX, playerY, player);
        }

        if (finderShortcutWidget != null) {
            finderShortcutWidget.render(drawContext, i, j, f);
        }

        drawScaledText(drawContext, FONT, Text.translatable("gui.cobblenav.pokenav_item.main_menu").setStyle(Style.EMPTY.withColor(0xFFFFFF)),
                borderX + BORDER_DEPTH + 6, borderY + BORDER_DEPTH + 33, 1, 1, 40, 0, false, false, i, j);

        closeButton.render(drawContext, i, j, f);
        contactsButton.render(drawContext, i, j, f);
        spawnCheckButton.render(drawContext, i, j, f);

        super.render(drawContext, i, j, f);
    }

    @Override
    public boolean mouseClicked(double d, double e, int i) {
        if (finderShortcutWidget != null) {
            finderShortcutWidget.mouseClicked(d, e, i);
        }
        spawnCheckButton.mouseClicked(d, e, i);
        contactsButton.mouseClicked(d, e, i);
        closeButton.mouseClicked(d, e, i);
        return super.mouseClicked(d, e, i);
    }

    private void renderPlayer(DrawContext drawContext, int x, int y, PlayerEntity player) {
        Quaternionf quaternionf = (new Quaternionf()).rotateZ(3.1415927F);
        Quaternionf quaternionf2 = (new Quaternionf()).rotateX(20.0F * 0.017453292F);
        quaternionf.mul(quaternionf2);
        float m = player.bodyYaw;
        float n = player.getYaw();
        float o = player.getPitch();
        float p = player.prevHeadYaw;
        float q = player.headYaw;
        player.bodyYaw = 190.0F;
        player.setYaw(220.0F);
        player.setPitch(0.0F);
        player.headYaw = 180F;
        player.prevHeadYaw = player.getYaw();
        InventoryScreen.drawEntity(drawContext, x, y + BORDER_HEIGHT / 2, 20, (new Quaternionf()).rotateZ(3.1415927F), (new Quaternionf()).rotateX(120.0F * 0.017453292F), this.player);
        player.bodyYaw = m;
        player.setYaw(n);
        player.setPitch(o);
        player.prevHeadYaw = p;
        player.headYaw = q;
    }
}
