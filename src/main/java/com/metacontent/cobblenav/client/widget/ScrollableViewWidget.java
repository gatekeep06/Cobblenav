package com.metacontent.cobblenav.client.widget;

import com.metacontent.cobblenav.Cobblenav;
import com.metacontent.cobblenav.client.CobblenavClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.minecraft.util.math.ColorHelper;

public class ScrollableViewWidget<T extends ClickableWidget> extends ClickableWidget implements Clickable {
    private static final int SHADOW = ColorHelper.Argb.getArgb(40, 0, 0, 0);
    private final T widget;
    private final float scrollSize;
    private double scrollY = 0;
    private final ScrollerWidget scrollerWidget;
    private final float scale;

    public ScrollableViewWidget(T widget, int width, int height, float scrollSize) {
        super(widget.getX(), widget.getY(), width, height, Text.literal(""));
        this.widget = widget;
        this.scrollSize = scrollSize;
        this.scrollerWidget = new ScrollerWidget(getX() + getWidth() + 3, getY() + 1, 0, this::dragScroller);
        this.scale = CobblenavClient.CONFIG.screenScale;
    }

    @Override
    protected void renderButton(DrawContext drawContext, int i, int j, float f) {
        boolean isWidgetBigger = getHeight() < widget.getHeight();
        scrollerWidget.setHeight((int) (getHeight() * (isWidgetBigger ? ((float) getHeight() / (float) widget.getHeight()) : 1f)) - 2);
        if (this.visible) {
            drawContext.enableScissor((int) ((getX() - getWidth()) * scale), (int) (getY() * scale),
                    (int) ((getX() + getWidth() * 2) * scale), (int) ((getY() + getHeight()) * scale));
            widget.render(drawContext, i, j, f);
            drawContext.disableScissor();
            if (isWidgetBigger) {
                drawContext.fill(getX() + getWidth() + 3, getY() + 1, getX() + getWidth() + 5, getY() + getHeight() - 1, SHADOW);
                scrollerWidget.render(drawContext, i, j, f);
            }
        }
    }

    @Override
    public boolean mouseClicked(double d, double e, int i) {
        if (this.active && this.visible) {
            if (this.isMainClickButton(i)) {
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
        return (getY() + 1 + (getHeight() - 2 - scrollerWidget.getHeight()) * (scrollY / (widget.getHeight() - getHeight())));
    }

    private void dragScroller(double deltaY) {
        if (widget.getHeight() > getHeight()) {
            scrollY = ((deltaY - getY()) * (widget.getHeight() - getHeight())) / (getHeight() - 2 - scrollerWidget.getHeight());
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
        widget.setY((int) (getY() - scrollY));
        scrollerWidget.setY((int) calcScrollerPos(scrollY));
    }

    public void resetScrollY() {
        scrollY = 0;
        widget.setY(getY());
        scrollerWidget.setY(getY() + 1);
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder narrationMessageBuilder) {

    }
}
