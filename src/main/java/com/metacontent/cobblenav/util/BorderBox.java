package com.metacontent.cobblenav.util;

public class BorderBox {
    public final int left;
    public final int top;
    public final int right;
    public final int bottom;

    public BorderBox(int width) {
        this.left = width;
        this.top = width;
        this.right = width;
        this.bottom = width;
    }

    public BorderBox(int horizontal, int vertical) {
        this.left = horizontal;
        this.right = horizontal;
        this.top = vertical;
        this.bottom = vertical;
    }

    public BorderBox(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }
}
