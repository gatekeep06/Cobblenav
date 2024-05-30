package com.metacontent.cobblenav.client.widget;

import com.cobblemon.mod.common.api.types.ElementalType;
import com.cobblemon.mod.common.api.types.ElementalTypes;
import com.cobblemon.mod.common.client.gui.summary.widgets.type.SingleTypeWidget;
import com.cobblemon.mod.common.client.gui.summary.widgets.type.TypeWidget;
import com.metacontent.cobblenav.Cobblenav;
import com.metacontent.cobblenav.util.BorderBox;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.cobblemon.mod.common.api.gui.GuiUtilsKt.blitk;

public class BadgeDisplayWidget implements Drawable {
    private final TableWidget<TypeWidget> badgeTable;

    public BadgeDisplayWidget(int x, int y) {
        badgeTable = new TableWidget<>(x, y, 9, 0, new BorderBox(1));
    }

    @Override
    public void render(DrawContext drawContext, int i, int j, float f) {
        badgeTable.render(drawContext, i, j, f);
    }

    public void setBadges(Set<String> allBadges, Set<String> playerBadges) {
        List<TypeWidget> typeWidgets = new ArrayList<>();
        for (String badge : allBadges) {
            ElementalType type = ElementalTypes.INSTANCE.get(badge.toLowerCase());
            if (type != null) {
                boolean granted = playerBadges.stream().anyMatch(playerBadge -> playerBadge.toLowerCase().equals(type.getName()));
                TypeWidget typeWidget = new BadgeWidget(0, 0, 9, 9, type, granted);
                typeWidgets.add(typeWidget);
            }
        }
        badgeTable.calcRows(typeWidgets.size());
        badgeTable.setWidgets(typeWidgets);
    }

    private static class BadgeWidget extends TypeWidget {
        private static final Identifier COVERAGE = new Identifier(Cobblenav.ID, "textures/gui/type_widget_coverage.png");
        private final ElementalType type;
        private final boolean granted;

        public BadgeWidget(int pX, int pY, int pWidth, int pHeight, ElementalType type, boolean granted) {
            super(pX, pY, pWidth, pHeight, Text.literal("BadgeWidget"));
            this.type = type;
            this.granted = granted;
        }

        @Override
        protected void renderButton(DrawContext drawContext, int i, int j, float f) {
            MatrixStack matrixStack = drawContext.getMatrices();
            matrixStack.push();
            renderType(type, matrixStack, getX(), getY());
            if (!granted) {
                float scale = 0.25f;
                blitk(matrixStack, COVERAGE, (getX() + 0.5) / scale, getY() / scale, 36, 36, 0, 0,
                        36, 36, 0, 1, 1, 1, 0.7, true, scale);
            }
            matrixStack.pop();
        }
    }
}
