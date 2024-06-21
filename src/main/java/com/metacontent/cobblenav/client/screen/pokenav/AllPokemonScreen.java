package com.metacontent.cobblenav.client.screen.pokenav;

import com.cobblemon.mod.common.pokemon.RenderablePokemon;
import com.metacontent.cobblenav.client.CobblenavClient;
import com.metacontent.cobblenav.client.screen.AbstractPokenavItemScreen;
import com.metacontent.cobblenav.client.widget.ModelWidget;
import com.metacontent.cobblenav.client.widget.PokemonSpawnInfoWidget;
import com.metacontent.cobblenav.client.widget.ScrollableViewWidget;
import com.metacontent.cobblenav.client.widget.TableWidget;
import com.metacontent.cobblenav.util.BorderBox;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.List;

import static com.cobblemon.mod.common.api.gui.GuiUtilsKt.blitk;

public class AllPokemonScreen extends AbstractPokenavItemScreen {
    private final List<RenderablePokemon> allPokemon;
    private ScrollableViewWidget<TableWidget<ModelWidget>> allPokemonView;

    public AllPokemonScreen(List<RenderablePokemon> allPokemon) {
        super(Text.literal("AllPokemon"));
        this.allPokemon = allPokemon;
    }

    @Override
    protected void init() {
        super.init();
        int y = getBorderY() + BORDER_DEPTH + 30;
        TableWidget<ModelWidget> tableWidget = new TableWidget<>(getBorderX() + BORDER_DEPTH + 3, y,
                7, 3, new BorderBox(4, 0));
        allPokemonView = new ScrollableViewWidget<>(tableWidget, 200, 106, CobblenavClient.CONFIG.scrollSize);
        List<ModelWidget> widgets = allPokemon.stream().map(pokemon -> new ModelWidget(0, 0, 20, pokemon, 0.5f, 340f, 0)).toList();
        tableWidget.calcRows(allPokemon.size());
        tableWidget.setWidgets(widgets);
    }

    @Override
    public void renderScreen(DrawContext drawContext, int i, int j, float f) {
        MatrixStack matrixStack = drawContext.getMatrices();

        blitk(matrixStack, BACKGROUND,
                getBorderX() + BORDER_DEPTH, getBorderY() + BORDER_DEPTH + 20, BORDER_HEIGHT - 2 * BORDER_DEPTH - 32, BORDER_WIDTH - 2 * BORDER_DEPTH, 0, 131, 256,
                256, 0, 1, 1, 1, 1, false, 1);

        blitk(matrixStack, BORDERS,
                getBorderX(), getBorderY() + BORDER_HEIGHT - BORDER_DEPTH - 14, 14, BORDER_WIDTH, 0, BORDER_HEIGHT + 1, 256,
                256, 0, 1,1,1,1,false,1);

        allPokemonView.render(drawContext, i, j, f);
    }

    @Override
    public void onMouseClicked(double d, double e, int i) {
        allPokemonView.mouseClicked(d, e, i);
    }

    @Override
    public void onMouseDragged(double d, double e, int i, double f, double g) {
        allPokemonView.mouseDragged(d, e, i, f, g);
    }

    @Override
    public void onMouseScrolled(double d, double e, double f) {
        allPokemonView.mouseScrolled(d, e, f);
    }
}
