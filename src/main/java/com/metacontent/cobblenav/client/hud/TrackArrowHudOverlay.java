package com.metacontent.cobblenav.client.hud;

import com.metacontent.cobblenav.Cobblenav;
import com.metacontent.cobblenav.client.CobblenavClient;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

import static com.cobblemon.mod.common.api.gui.GuiUtilsKt.blitk;

public class TrackArrowHudOverlay implements HudRenderCallback {
    private static final Identifier HUD_ELEMENTS = new Identifier(Cobblenav.ID, "textures/gui/hud_elements.png");

    private int trackedEntityId = -1;

    public void setTrackedEntityId(int id) {
        trackedEntityId = id;
    }

    public void resetTracking() {
        trackedEntityId = -1;
    }

    public boolean isTracking() {
        return trackedEntityId != -1;
    }

    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta) {
        try {
            if (isTracking()) {
                MinecraftClient client = MinecraftClient.getInstance();
                PlayerEntity player = client.player;
                if (client.world == null || player == null) {
                    return;
                }
                Entity trackedEntity = client.world.getEntityById(trackedEntityId);
                if (trackedEntity == null) {
                    resetTracking();
                    return;
                }

                float width = client.getWindow().getScaledWidth();
                float height = client.getWindow().getScaledHeight();
                MatrixStack matrixStack = drawContext.getMatrices();
                float x = width / 2;
                float y = height - CobblenavClient.CONFIG.trackArrowVerticalOffset;

                double trackedPosX = trackedEntity.getX();
                double trackedPosZ = trackedEntity.getZ();
                double tanX = trackedPosX - player.getX();
                double tanZ = trackedPosZ - player.getZ();

                float angle = (float) Math.toDegrees(Math.atan2(tanZ, tanX));

                matrixStack.push();
                matrixStack.translate(x, y, -10);
                matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(135 + angle - player.headYaw));
                blitk(matrixStack, HUD_ELEMENTS, -7.5, -7.5, 28, 28, 0, 0, 256, 256,
                        0, 1, 1, 1, 1, false, 1);
                matrixStack.pop();

                blitk(matrixStack, HUD_ELEMENTS, x - 7.5, y - 7.5, 15, 15, 30, 0, 256, 256,
                        0, 1, 1, 1, 1, false, 1);
            }
        }
        catch (Throwable e) {
            Cobblenav.LOGGER.error(e.getMessage(), e);
        }
    }
}
