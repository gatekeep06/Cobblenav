package com.metacontent.cobblenav.client.widget.main_screen;

import com.cobblemon.mod.common.client.CobblemonClient;
import com.cobblemon.mod.common.client.gui.summary.widgets.ModelWidget;
import com.cobblemon.mod.common.client.render.models.blockbench.pokemon.PokemonPoseableModel;
import com.cobblemon.mod.common.client.render.models.blockbench.repository.PokemonModelRepository;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.activestate.ShoulderedState;
import com.metacontent.cobblenav.client.CobblenavClient;
import com.metacontent.cobblenav.client.RenderUtility;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
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

    public PartyWidget(int playerX, int playerY, int borderX, int borderY) {
        this.player = MinecraftClient.getInstance().player;
        this.playerX = playerX;
        this.playerY = playerY;
        this.borderX = borderX;
        this.borderY = borderY;
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
                ModelWidget modelWidget = new ModelWidget(pX - 101, borderY + 35, 200, 200,
                        pokemon.asRenderablePokemon(), scale, 350f + 20 * (index % 2 == 1 ? 1 : 0), scaledOffsetY - model.getProfileTranslation().y - adjustment);
                partyModels.add(modelWidget);
                index++;
            }
        }
    }

    @Override
    protected void renderWidget(DrawContext drawContext, int i, int j, float f) {
        MatrixStack matrixStack = drawContext.getMatrices();
        drawContext.enableScissor(borderX + BORDER_DEPTH, borderY + BORDER_DEPTH + 20,
                borderX + BORDER_WIDTH - BORDER_DEPTH, borderY + BORDER_HEIGHT - BORDER_DEPTH);
        matrixStack.push();
        matrixStack.translate(0f, 0f, 2000f);
        RenderUtility.renderPlayer(drawContext, playerX, playerY, player);
        for (int index = 0; index < partyModels.size(); ++index) {
            partyModels.get(index).render(drawContext, i, j, f);
            if (index % 2 != 0) {
                matrixStack.translate(0f, 0f, -500f);
            }
        }
        matrixStack.pop();
        drawContext.disableScissor();
    }
}
