package com.metacontent.cobblenav.client;

import com.metacontent.cobblenav.client.hud.TrackArrowHudOverlay;
import com.metacontent.cobblenav.config.ClientCobblenavConfig;
import com.metacontent.cobblenav.networking.CobblenavPackets;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class CobblenavClient implements ClientModInitializer {
    public static final TrackArrowHudOverlay TRACK_ARROW_HUD_OVERLAY = new TrackArrowHudOverlay();
    public static ClientCobblenavConfig CONFIG = ClientCobblenavConfig.init();

    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
                dispatcher.register(ClientCommandManager.literal("cobblenavclient")
                        .then(ClientCommandManager.literal("reloadClientConfig")
                                .executes(context -> {
                                    reloadConfig();
                                    return 1;
                                }))));

        CobblenavPackets.registerS2CPackets();
        HudRenderCallback.EVENT.register(TRACK_ARROW_HUD_OVERLAY);
    }

    public static void reloadConfig() {
        CONFIG = ClientCobblenavConfig.init();
    }
}
