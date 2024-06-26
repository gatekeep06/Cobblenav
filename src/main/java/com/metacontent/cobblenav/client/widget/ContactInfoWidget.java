package com.metacontent.cobblenav.client.widget;

import com.metacontent.cobblenav.Cobblenav;
import com.metacontent.cobblenav.networking.CobblenavPackets;
import com.metacontent.cobblenav.util.PokenavContact;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static com.cobblemon.mod.common.api.gui.GuiUtilsKt.blitk;
import static com.cobblemon.mod.common.client.render.RenderHelperKt.drawScaledText;

public class ContactInfoWidget implements Drawable, Widget {
    private static final Identifier TEXTURE = new Identifier(Cobblenav.ID, "textures/gui/contact_screen_widgets.png");
    private static final Identifier FONT = null;
    public static final int TEAM_BOX_HEIGHT = 79;
    public static final int TEAM_BOX_WIDTH = 75;
    public static final int NAME_BOX_HEIGHT = 21;
    public static final int NAME_BOX_WIDTH = 75;
    private static final int ANIMATION_LENGTH = 5;

    private int x;
    private int y;
    private PokenavContact contact;
    private int ticker;

    public ContactInfoWidget(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void render(DrawContext drawContext, int i, int j, float f) {
        int animX = (int) (x  - 15 * ((double)ticker / (double)ANIMATION_LENGTH));
        blitk(drawContext.getMatrices(), TEXTURE, animX, y, NAME_BOX_HEIGHT, NAME_BOX_WIDTH, 0, 189, 256,
                256, 0, 1, 1, 1, 1, false, 1);
        blitk(drawContext.getMatrices(), TEXTURE, animX, y + NAME_BOX_HEIGHT + 3, TEAM_BOX_HEIGHT, TEAM_BOX_WIDTH, 0, 109, 256,
                256, 0, 1, 1, 1, 1, false, 1);
        blitk(drawContext.getMatrices(), TEXTURE, x, y, NAME_BOX_HEIGHT, 1, 0, 189, 256,
                256, 0, 1, 1, 1, 1, false, 1);
        blitk(drawContext.getMatrices(), TEXTURE, x, y + NAME_BOX_HEIGHT + 3, TEAM_BOX_HEIGHT,1, 0, 109, 256,
                256, 0, 1, 1, 1, 1, false, 1);

        drawScaledText(drawContext, FONT, Text.literal(contact.getTitleOrName()).setStyle(Style.EMPTY.withBold(true)),
                animX + NAME_BOX_WIDTH / 2, y + NAME_BOX_HEIGHT / 2 - 4, 1f, 1, NAME_BOX_WIDTH - 6,
                0xFFFFFF, true, false, i, j);

        drawScaledText(drawContext, FONT,
                Text.translatable("gui.cobblenav.pokenav_item.contact_stats", contact.getWinnings(), contact.getLosses()),
                animX + TEAM_BOX_WIDTH / 2, y + NAME_BOX_HEIGHT + 8, 1f, 1, (int) (TEAM_BOX_WIDTH / 1.1),
                0xFFFFFF, true, false, i, j);

        AtomicInteger teamY = new AtomicInteger(y + NAME_BOX_HEIGHT + 19);
        contact.getTeam().forEach(member -> {
            drawScaledText(drawContext, FONT,
                    Text.translatable("cobblemon.species." + member.name + ".name"),
                    animX + 3, teamY.get() + 2, 0.6f, 1, (int) (0.75 * TEAM_BOX_WIDTH) - 2,
                    0xFFFFFF, false, false, i, j);
            drawScaledText(drawContext, FONT,
                    Text.literal(member.level + "lvl"),
                    animX + TEAM_BOX_WIDTH - 20, teamY.get() + 2, 0.6f, 1, (int) (0.5 * TEAM_BOX_WIDTH),
                    0xFFFFFF, false, false, i, j);
            teamY.addAndGet(10);
        });

        if (ticker > 0) {
            ticker--;
        }
    }

    public void setContact(PokenavContact contact) {
        this.contact = contact;
        ticker = ANIMATION_LENGTH;
    }

    public PokenavContact getContact() {
        return contact;
    }

    @Override
    public void setX(int i) {
        x = i;
    }

    @Override
    public void setY(int i) {
        y = i;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    public void deleteContact() {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeString(contact.getKey());
        ClientPlayNetworking.send(CobblenavPackets.DELETE_CONTACT_PACKET, buf);
    }

    @Override
    public void forEachChild(Consumer<ClickableWidget> consumer) {

    }
}
