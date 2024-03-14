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
        this.scrollerWidget = new ScrollerWidget(getX() + getWidth(), getY(), 4, 15, 73, 24, this::dragScroller);
    }

    @Override
    protected void renderButton(DrawContext drawContext, int i, int j, float f) {
        if (this.visible) {
            drawContext.enableScissor(getX() - getWidth(), getY(), getX() + getWidth() * 2, getY() + getHeight());
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
                return middleButtonScroll(f, scrollSize);
            }
        }
        return false;
    }

    private double calcScrollerPos(double scrollY) {
        return (getY() + (getHeight() - 5 - scrollerWidget.getHeight()) * (scrollY / (widget.getHeight() - getHeight())));
    }

    private void dragScroller(double deltaY) {
        if (widget.getHeight() > getHeight()) {
            scrollY = ((deltaY - getY()) * (widget.getHeight() - getHeight())) / (getHeight() - 5 - scrollerWidget.getHeight());
            scroll();
        }
    }

    private boolean middleButtonScroll(double f, float scrollSize) {
        if (widget.getHeight() > getHeight()) {
            scrollY -= f * scrollSize;
            scroll();
            return true;
        }
        return false;
    }

    private void scroll() {
        if (scrollY < 0) {
            scrollY = 0;
        }
        else if (scrollY > widget.getHeight() - getHeight()) {
            scrollY = widget.getHeight() - getHeight();
        }
        scrollerWidget.setY((int) calcScrollerPos(scrollY));
        widget.setY((int) (getY() - scrollY));
    }

    public void resetScrollY() {
        this.scrollY = 0;
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder narrationMessageBuilder) {

    }
}
