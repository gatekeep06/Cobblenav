package com.metacontent.cobblenav.client;

import com.metacontent.cobblenav.client.hud.TrackArrowHudOverlay;
import com.metacontent.cobblenav.networking.CobblenavPackets;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class CobblenavClient implements ClientModInitializer {
    public static final TrackArrowHudOverlay TRACK_ARROW_HUD_OVERLAY = new TrackArrowHudOverlay();

    @Override
    public void onInitializeClient() {
        CobblenavPackets.registerS2CPackets();
        HudRenderCallback.EVENT.register(TRACK_ARROW_HUD_OVERLAY);
    }
}
