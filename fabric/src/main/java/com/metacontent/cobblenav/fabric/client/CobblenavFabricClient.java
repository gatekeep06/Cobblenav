package com.metacontent.cobblenav.fabric.client;

import com.metacontent.cobblenav.client.CobblenavClient;
import net.fabricmc.api.ClientModInitializer;

public class CobblenavFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CobblenavClient.init();
    }
}
