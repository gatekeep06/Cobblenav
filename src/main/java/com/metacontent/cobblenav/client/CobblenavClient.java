package com.metacontent.cobblenav.client;

import com.cobblemon.mod.common.client.CobblemonClient;
import com.metacontent.cobblenav.client.hud.TrackArrowHudOverlay;
import com.metacontent.cobblenav.client.screen.pokenav.LocationScreen;
import com.metacontent.cobblenav.client.screen.pokenav.MainScreen;
import com.metacontent.cobblenav.config.ClientCobblenavConfig;
import com.metacontent.cobblenav.item.PokenavItem;
import com.metacontent.cobblenav.networking.CobblenavPackets;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class CobblenavClient implements ClientModInitializer {
    public static final TrackArrowHudOverlay TRACK_ARROW_HUD_OVERLAY = new TrackArrowHudOverlay();
    public static ClientCobblenavConfig CONFIG = ClientCobblenavConfig.init();
    private static KeyBinding pokenavKey;
    private static KeyBinding locationKey;

    @Override
    public void onInitializeClient() {
        locationKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.cobblenav.open_location",
                InputUtil.Type.KEYSYM, GLFW.GLFW_DONT_CARE,
                "category.cobblenav"
        ));
        pokenavKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.cobblenav.open_pokenav",
                InputUtil.Type.KEYSYM, GLFW.GLFW_DONT_CARE,
                "category.cobblenav"
        ));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) {
                return;
            }
            if (client.currentScreen != null || CobblemonClient.INSTANCE.getBattle() != null) {
                return;
            }
            if (!client.player.getInventory().containsAny(stack -> stack.getItem() instanceof PokenavItem)) {
                return;
            }
            if (pokenavKey.wasPressed()) {
                client.setScreen(new MainScreen());
            }
            else if (locationKey.wasPressed()) {
                client.setScreen(new LocationScreen());
            }
        });
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
