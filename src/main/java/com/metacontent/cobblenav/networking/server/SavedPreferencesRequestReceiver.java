package com.metacontent.cobblenav.networking.server;

import com.metacontent.cobblenav.networking.CobblenavPackets;
import com.metacontent.cobblenav.util.PreferencesSaverEntity;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class SavedPreferencesRequestReceiver {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        server.execute(() -> {
            if (player instanceof PreferencesSaverEntity preferencesSaver) {
                NbtCompound nbt = preferencesSaver.cobblenav$getSavedPreferences();
                int bucketIndex = nbt.getInt("bucket_index");
                int sortingMark = nbt.getInt("sorting_mark");
                PacketByteBuf responseBuf = PacketByteBufs.create();
                responseBuf.writeInt(bucketIndex);
                responseBuf.writeInt(sortingMark);
                responseSender.sendPacket(CobblenavPackets.SAVED_PREFERENCES_PACKET, responseBuf);
            }
        });
    }
}
