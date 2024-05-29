package com.metacontent.cobblenav.client.widget.main_screen;

import com.cobblemon.mod.common.client.CobblemonClient;
import com.cobblemon.mod.common.client.render.models.blockbench.pokemon.PokemonPoseableModel;
import com.cobblemon.mod.common.client.render.models.blockbench.repository.PokemonModelRepository;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.activestate.ShoulderedState;
import com.metacontent.cobblenav.client.CobblenavClient;
import com.metacontent.cobblenav.client.widget.ModelWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import org.joml.Quaternionf;

import java.util.ArrayList;
import java.util.List;

import static com.metacontent.cobblenav.client.screen.AbstractPokenavItemScreen.*;

public class PartyWidget extends MainScreenWidget {
    private final PlayerEntity player;
    private final List<ModelWidget> partyModels;
    private final int playerX;
    private final int playerY;
    private final int borderX;
    private final int borderY;
    private final float baseScale;

    public PartyWidget(int playerX, int playerY, int borderX, int borderY) {
        this.borderX = borderX;
        this.borderY = borderY;
        this.player = MinecraftClient.getInstance().player;
        this.playerX = playerX;
        this.playerY = playerY;
        this.baseScale = CobblenavClient.CONFIG.screenScale;
        partyModels = new ArrayList<>();
        List<Pokemon> party = CobblemonClient.INSTANCE.getStorage().getMyParty().getSlots();

        int index = 0;
        int pX = playerX;
        for (Pokemon pokemon : party) {
            if (pokemon != null) {
                if (pokemon.getState() instanceof ShoulderedState) {
                    continue;
                }
                PokemonPoseableModel model = PokemonModelRepository.INSTANCE.getPoser(pokemon.getSpecies().getResourceIdentifier(), pokemon.getAspects());
                double adjustment = CobblenavClient.CONFIG.partyWidgetAdjustments.getOrDefault(pokemon.showdownId(), 0d);
                float scale = pokemon.getForm().getBaseScale() / model.getProfileScale();
                double scaledOffsetY = 100f - 35f * scale;
                pX += (index * 20 * (index % 2 == 1 ? -1 : 1)) + (index % 2 == 1 ? -1 : 1) * 20;
                //wth is going on with this offsetY :skull:
                ModelWidget modelWidget = new ModelWidget(pX - 101, playerY - 9, 200,
                        pokemon.asRenderablePokemon(), scale, 350f + 20 * (index % 2 == 1 ? 1 : 0), (int) (scaledOffsetY - model.getProfileTranslation().y - adjustment));
                partyModels.add(modelWidget);
                index++;
            }
        }
    }

    @Override
    protected void renderWidget(DrawContext drawContext, int i, int j, float f) {
        MatrixStack matrixStack = drawContext.getMatrices();
        drawContext.enableScissor((int) ((borderX + BORDER_DEPTH) * baseScale), (int) ((borderY + BORDER_DEPTH + 20) * baseScale),
                (int) ((borderX + BORDER_WIDTH - BORDER_DEPTH) * baseScale), (int) ((borderY + BORDER_HEIGHT - BORDER_DEPTH) * baseScale));
        matrixStack.push();
        matrixStack.translate(0f, 0f, 2000f);
        renderPlayer(drawContext, playerX, playerY, player);
        for (int index = 0; index < partyModels.size(); ++index) {
            partyModels.get(index).render(drawContext, i, j, f);
            if (index % 2 != 0) {
                matrixStack.translate(0f, 0f, -500f);
            }
        }
        matrixStack.pop();
        drawContext.disableScissor();
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

        drawContext.getMatrices().push();
        drawContext.getMatrices().translate(0f, 0f, 100f);
        InventoryScreen.drawEntity(drawContext, x, y + BORDER_HEIGHT / 2, 20, (new Quaternionf()).rotateZ(3.1415927F), (new Quaternionf()).rotateX(120.0F * 0.017453292F), this.player);
        drawContext.getMatrices().pop();

        player.bodyYaw = m;
        player.setYaw(n);
        player.setPitch(o);
        player.prevHeadYaw = p;
        player.headYaw = q;
    }
}
