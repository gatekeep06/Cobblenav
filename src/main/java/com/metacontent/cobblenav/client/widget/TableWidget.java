package com.metacontent.cobblenav.client.widget;

import com.metacontent.cobblenav.util.BorderBox;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TableWidget<T extends ClickableWidget> extends ClickableWidget {
    private final int columns;
    private int rows;
    private List<T> widgets;
    private final BorderBox box;

    public TableWidget(int x, int y, int columns, int rows, BorderBox box) {
        super(x, y, 0, 0, Text.literal(""));
        this.columns = columns;
        this.rows = rows;
        this.box = box;
    }

    public void calcRows(int size) {
        this.rows = size / columns + (size % columns == 0 ? 0 : 1);
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public void setWidgets(List<T> widgets) {
        this.widgets = widgets;
        List<Integer> widths = new ArrayList<>();
        List<Integer> heights = new ArrayList<>();
        for (int i = 0; i < columns; i++) {
            heights.add(0);
        }
        Iterator<T> iterator = widgets.iterator();
        for (int i = 0; i < rows; i++) {
            int width = 0;
            for (int j = 0; j < columns; j++) {
                if (iterator.hasNext()) {
                    T widget = iterator.next();
                    widget.setX(getX() + box.left + width);
                    widget.setY(getY() + box.top + heights.get(j));
                    width += box.left + widget.getWidth() + box.right;
                    int height = box.top + widget.getHeight() + box.bottom;
                    heights.set(j, heights.get(j) + height);
                }
            }
            widths.add(width);
        }
        setWidth(widths.stream().max(Integer::compareTo).orElse(0));
        setHeight(heights.stream().max(Integer::compareTo).orElse(0));
    }

    @Override
    protected void renderButton(DrawContext drawContext, int i, int j, float f) {
        Iterator<T> iterator = widgets.iterator();
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                if (iterator.hasNext()) {
                    iterator.next().render(drawContext, i, j, f);
                }
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

    @Override
    public void setX(int i) {
        super.setX(i);
        setWidgets(widgets);
    }

    @Override
    public void setY(int i) {
        super.setY(i);
        setWidgets(widgets);
    }

    public void setHeight(int i) {
        this.height = i;
    }
}