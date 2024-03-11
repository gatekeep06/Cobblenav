package com.metacontent.cobblenav.client.widget;

import com.metacontent.cobblenav.util.BorderBox;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class TableWidget<T extends ClickableWidget> extends ClickableWidget {
    private final int columns;
    private final int rows;
    private List<T> widgets;
    private final BorderBox box;

    public TableWidget(int x, int y, int columns, int rows, List<T> widgets, BorderBox box) {
        super(x, y, 0, 0, Text.literal(""));
        this.columns = columns;
        this.rows = rows;
        this.box = box;
        setWidgets(widgets);
    }

    public void setWidgets(List<T> widgets) {
        this.widgets = widgets;
        List<Integer> widths = new ArrayList<>();
        List<Integer> heights = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            int width = 0;
            for (int j = 0; j < columns; j++) {
                T widget = widgets.get(i * columns + j);
                widget.setX(getX() + box.left + width);
                width += box.left + widget.getWidth() + box.right;
            }
            widths.add(width);
        }
        for (int i = 0; i < columns; i++) {
            int height = 0;
            for (int j = 0; j < rows; j++) {
                T widget = widgets.get(i * columns + j);
                widget.setY(getY() + box.top + height);
                height += box.top + widget.getHeight() + box.bottom;
            }
            heights.add(height);
        }
        setWidth(widths.stream().max(Integer::compareTo).orElse(0));
        setHeight(heights.stream().max(Integer::compareTo).orElse(0));
    }

    @Override
    protected void renderButton(DrawContext drawContext, int i, int j, float f) {
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                widgets.get(row * columns + column).render(drawContext, i, j, f);
            }
        }
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
                    for (T widget : widgets) {
                        widget.mouseClicked(d, e, i);
                    }
                    this.onClick(d, e);
                    return true;
                }
            }

        }
        return false;
    }

    public void setHeight(int i) {
        this.height = i;
    }
}
