package com.metacontent.cobblenav.client.widget;

import com.cobblemon.mod.common.api.types.ElementalType;
import com.cobblemon.mod.common.api.types.ElementalTypes;
import com.cobblemon.mod.common.client.gui.summary.widgets.type.TypeWidget;
import com.metacontent.cobblenav.Cobblenav;
import com.metacontent.cobblenav.util.BorderBox;
import com.metacontent.cobblenav.util.GrantedBadge;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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

    public void setBadges(Set<String> allBadges, Set<GrantedBadge> playerBadges) {
        List<TypeWidget> typeWidgets = new ArrayList<>();
        for (String badge : allBadges) {
            ElementalType type = ElementalTypes.INSTANCE.get(badge.toLowerCase());
            if (type != null) {
                GrantedBadge grantedBadge = playerBadges.stream().filter(b -> b.type().equals(badge))
                        .findFirst().orElse(null);
                TypeWidget typeWidget = new BadgeWidget(0, 0, 9, 9, type, grantedBadge);
                typeWidgets.add(typeWidget);
            }
        }
        badgeTable.calcRows(typeWidgets.size());
        badgeTable.setWidgets(typeWidgets);
    }

    private static class BadgeWidget extends TypeWidget {
        private static final Identifier COVERAGE = new Identifier(Cobblenav.ID, "textures/gui/type_widget_coverage.png");
        private final ElementalType type;
        private final GrantedBadge grantedBadge;
        private final TextRenderer textRenderer;
        private final List<Text> tooltip;

        public BadgeWidget(int pX, int pY, int pWidth, int pHeight, ElementalType type, GrantedBadge grantedBadge) {
            super(pX, pY, pWidth, pHeight, Text.literal("BadgeWidget"));
            this.type = type;
            this.grantedBadge = grantedBadge;
            this.textRenderer = MinecraftClient.getInstance().textRenderer;
            Style typeStyle = Style.EMPTY.withColor(type.getHue());
            this.tooltip = granted() ? List.of(
                    type.getDisplayName().setStyle(typeStyle),
                    Text.literal(grantedBadge.grantedBy()),
                    Text.literal(grantedBadge.grantDate().toInstant().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_LOCAL_DATE))
            ) : List.of(type.getDisplayName().setStyle(typeStyle));
        }

        @Override
        protected void renderButton(DrawContext drawContext, int i, int j, float f) {
            MatrixStack matrixStack = drawContext.getMatrices();
            matrixStack.push();
            renderType(type, matrixStack, getX(), getY());
            if (!granted()) {
                float scale = 0.25f;
                blitk(matrixStack, COVERAGE, (getX() + 0.5) / scale, getY() / scale, 36, 36, 0, 0,
                        36, 36, 0, 1, 1, 1, 0.7, true, scale);
            }
            if (hovered) {
                matrixStack.push();
                matrixStack.translate(0f, 0f, 1f);
                drawContext.drawTooltip(textRenderer, tooltip, i, j);
                matrixStack.pop();
            }
            matrixStack.pop();
        }

        private boolean granted() {
            return grantedBadge != null;
        }
    }
}
