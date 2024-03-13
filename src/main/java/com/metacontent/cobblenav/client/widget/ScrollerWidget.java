package com.metacontent.cobblenav.client.widget;

public class ScrollerWidget extends IconButton {
    private final OnScrollerDrag onDrag;

    public ScrollerWidget(int x, int y, int width, int height, int offsetX, int offsetY, OnScrollerDrag onDrag) {
        super(x, y, width, height, offsetX, offsetY, 0, () -> {});
        this.onDrag = onDrag;
    }

    @Override
    protected void onDrag(double d, double e, double f, double g) {
        onDrag.drag(g);
    }

    public interface OnScrollerDrag {
        void drag(double deltaY);
    }
}
