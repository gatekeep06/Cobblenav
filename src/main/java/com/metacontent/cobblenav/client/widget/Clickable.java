package com.metacontent.cobblenav.client.widget;

import net.minecraft.client.MinecraftClient;

public interface Clickable {
    default boolean isMainClickButton(int clickedButton) {
        int button = MinecraftClient.getInstance().options.useKey.matchesMouse(1) ? 0 : 1;
        return clickedButton == button;
    }

    default boolean isSecondaryClickButton(int clickedButton) {
        int button = MinecraftClient.getInstance().options.useKey.matchesMouse(1) ? 1 : 0;
        return clickedButton == button;
    }
}
