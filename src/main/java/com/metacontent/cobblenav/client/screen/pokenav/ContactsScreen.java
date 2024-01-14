package com.metacontent.cobblenav.client.screen.pokenav;

import com.cobblemon.mod.common.CobblemonSounds;
import com.metacontent.cobblenav.client.screen.AbstractPokenavItemScreen;
import com.metacontent.cobblenav.client.widget.PokenavItemButton;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import static com.cobblemon.mod.common.api.gui.GuiUtilsKt.blitk;

public class ContactsScreen extends AbstractPokenavItemScreen {
    private int borderX;
    private int borderY;

    private PokenavItemButton backButton;

    protected ContactsScreen() {
        super(Text.literal("Contacts"));
    }

    @Override
    protected void init() {
        super.init();

        borderX = (width - BORDER_WIDTH) / 2;
        borderY = (height - BORDER_HEIGHT) / 2 - 10;

        backButton = new PokenavItemButton(borderX + BORDER_DEPTH + 3, borderY + BORDER_HEIGHT - BORDER_DEPTH - 12, 11, 11, 73, 0, 0, 0,
                Text.literal(""),
                BUTTONS,
                BUTTONS_HOVERED,
                () -> {
                    player.playSound(CobblemonSounds.PC_CLICK, 0.1f, 1.25f);
                    MinecraftClient.getInstance().setScreen(new MainScreen());
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

        super.render(drawContext, i, j, f);
    }

    @Override
    public boolean mouseClicked(double d, double e, int i) {
        backButton.mouseClicked(d, e, i);
        return super.mouseClicked(d, e, i);
    }
}
