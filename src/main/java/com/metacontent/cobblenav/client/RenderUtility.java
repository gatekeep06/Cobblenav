package com.metacontent.cobblenav.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import org.joml.Quaternionf;

import static com.metacontent.cobblenav.client.screen.AbstractPokenavItemScreen.BORDER_HEIGHT;

public class RenderUtility {
    public static void renderPlayer(DrawContext drawContext, int x, int y, PlayerEntity player) {
        Quaternionf quaternionf = (new Quaternionf()).rotateZ(3.1415927F);
        Quaternionf quaternionf2 = (new Quaternionf()).rotateX(20.0F * 0.017453292F);
        quaternionf.mul(quaternionf2);
        float m = player.bodyYaw;
        float n = player.getYaw();
        float o = player.getPitch();
        float p = player.prevHeadYaw;
        float q = player.headYaw;
        player.bodyYaw = 190.0F;
        player.setYaw(220.0F);
        player.setPitch(0.0F);
        player.headYaw = 180F;
        player.prevHeadYaw = player.getYaw();

        drawContext.getMatrices().push();
        drawContext.getMatrices().translate(0f, 0f, 100f);
        InventoryScreen.drawEntity(drawContext, x, y + BORDER_HEIGHT / 2, 20, (new Quaternionf()).rotateZ(3.1415927F), (new Quaternionf()).rotateX(120.0F * 0.017453292F), player);
        drawContext.getMatrices().pop();

        player.bodyYaw = m;
        player.setYaw(n);
        player.setPitch(o);
        player.prevHeadYaw = p;
        player.headYaw = q;
    }
}
