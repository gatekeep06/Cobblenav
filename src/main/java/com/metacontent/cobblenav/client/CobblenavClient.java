package com.metacontent.cobblenav.client;

import com.metacontent.cobblenav.networking.CobblenavPackets;
import net.fabricmc.api.ClientModInitializer;

public class CobblenavClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        CobblenavPackets.registerS2CPackets();
    }
}
