package com.metacontent.cobblenav.client.screen.pokenav;

import com.metacontent.cobblenav.client.screen.AbstractPokenavItemScreen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class StatsScreen extends AbstractPokenavItemScreen {
    protected StatsScreen() {
        super(Text.literal("Stats"));
    }

    @Override
    public void render(DrawContext drawContext, int i, int j, float f) {
        renderBackground(drawContext);

        super.render(drawContext, i, j, f);
    }
}
