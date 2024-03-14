package com.metacontent.cobblenav.client.widget;

public class ScrollerWidget extends IconButton {
    private final OnScrollerDrag onDrag;

    public ScrollerWidget(int x, int y, int width, int height, int offsetX, int offsetY, OnScrollerDrag onDrag) {
        super(x, y, width, height, offsetX, offsetY, 0, () -> {});
        this.onDrag = onDrag;
    }

    @Override
    protected void onDrag(double d, double e, double f, double g) {
        onDrag.drag(e - (double) getHeight() / 2);
    }

    @Override
    public boolean mouseDragged(double d, double e, int i, double f, double g) {
        if (this.isValidClickButton(i) && this.clicked(d, e) && g != 0) {
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
