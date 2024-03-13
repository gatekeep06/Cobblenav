package com.metacontent.cobblenav.client.widget;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

public class ScrollableViewWidget<T extends ClickableWidget> extends ClickableWidget {
    private final T widget;
    private final float scrollSize;
    private double scrollY = 0;
    private final ScrollerWidget scrollerWidget;

    public ScrollableViewWidget(T widget, int width, int height, float scrollSize) {
        super(widget.getX(), widget.getY(), width, height, Text.literal(""));
        this.widget = widget;
        this.scrollSize = scrollSize;
        this.scrollerWidget = new ScrollerWidget(getX() + getWidth(), getY(), 4, 15, 73, 24, deltaY -> scroll((-deltaY * 2.0 / scrollSize)));
    }

    @Override
    protected void renderButton(DrawContext drawContext, int i, int j, float f) {
        if (this.visible) {
            drawContext.enableScissor(getX(), getY(), getX() + getWidth(), getY() + getHeight());
            widget.render(drawContext, i, j, f);
            drawContext.disableScissor();
            scrollerWidget.render(drawContext, i, j, f);
        }
    }

    @Override
    public boolean mouseClicked(double d, double e, int i) {
        if (this.active && this.visible) {
            if (this.isValidClickButton(i)) {
                boolean bl = this.clicked(d, e);
                if (bl) {
                    widget.mouseClicked(d, e, i);
                    this.onClick(d, e);
                    return true;
                }
            }

        }
        return false;
    }

    @Override
    public boolean mouseDragged(double d, double e, int i, double f, double g) {
        scrollerWidget.mouseDragged(d, e, i, f, g);
        return super.mouseDragged(d, e, i, f, g);
    }

    @Override
    public boolean mouseScrolled(double d, double e, double f) {
        if (this.active && this.visible) {
            if (this.clicked(d, e)) {
                return scroll(f);
            }
        }
        return false;
    }

    private boolean scroll(double f) {
        if (widget.getHeight() > getHeight()) {
            scrollY -= f * scrollSize;
            if (scrollY < 0) {
                scrollY = 0;
            } else if (scrollY > widget.getHeight() - getHeight()) {
                scrollY = widget.getHeight() - getHeight();
            }
            scrollerWidget.setY((int) (getY() + (getHeight() - 5 - scrollerWidget.getHeight()) * (scrollY / (widget.getHeight() - getHeight()))));
            widget.setY((int) (getY() - scrollY));
            return true;
        }
        return false;
    }

    public void resetScrollY() {
        this.scrollY = 0;
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder narrationMessageBuilder) {

    }
}
