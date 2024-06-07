package com.metacontent.cobblenav.client.widget;

import com.metacontent.cobblenav.Cobblenav;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public abstract class AbstractPokenavButton extends ClickableWidget implements Clickable {
    public static final Identifier FONT = new Identifier("uniform");
    protected static final Identifier TEXTURE = new Identifier(Cobblenav.ID, "textures/gui/pokenav_item_gui_buttons.png");
    protected static final Identifier HOVERED_TEXTURE = new Identifier(Cobblenav.ID, "textures/gui/pokenav_item_gui_buttons_hovered.png");
    protected final int offsetX;
    protected final int offsetY;
    protected final PlayerEntity player;

    public AbstractPokenavButton(int x, int y, int width, int height, int offsetX, int offsetY) {
        super(x, y, width, height, Text.literal(""));
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.player = MinecraftClient.getInstance().player;
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder narrationMessageBuilder) {

    }

    @Override
    public boolean mouseClicked(double d, double e, int i) {
        if (this.active && this.visible) {
            if (this.isMainClickButton(i)) {
                boolean bl = this.clicked(d, e);
                if (bl) {
                    this.onClick(d, e);
                    return true;
                }
            }
        }
        return false;
    }

    public interface OnPressed {
        void onPressed();
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
