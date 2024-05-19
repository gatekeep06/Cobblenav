package com.metacontent.cobblenav.client.widget;

import com.metacontent.cobblenav.Cobblenav;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static com.cobblemon.mod.common.api.gui.GuiUtilsKt.blitk;
import static com.cobblemon.mod.common.client.render.RenderHelperKt.drawScaledText;

//TODO: Avatar icons and title animation
public class ContactListItem extends ClickableWidget {
    private static final Identifier FONT = new Identifier("uniform");
    private static final Identifier TEXTURE = new Identifier(Cobblenav.ID, "textures/gui/pokenav_item_gui_buttons.png");
    private static final int MAX_WIDTH = 56;
    private final String name;
    private final String title;
    private final int index;
    private boolean isSelected;
    private final OnSelect action;
    private final int maxRenderY;
    private final int minRenderY;

    public ContactListItem(int x, int y, String name, String title, int index, OnSelect onSelect, int maxRenderY, int minRenderY) {
        super(x, y, 110, 12, Text.literal(""));
        this.name = name;
        this.title = title;
        this.index = index;
        this.action = onSelect;
        this.maxRenderY = maxRenderY;
        this.minRenderY = minRenderY;
    }

    public void renderItem(DrawContext drawContext, int i, int j, float f, int selectedIndex) {
        this.isSelected = selectedIndex == index;
        render(drawContext, i, j, f);
    }

    @Override
    protected void renderButton(DrawContext drawContext, int i, int j, float f) {
        if (isVisible()) {
            Style style = Style.EMPTY.withBold(isSelected).withItalic(isSelected);

            if (isSelected) {
                blitk(drawContext.getMatrices(), TEXTURE, getX(), getY() + 1, 7, 4, 0, 72, 256,
                        256, 0, 1, 1, 1, 1, false, 1);
            }

            drawScaledText(drawContext, FONT, Text.literal(name).setStyle(style),
                    getX() + 6, getY(), 1, 1,
                    MAX_WIDTH, isHovered() ? 0xD3D3D3 : 0xFFFFFF, false, isHovered(), i, j);

            drawScaledText(drawContext, FONT, Text.literal(title).setStyle(style),
                    getX() + MAX_WIDTH + 8, getY(), 1, 1,
                    MAX_WIDTH, isHovered() ? 0xD3D3D3 : 0xFFFFFF, false, isHovered(), i, j);
        }
    }

    private boolean isVisible() {
        return getY() >= minRenderY && getY() <= maxRenderY;
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder narrationMessageBuilder) {

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
        action.onSelect();
    }

    public interface OnSelect {
        void onSelect();
    }
}
