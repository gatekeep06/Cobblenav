package com.metacontent.cobblenav.client.widget;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static com.cobblemon.mod.common.client.render.RenderHelperKt.drawScaledText;

public class ContactListItem extends ClickableWidget {
    private static final Identifier FONT = new Identifier("uniform");
    private static final int MAX_WIDTH = 56;
    private final String name;
    private final String title;
    private final int index;
    private boolean isSelected;
    private final OnSelect action;

    public ContactListItem(int x, int y, String name, String title, int index, OnSelect onSelect) {
        super(x, y, 120, 12, Text.literal(""));
        this.name = name;
        this.title = title;
        this.index = index;
        this.action = onSelect;
    }

    public void renderItem(DrawContext drawContext, int i, int j, float f, int selectedIndex) {
        this.isSelected = selectedIndex == index;
        render(drawContext, i, j, f);
    }

    @Override
    protected void renderButton(DrawContext drawContext, int i, int j, float f) {
        Style style = Style.EMPTY.withBold(isSelected).withItalic(isSelected);

        drawScaledText(drawContext, FONT, Text.literal(name).setStyle(style),
                getX(), getY(), 1, 1,
                MAX_WIDTH, isHovered() ? 0xD3D3D3 : 0xFFFFFF, false, isHovered(), i, j);

        drawScaledText(drawContext, FONT, Text.literal(title).setStyle(style),
                getX() + MAX_WIDTH + 2, getY(), 1, 1,
                MAX_WIDTH, isHovered() ? 0xD3D3D3 : 0xFFFFFF, false, isHovered(), i, j);
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder narrationMessageBuilder) {

    }

    @Override
    public void onClick(double d, double e) {
        action.onSelect();
    }

    public interface OnSelect {
        void onSelect();
    }
}
