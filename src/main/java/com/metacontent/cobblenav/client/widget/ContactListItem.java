package com.metacontent.cobblenav.client.widget;

import com.metacontent.cobblenav.Cobblenav;
import com.metacontent.cobblenav.util.PokenavContact;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.texture.PlayerSkinProvider;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;

import static com.cobblemon.mod.common.api.gui.GuiUtilsKt.blitk;
import static com.cobblemon.mod.common.client.render.RenderHelperKt.drawScaledText;

public class ContactListItem extends ClickableWidget {
    private static final Identifier FONT = new Identifier("uniform");
    private static final Identifier TEXTURE = new Identifier(Cobblenav.ID, "textures/gui/pokenav_item_gui_buttons.png");
    private static final int MAX_WIDTH = 56;
    private final PokenavContact contact;
    private Identifier skinId;
    private final int index;
    private boolean isSelected;
    private final OnSelect action;
    private final int maxRenderY;
    private final int minRenderY;

    public ContactListItem(int x, int y, PokenavContact contact, int index, OnSelect onSelect, int maxRenderY, int minRenderY) {
        super(x, y, 120, 12, Text.literal(""));
        this.contact = contact;
        this.index = index;
        this.action = onSelect;
        this.maxRenderY = maxRenderY;
        this.minRenderY = minRenderY;

        PlayerSkinProvider skinProvider = MinecraftClient.getInstance().getSkinProvider();
        MinecraftProfileTexture texture = skinProvider.getTextures(contact.getProfile()).get(MinecraftProfileTexture.Type.SKIN);
        if (texture != null) {
            skinId = skinProvider.loadSkin(texture, MinecraftProfileTexture.Type.SKIN);
        }
        else {
            skinId = DefaultSkinHelper.getTexture(Uuids.getUuidFromProfile(contact.getProfile()));
        }
    }

    public void renderItem(DrawContext drawContext, int i, int j, float f, int selectedIndex) {
        this.isSelected = selectedIndex == index;
        render(drawContext, i, j, f);
    }

    @Override
    protected void renderButton(DrawContext drawContext, int i, int j, float f) {
        if (isVisible()) {
            Style style = Style.EMPTY.withBold(isSelected).withItalic(isSelected);

            if (isSelected) {
                blitk(drawContext.getMatrices(), TEXTURE, getX(), getY() + 1, 7, 4, 0, 72, 256,
                        256, 0, 1, 1, 1, 1, false, 1);
            }

            drawScaledText(drawContext, FONT, Text.literal(contact.getProfile().getName()).setStyle(style),
                    getX() + 18, getY(), 1, 1,
                    MAX_WIDTH, isHovered() ? 0xD3D3D3 : 0xFFFFFF, false, isHovered(), i, j);

            drawScaledText(drawContext, FONT, Text.literal(contact.getTitle()).setStyle(style),
                    getX() + MAX_WIDTH + 20, getY(), 1, 1,
                    MAX_WIDTH, isHovered() ? 0xD3D3D3 : 0xFFFFFF, false, isHovered(), i, j);

            if (skinId != null) {
                blitk(drawContext.getMatrices(), skinId, getX() + 6, getY() + 1, 8, 8, 8, 8, 64, 64,
                        0, 1, 1, 1, 1, false, 1);
            }
        }
    }

    private boolean isVisible() {
        return getY() >= minRenderY && getY() <= maxRenderY;
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
                    this.onClick(d, e);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onClick(double d, double e) {
        action.onSelect();
    }

    public interface OnSelect {
        void onSelect();
    }
}
