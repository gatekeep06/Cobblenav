package com.metacontent.cobblenav.client.widget;

import com.cobblemon.mod.common.CobblemonSounds;
import com.cobblemon.mod.common.client.gui.summary.widgets.ModelWidget;
import com.cobblemon.mod.common.pokemon.RenderablePokemon;
import com.metacontent.cobblenav.Cobblenav;
import com.metacontent.cobblenav.client.CobblenavClient;
import com.metacontent.cobblenav.client.screen.pokenav.FinderScreen;
import com.metacontent.cobblenav.client.screen.pokenav.LocationScreen;
import com.metacontent.cobblenav.config.util.PercentageDisplayType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.text.DecimalFormat;

import static com.cobblemon.mod.common.client.render.RenderHelperKt.drawScaledText;

public class PokemonSpawnInfoWidget extends ClickableWidget {
    private static final Identifier FONT = new Identifier("uniform");
    private static final Identifier BUTTONS = new Identifier(Cobblenav.ID, "textures/gui/pokenav_item_gui_buttons.png");
    private final ModelWidget pokemonModel;
    private final PlayerEntity player;
    private final float probability;
    private static final DecimalFormat df = new DecimalFormat("#.##");
    private String sign = "%";
    private int color = 0xffffff;
    private boolean showActionButtons = false;
    private final int minRenderY;
    private final int maxRenderY;

    private final IconButton shareButton;
    private final IconButton findButton;

    public PokemonSpawnInfoWidget(int i, int j, RenderablePokemon pokemon, float probability, LocationScreen parent, int minRenderY, int maxRenderY) {
        super(i, j, 20, 30, pokemon.getSpecies().getTranslatedName());
        this.pokemonModel = new ModelWidget(getX(), getY(), getWidth(), getHeight() - getHeight() / 3, pokemon, 0.5F, 340F, 0F);

        PercentageDisplayType type = CobblenavClient.CONFIG.percentageDisplayType;
        if (type == PercentageDisplayType.PERMILLE_ALLOWED) {
            if (probability < 0.01f) {
                color = 0xD3D3D3;
                sign = "‰";
                this.probability = probability * 10f;
            }
            else {
                this.probability = probability;
            }
        }
        else {
            this.probability = probability;
        }

        this.player = MinecraftClient.getInstance().player;
        this.minRenderY = minRenderY;
        this.maxRenderY = maxRenderY;

        shareButton = new IconButton(getX() + getWidth() / 2 + 1, getY() + getHeight() - 12, 11, 11, 73, 12,
                0,
                () -> {
                    player.playSound(CobblemonSounds.PC_CLICK, 0.1f, 1.25f);
                    String form = pokemonModel.getPokemon().getForm().getName();
                    String name = pokemonModel.getPokemon().getSpecies().getTranslatedName().getString() + (form.equals("Normal") ? "" : " (" + form + ")");
                    Vec3d vec3d = player.getPos();
                    String coordinates = "x: " + (int) vec3d.x + " y: " + (int) vec3d.y + " z: " + (int) vec3d.z + " (" + player.getWorld().getDimensionKey().getValue() + ")";
                    String chance = parent.getCurrentBucketName() + " - " + df.format(probability) + sign;
                    Text text = Text.translatable("gui.cobblenav.pokenav_item.spawn_info_message", name, coordinates, chance);
                    player.sendMessage(text);
                    player.sendMessage(Text.translatable("gui.cobblenav.pokenav_item.share_spawn_info_message")
                            .setStyle(Style.EMPTY.withColor(0xff9a38).withBold(true)
                                    .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, text.getString()))));
                }
        );
        findButton = new IconButton(getX() + getWidth() / 2 - 12, getY() + getHeight() - 12, 11, 11, 85, 12,
                0,
                () -> {
                    parent.savePreferences();
                    player.playSound(CobblemonSounds.PC_CLICK, 0.1f, 1.25f);
                    MinecraftClient.getInstance().setScreen(new FinderScreen(pokemonModel.getPokemon(), parent));
                }
        );
    }

    public boolean isVisible() {
        return getY() >= minRenderY && getY() <= maxRenderY;
    }

    @Override
    protected void renderButton(DrawContext drawContext, int i, int j, float f) {
        if (isVisible()) {
            pokemonModel.render(drawContext, i, j, f);
            drawScaledText(drawContext, FONT, Text.literal(df.format(probability) + sign).setStyle(Style.EMPTY.withBold(true)),
                    getX() + getWidth() / 2, getY() + getHeight() - 10, 1, 1, 2 * getWidth(), color, true, false, i, j);
            if (showActionButtons) {
                showActionButtons = hovered;
                shareButton.renderButton(drawContext, i, j, f);
                findButton.renderButton(drawContext, i, j, f);
            }
        }
    }

    @Override
    public boolean mouseClicked(double d, double e, int i) {
        if (this.active && this.visible) {
            if (this.isValidClickButton(i)) {
                boolean bl = this.clicked(d, e);
                if (bl) {
                    if (showActionButtons) {
                        shareButton.mouseClicked(d, e, i);
                        findButton.mouseClicked(d, e, i);
                    }
                    this.onClick(d, e);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onClick(double d, double e) {
        showActionButtons = !showActionButtons;
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder narrationMessageBuilder) {

    }

    @Override
    public void setX(int i) {
        super.setX(i);
        this.pokemonModel.setX(i);
        this.shareButton.setX(getX() + getWidth() / 2 + 1);
        this.findButton.setX(getX() + getWidth() / 2 - 12);
    }

    @Override
    public void setY(int i) {
        super.setY(i);
        this.pokemonModel.setY(i);
        this.shareButton.setY(getY() + getHeight() - 12);
        this.findButton.setY(getY() + getHeight() - 12);
    }

    public ModelWidget getPokemonModel() {
        return pokemonModel;
    }

    public String getProbabilityString() {
        return probability + sign;
    }
}
