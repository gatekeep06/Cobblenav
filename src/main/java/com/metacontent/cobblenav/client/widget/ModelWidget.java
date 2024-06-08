package com.metacontent.cobblenav.client.widget;

import com.cobblemon.mod.common.client.render.models.blockbench.pokemon.PokemonFloatingState;
import com.cobblemon.mod.common.pokemon.RenderablePokemon;
import com.cobblemon.mod.common.util.math.QuaternionUtilsKt;
import com.metacontent.cobblenav.Cobblenav;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import static com.cobblemon.mod.common.client.gui.PokemonGuiUtilsKt.drawProfilePokemon;

public class ModelWidget implements Drawable {
    private int x;
    private int y;
    private final int width;
    private final int offsetY;
    private RenderablePokemon pokemon;
    private final float scale;
    private final Vector3f rotationVec;
    private final PokemonFloatingState state = new PokemonFloatingState();
    private boolean visible = true;

    public ModelWidget(int x, int y, int width, RenderablePokemon pokemon, float scale, float rotationY, int offsetY) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.pokemon = pokemon;
        this.scale = scale;
        this.rotationVec = new Vector3f(8f, rotationY, 0f);
        this.offsetY = offsetY;
    }

    @Override
    public void render(DrawContext drawContext, int i, int j, float f) {
        MatrixStack matrixStack = drawContext.getMatrices();
        matrixStack.push();
        matrixStack.translate(x + width / 2f, y + offsetY, 0f);
        matrixStack.scale(scale, scale, 1f);
        matrixStack.push();
        if (visible) {
            try {
                drawProfilePokemon(pokemon, matrixStack, QuaternionUtilsKt.fromEulerXYZDegrees(new Quaternionf(), rotationVec), state, f, 20f);
            } catch (Exception e) {
                visible = false;
                String msg = "An exception occurred when attempting to render " + pokemon.getSpecies().getTranslatedName().getString() + "!";
                PlayerEntity player = MinecraftClient.getInstance().player;
                if (player != null) {
                    player.sendMessage(Text.literal(msg).formatted(Formatting.RED));
                }
                Cobblenav.LOGGER.warn(e.getMessage());
                Cobblenav.LOGGER.warn(msg + " Check your Cobblemon addons for updates");
            }
        }
        matrixStack.pop();
        matrixStack.pop();
    }

    public RenderablePokemon getPokemon() {
        return pokemon;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setPokemon(RenderablePokemon pokemon) {
        this.pokemon = pokemon;
    }
}
