package com.metacontent.cobblenav.client.widget;

import com.cobblemon.mod.common.client.gui.summary.widgets.ModelWidget;
import com.cobblemon.mod.common.pokemon.RenderablePokemon;
import com.metacontent.cobblenav.Cobblenav;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.text.DecimalFormat;

import static com.cobblemon.mod.common.api.gui.GuiUtilsKt.blitk;
import static com.cobblemon.mod.common.client.render.RenderHelperKt.drawScaledText;

public class PokemonSpawnInfoWidget extends ClickableWidget {
    private static final Identifier FONT = new Identifier("uniform");
    private static final Identifier COPIED = new Identifier(Cobblenav.ID, "textures/gui/pokenav_item_gui_buttons.png");
    private final ModelWidget pokemonModel;
    private final float probability;
    private final String bucket;
    private static final DecimalFormat df = new DecimalFormat("#.##");
    private int timer = 0;

    public PokemonSpawnInfoWidget(int i, int j, RenderablePokemon pokemon, float probability, String bucket) {
        super(i, j, 20, 30, pokemon.getSpecies().getTranslatedName());
        this.pokemonModel = new ModelWidget(getX(), getY(), getWidth(), getHeight() - getHeight() / 3, pokemon, 0.5F, 340F, 0F);
        this.probability = probability;
        this.bucket = bucket;
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
        if (timer > 0) {
            blitk(drawContext.getMatrices(), COPIED, getX() + getWidth() / 2 - 5, getY() + getHeight() - 12, 10, 10, 0, 60, 256,
                    256, 0, 1, 1, 1, 1, false, 1);
            timer--;
        }
    }

    @Override
    public boolean mouseClicked(double d, double e, int i) {
        if (this.active && this.visible) {
            if (this.isValidClickButton(i)) {
                boolean bl = this.clicked(d, e);
                if (bl) {
                    this.onClick(d, e);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onClick(double d, double e) {
        timer = 100;
        String name = pokemonModel.getPokemon().getSpecies().getTranslatedName().getString();
        Vec3d vec3d = MinecraftClient.getInstance().player.getPos();
        String coordinates = "x: " + (int) vec3d.x + " y: " + (int) vec3d.y + " z: " + (int) vec3d.z;
        String chance = bucket + " - " + df.format(probability) + "%";
        MinecraftClient.getInstance().player.sendMessage(Text.translatable("gui.cobblenav.pokenav_item.spawn_message_clipboard",
                name, coordinates, chance));
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder narrationMessageBuilder) {

    }
}
