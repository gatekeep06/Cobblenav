package com.metacontent.cobblenav.client.widget;

import com.cobblemon.mod.common.CobblemonSounds;
import com.metacontent.cobblenav.Cobblenav;
import com.metacontent.cobblenav.util.ContactSelector;
import com.metacontent.cobblenav.util.PokenavContact;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

import static com.cobblemon.mod.common.api.gui.GuiUtilsKt.blitk;

//TODO: min height

public class ContactListWidget extends ClickableWidget {
    public static final Identifier BUTTONS = new Identifier(Cobblenav.ID, "textures/gui/pokenav_item_gui_buttons.png");
    public static final Identifier BUTTONS_HOVERED = new Identifier(Cobblenav.ID, "textures/gui/pokenav_item_gui_buttons_hovered.png");
    public static final int WIDTH = 128;
    public static final int HEIGHT = 99;

    private List<ContactListItem> items;
    private final List<PokenavContact> contactList;
    private final PlayerEntity player;
    private final ContactSelector selector;

    private void createItems() {
        items = new ArrayList<>();
        for (int i = 0; i < contactList.size(); i++) {
            int index = i;
            ContactListItem item = new ContactListItem(getX(), getY() + index * 12, contactList.get(i), index,
                    () -> {
                        player.playSound(CobblemonSounds.PC_GRAB, 0.05f, 1.25f);
                        selector.setContactIndex(index);
                    },
                    getY() + getHeight() + 12, getY() - 12
            );
            items.add(item);
        }
        height = items.size() * 12;
    }

    public ContactListWidget(int x, int y, List<PokenavContact> contactList, ContactSelector selector) {
        super(x, y, WIDTH, HEIGHT, Text.literal("Contact List"));

        this.contactList = contactList;
        player = MinecraftClient.getInstance().player;
        this.selector = selector;
        createItems();
    }

    @Override
    protected void renderButton(DrawContext drawContext, int i, int j, float f) {
//        items.forEach(item -> item.renderItem(drawContext, i, j, f, selector.getContactIndex()));
        for (ContactListItem item : items) {
            item.renderItem(drawContext, i, j, f, selector.getContactIndex());
        }
    }

    public void deleteContact(PokenavContact contact) {
        contactList.remove(contact);
        createItems();
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder narrationMessageBuilder) {

    }

    @Override
    public boolean mouseClicked(double d, double e, int i) {
        if (this.active && this.visible) {
            if (this.isValidClickButton(i)) {
                boolean bl = this.clicked(d, e);
                if (bl) {
                    selector.setContactIndex(-1);
                    items.forEach(item -> item.mouseClicked(d, e, i));
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void setY(int i) {
        int deltaY = i - getY();
        items.forEach(item -> item.setY(item.getY() + deltaY));
        super.setY(i);
    }
}
