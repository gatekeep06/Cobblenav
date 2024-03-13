package com.metacontent.cobblenav.client.screen.pokenav;

import com.cobblemon.mod.common.CobblemonSounds;
import com.metacontent.cobblenav.client.screen.AbstractPokenavItemScreen;
import com.metacontent.cobblenav.client.widget.ContactInfoWidget;
import com.metacontent.cobblenav.client.widget.ContactListWidget;
import com.metacontent.cobblenav.client.widget.IconButton;
import com.metacontent.cobblenav.networking.CobblenavPackets;
import com.metacontent.cobblenav.util.CobblenavNbtHelper;
import com.metacontent.cobblenav.util.ContactSelector;
import com.metacontent.cobblenav.util.PokenavContact;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;

import java.util.*;

import static com.cobblemon.mod.common.api.gui.GuiUtilsKt.blitk;

@Environment(EnvType.CLIENT)
public class ContactsScreen extends AbstractPokenavItemScreen implements ContactSelector {
    private int borderX;
    private int borderY;
    private List<PokenavContact> contacts;
    private int selectedContactIndex;

    private IconButton backButton;
    private IconButton deleteButton;
    private ContactListWidget contactListWidget;
    private ContactInfoWidget contactInfoWidget;

    protected ContactsScreen() {
        super(Text.literal("Contacts"));
    }

    private void requestContactData() {
        selectedContactIndex = -1;
        contacts = new ArrayList<>();
        ClientPlayNetworking.send(CobblenavPackets.CONTACT_DATA_PACKET_SERVER, PacketByteBufs.create());
    }

    public void createContactList(NbtCompound nbt) {
        Set<String> keys = nbt.getKeys();
        keys.forEach(key -> {
            if (!Objects.equals(key, "title")) {
                NbtCompound contactData = nbt.getCompound(key);
                PokenavContact pokenavContact = CobblenavNbtHelper.toPokenavContact(contactData, key);
                contacts.add(pokenavContact);
            }
        });
        contactListWidget = new ContactListWidget(borderX - BORDER_DEPTH + BORDER_WIDTH - ContactListWidget.WIDTH,
                borderY + BORDER_DEPTH + 24, contacts, this);
        contactInfoWidget = new ContactInfoWidget(borderX + BORDER_DEPTH, borderY + BORDER_DEPTH + 27);
    }

    @Override
    protected void init() {
        super.init();

        requestContactData();

        borderX = (width - BORDER_WIDTH) / 2;
        borderY = (height - BORDER_HEIGHT) / 2 - 10;

        backButton = new IconButton(borderX + BORDER_DEPTH + 3, borderY + BORDER_HEIGHT - BORDER_DEPTH - 12,
                11, 11, 73, 0, 0,
                () -> {
                    player.playSound(CobblemonSounds.PC_CLICK, 0.1f, 1.25f);
                    MinecraftClient.getInstance().setScreen(new MainScreen());
                }
        );
        deleteButton = new IconButton(borderX + BORDER_WIDTH - BORDER_DEPTH - 14, borderY + BORDER_HEIGHT - BORDER_DEPTH - 12,
                11, 11, 109, 12, 0,
                () -> {
                    player.playSound(CobblemonSounds.PC_RELEASE, 0.1f, 1.25f);
                    selectedContactIndex = -1;
                    contactListWidget.deleteContact(contactInfoWidget.getContact());
                    contactInfoWidget.deleteContact();
                }
        );
    }

    @Override
    public void render(DrawContext drawContext, int i, int j, float f) {
        renderBackground(drawContext);

        MatrixStack matrixStack = drawContext.getMatrices();

        blitk(matrixStack, BACKGROUND,
                borderX + BORDER_DEPTH, borderY + BORDER_DEPTH + 20, BORDER_HEIGHT - 2 * BORDER_DEPTH - 32, BORDER_WIDTH - 2 * BORDER_DEPTH, 0, 131, 256,
                256, 0, 1, 1, 1, 1, false, 1);

        blitk(matrixStack, BORDERS,
                borderX, borderY + BORDER_HEIGHT - BORDER_DEPTH - 14, 14, BORDER_WIDTH, 0, BORDER_HEIGHT + 1, 256,
                256, 0, 1,1,1,1,false,1);

        backButton.render(drawContext, i, j, f);
        if (contactListWidget != null) {
            contactListWidget.render(drawContext, i, j, f);
            if (selectedContactIndex != -1) {
                contactInfoWidget.render(drawContext, i, j, f);
                deleteButton.render(drawContext, i, j, f);
            }
        }

        super.render(drawContext, i, j, f);
    }

    @Override
    public boolean mouseClicked(double d, double e, int i) {
        backButton.mouseClicked(d, e, i);
        if (contactListWidget != null) {
            contactListWidget.mouseClicked(d, e, i);
            if (selectedContactIndex != -1) {
                deleteButton.mouseClicked(d, e, i);
            }
        }
        return super.mouseClicked(d, e, i);
    }

    @Override
    public void setContactIndex(int index) {
        selectedContactIndex = index;
        if (index != -1) {
            contactInfoWidget.setContact(contacts.get(index));
        }
    }

    @Override
    public int getContactIndex() {
        return selectedContactIndex;
    }
}
