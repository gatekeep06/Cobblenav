package com.metacontent.cobblenav.client.widget;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.ColorHelper;

public class ScrollerWidget extends AbstractPokenavButton {
    private static final int COLOR = ColorHelper.Argb.getArgb(255, 200, 200, 200);
    private static final int HOVERED_COLOR = ColorHelper.Argb.getArgb(255, 255, 255, 255);
    private final OnScrollerDrag onDrag;

    public ScrollerWidget(int x, int y, int height, OnScrollerDrag onDrag) {
        super(x, y, 2, height, 0, 0);
        this.onDrag = onDrag;
    }


    @Override
    protected void renderButton(DrawContext drawContext, int i, int j, float f) {
        drawContext.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(),
                isHovered() ? HOVERED_COLOR : COLOR);
    }

    @Override
    protected void onDrag(double d, double e, double f, double g) {
        onDrag.drag(e - (double) getHeight() / 2);
    }

    @Override
    public boolean mouseDragged(double d, double e, int i, double f, double g) {
        if (this.isMainClickButton(i) && this.clicked(d, e) && g != 0) {
            this.onDrag(d, e, f, g);
            return true;
        }
        return false;
    }

    @Override
    protected boolean clicked(double d, double e) {
        return this.active && this.visible
                && d >= (double)(this.getX() - 10) && e >= (double)(this.getY() - 10)
                && d < (double)(this.getX() + this.width + 10) && e < (double)(this.getY() + this.height + 10);
    }

    public interface OnScrollerDrag {
        void drag(double deltaY);
    }
}
