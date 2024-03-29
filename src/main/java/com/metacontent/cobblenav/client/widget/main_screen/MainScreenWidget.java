package com.metacontent.cobblenav.client.widget.main_screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;

public abstract class MainScreenWidget implements Drawable {
    @Override
    public void render(DrawContext drawContext, int i, int j, float f) {
        renderWidget(drawContext, i, j, f);
    }

    protected abstract void renderWidget(DrawContext drawContext, int i, int j, float f);
}
