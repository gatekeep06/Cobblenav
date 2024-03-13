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

public class ContactListWidget extends ClickableWidget {
    public static final Identifier TEXTURE = new Identifier(Cobblenav.ID, "textures/gui/contact_screen_widgets.png");
    public static final Identifier BUTTONS = new Identifier(Cobblenav.ID, "textures/gui/pokenav_item_gui_buttons.png");
    public static final Identifier BUTTONS_HOVERED = new Identifier(Cobblenav.ID, "textures/gui/pokenav_item_gui_buttons_hovered.png");
    public static final int WIDTH = 128;
    public static final int HEIGHT = 108;

    private final List<PokenavContact> contactList;
    private int listPage = 0;
    private List<ContactListItem> items;
    private final PlayerEntity player;
    private final ContactSelector selector;

    private final IconButton decreaseListPageButton;
    private final IconButton increaseListPageButton;

    private void createItems() {
        items = new ArrayList<>();
        int x = getX() + 14;
        int y = getY() + 12;
        while (!contactList.isEmpty() && contactList.size() - 7 * listPage <= 0) {
            listPage--;
        }
        int maxI = contactList.size() - 7 * listPage < 7 ? contactList.size() : 7 * (listPage + 1);
        for (int i = 7 * listPage; i < maxI; i++) {
            int index = i;
            ContactListItem item = new ContactListItem(x, y, contactList.get(i).getName(), contactList.get(i).getTitle(), index,
                    () -> {
                        player.playSound(CobblemonSounds.PC_GRAB, 0.05f, 1.25f);
                        selector.setContactIndex(index);
                    }
            );
            items.add(item);
            y += 12;
        }
    }

    public ContactListWidget(int x, int y, List<PokenavContact> contactList, ContactSelector selector) {
        super(x, y, WIDTH, HEIGHT, Text.literal("Contact List"));

        this.contactList = contactList;
        player = MinecraftClient.getInstance().player;
        this.selector = selector;
        createItems();

        decreaseListPageButton = new IconButton(x + width / 2 - 2, y + 5, 7, 5, 114, 0, 0,
                () -> {
                    player.playSound(CobblemonSounds.PC_CLICK, 0.05f, 1.25f);
                    if (listPage - 1 >= 0) {
                        listPage--;
                        createItems();
                    }
                }
        );
        increaseListPageButton = new IconButton(x + width / 2 - 2, y + height - 11, 7, 5, 107, 0, 0,
                () -> {
                    player.playSound(CobblemonSounds.PC_CLICK, 0.05f, 1.25f);
                    int pagesAmount = contactList.size() % 7 > 0 ? contactList.size() / 7 + 1 : contactList.size() / 7;
                    if (listPage + 1 < pagesAmount) {
                        listPage++;
                        createItems();
                    }
                }
        );
    }

    @Override
    protected void renderButton(DrawContext drawContext, int i, int j, float f) {
        blitk(drawContext.getMatrices(), TEXTURE, getX(), getY(), getHeight(), getWidth(), 0, 0, 256,
                256, 0, 1, 1, 1, 1, false, 1);
        decreaseListPageButton.render(drawContext, i, j, f);
        increaseListPageButton.render(drawContext, i, j, f);
        //items.forEach(item -> item.renderItem(drawContext, i, j, f, selector.getContactIndex()));
        for (int index = 0; index < Math.min(items.size(), 14); index++) {
            items.get(index).renderItem(drawContext, i, j, f, selector.getContactIndex());
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
                    boolean b1 = decreaseListPageButton.mouseClicked(d, e, i);
                    boolean b2 = increaseListPageButton.mouseClicked(d, e, i);
                    if (!(b1 || b2)) {
                        selector.setContactIndex(-1);
                    }
                    items.forEach(item -> item.mouseClicked(d, e, i));
                    return true;
                }
            }
        }
        return false;
    }
}
