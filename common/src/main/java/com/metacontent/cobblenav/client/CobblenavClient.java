package com.metacontent.cobblenav.client;

import com.metacontent.cobblenav.client.hud.TrackArrowHudOverlay;
import com.metacontent.cobblenav.config.ClientCobblenavConfig;
import dev.architectury.event.events.client.ClientGuiEvent;

public class CobblenavClient {
    public static final TrackArrowHudOverlay TRACK_ARROW_HUD_OVERLAY = new TrackArrowHudOverlay();
    public static ClientCobblenavConfig CONFIG = ClientCobblenavConfig.init();

    public static void init() {
        ClientGuiEvent.RENDER_HUD.register(TRACK_ARROW_HUD_OVERLAY);
    }
}
