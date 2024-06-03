package com.metacontent.cobblenav.networking;

import com.metacontent.cobblenav.util.PreferencesSaverEntity;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class SavePreferencesReceiver {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        int bucketIndex = buf.readInt();
        int sortingMark = buf.readInt();
        server.execute(() -> {
            if (player instanceof PreferencesSaverEntity preferencesSaver) {
                NbtCompound nbt = preferencesSaver.cobblenav$getSavedPreferences();
                nbt.putInt("bucket_index", bucketIndex);
                nbt.putInt("sorting_mark", sortingMark);
            }
        });
    }
}
