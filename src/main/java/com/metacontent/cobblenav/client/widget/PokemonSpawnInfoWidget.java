package com.metacontent.cobblenav.client.widget;

import com.cobblemon.mod.common.CobblemonSounds;
import com.cobblemon.mod.common.client.gui.summary.widgets.ModelWidget;
import com.cobblemon.mod.common.pokemon.RenderablePokemon;
import com.metacontent.cobblenav.Cobblenav;
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
    private boolean showActionButtons = false;

    private final PokenavItemButton shareButton;
    private final PokenavItemButton findButton;

    public PokemonSpawnInfoWidget(int i, int j, RenderablePokemon pokemon, float probability, String bucket) {
        super(i, j, 20, 30, pokemon.getSpecies().getTranslatedName());
        this.pokemonModel = new ModelWidget(getX(), getY(), getWidth(), getHeight() - getHeight() / 3, pokemon, 0.5F, 340F, 0F);
        this.probability = probability;
        this.player = MinecraftClient.getInstance().player;

        shareButton = new PokenavItemButton(getX() + getWidth() / 2 + 1, getY() + getHeight() - 12, 11, 11, 73, 12,
                0, 0, Text.literal(""), BUTTONS, null,
                () -> {
                    player.playSound(CobblemonSounds.PC_CLICK, 0.1f, 1.25f);
                    String name = pokemonModel.getPokemon().getSpecies().getTranslatedName().getString();
                    Vec3d vec3d = player.getPos();
                    String coordinates = "x: " + (int) vec3d.x + " y: " + (int) vec3d.y + " z: " + (int) vec3d.z;
                    String chance = bucket + " - " + df.format(probability) + "%";
                    Text text = Text.translatable("gui.cobblenav.pokenav_item.spawn_info_message", name, coordinates, chance);
                    player.sendMessage(text);
                    player.sendMessage(Text.translatable("gui.cobblenav.pokenav_item.share_spawn_info_message")
                            .setStyle(Style.EMPTY.withColor(0xff9a38).withBold(true)
                                    .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, text.getString()))));
                }
        );
        findButton = new PokenavItemButton(getX() + getWidth() / 2 - 12, getY() + getHeight() - 12, 11, 11, 85, 12,
                0, 0, Text.literal(""), BUTTONS, null,
                () -> {
                    player.playSound(CobblemonSounds.PC_CLICK, 0.1f, 1.25f);
                    player.sendMessage(Text.literal("Not available at this time. Wait for the next Cobblenav update").setStyle(Style.EMPTY.withColor(0xff9a38)));
                }
        );
    }

    @Override
    protected void renderButton(DrawContext drawContext, int i, int j, float f) {
        pokemonModel.render(drawContext, i, j, f);
        if (isHovered()) {
            drawScaledText(drawContext, FONT, pokemonModel.getPokemon().getSpecies().getTranslatedName(),
                getX() + getWidth() / 2, getY() - 4, 1, 1, getWidth(), 0xFFFFFF, true, false, i, j);
        }
        drawScaledText(drawContext, FONT, Text.literal(df.format(probability) + "%").setStyle(Style.EMPTY.withBold(true)),
                getX() + getWidth() / 2, getY() + getHeight() - 10, 1, 1, 2 * getWidth(), 0xFFFFFF, true, false, i, j);
        if (showActionButtons) {
            showActionButtons = hovered;
            shareButton.renderButton(drawContext, i, j, f);
            findButton.renderButton(drawContext, i, j, f);
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
}
