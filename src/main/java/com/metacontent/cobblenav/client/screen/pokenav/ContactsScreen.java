package com.metacontent.cobblenav.client.screen.pokenav;

import com.metacontent.cobblenav.client.screen.AbstractPokenavItemScreen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class ContactsScreen extends AbstractPokenavItemScreen {
    protected ContactsScreen() {
        super(Text.literal("Contacts"));
    }

    @Override
    public void render(DrawContext drawContext, int i, int j, float f) {
        renderBackground(drawContext);

        super.render(drawContext, i, j, f);
    }
}
