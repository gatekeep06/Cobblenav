package com.metacontent.cobblenav.client.widget;

import com.cobblemon.mod.common.CobblemonSounds;
import com.cobblemon.mod.common.client.gui.summary.widgets.ModelWidget;
import com.cobblemon.mod.common.pokemon.RenderablePokemon;
import com.metacontent.cobblenav.Cobblenav;
import com.metacontent.cobblenav.client.screen.pokenav.FinderScreen;
import com.metacontent.cobblenav.client.screen.pokenav.MainScreen;
import com.metacontent.cobblenav.networking.CobblenavPackets;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static com.cobblemon.mod.common.api.gui.GuiUtilsKt.blitk;
import static com.cobblemon.mod.common.client.render.RenderHelperKt.drawScaledText;

public class FinderShortcutWidget extends ClickableWidget {
    private static final Identifier BACKGROUND = new Identifier(Cobblenav.ID, "textures/gui/pokenav_item_gui_buttons.png");
    private static final Identifier BACKGROUND_HOVERED = new Identifier(Cobblenav.ID, "textures/gui/pokenav_item_gui_buttons_hovered.png");
    private static final Identifier FONT = new Identifier("uniform");
    private final MainScreen parent;
    private final ModelWidget pokemonModel;
    private final PlayerEntity player;
    private boolean isSoundPlayed = false;
    private final TextRenderer textRenderer;

    public FinderShortcutWidget(int x, int y, RenderablePokemon pokemon, MainScreen parent, TextRenderer textRenderer) {
        super(x, y, 20, 30, pokemon.getSpecies().getTranslatedName());
        this.pokemonModel = new ModelWidget(getX(), getY(), getWidth(), 2 * getHeight() / 3, pokemon, 0.5f, 340, 0);
        player = MinecraftClient.getInstance().player;
        this.parent = parent;
        this.textRenderer = textRenderer;
    }

    @Override
    protected void renderButton(DrawContext drawContext, int i, int j, float f) {
        if (isHovered()) {
            drawContext.drawTooltip(textRenderer, getMessage().copy(), i, j);
            if (!isSoundPlayed) {
                isSoundPlayed = true;
                if (player != null) {
                    player.playSound(CobblemonSounds.POKE_BALL_SHAKE, 0.2f, 1.5f);
                }
            }
            blitk(drawContext.getMatrices(), BACKGROUND_HOVERED, getX(), getY(), getHeight(), getWidth() + 1,
                    0, 104, 256, 256, 0, 1, 1, 1, 1, false, 1);
        }
        else {
            isSoundPlayed = false;
            blitk(drawContext.getMatrices(), BACKGROUND, getX(), getY(), getHeight(), getWidth() + 1,
                    0, 104, 256, 256, 0, 1, 1, 1, 1, false, 1);
        }
        drawScaledText(drawContext, FONT, Text.translatable("gui.cobblenav.pokenav_item.last_found")
                        .setStyle(Style.EMPTY.withBold(true)),
                getX() + getWidth() / 2 + 1, getY(), 1f, 1f, getWidth() - 2, 0xFFFFFF, true, true, i, j);
        pokemonModel.render(drawContext, i, j, f);
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder narrationMessageBuilder) {

    }

    @Override
    public void onClick(double d, double e) {
        player.playSound(CobblemonSounds.PC_CLICK, 0.1f, 1.25f);
        MinecraftClient.getInstance().setScreen(new FinderScreen(pokemonModel.getPokemon(), parent));
    }



    @Override
    public void setPosition(int x, int y) {
        super.setPosition(x, y);
    }

    @Override
    public boolean mouseClicked(double d, double e, int i) {
        if (this.active && this.visible) {
            boolean bl = this.clicked(d, e);
            if (bl) {
                if (this.isValidClickButton(i)) {
                    this.onClick(d, e);
                    return true;
                }
                else if (i == 1) {
                    ClientPlayNetworking.send(CobblenavPackets.REMOVE_LAST_FOUND_POKEMON_PACKET, PacketByteBufs.create());
                    parent.removeFinderShortcutWidget();
                }
            }
        }
        return false;
    }
}
