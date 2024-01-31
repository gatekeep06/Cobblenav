package com.metacontent.cobblenav.networking;

import com.cobblemon.mod.common.pokemon.RenderablePokemon;
import com.metacontent.cobblenav.util.CobblenavNbtHelper;
import com.metacontent.cobblenav.util.LastFoundPokemonSaverEntity;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class SaveFoundPokemonPacketReceiver {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        RenderablePokemon pokemon = RenderablePokemon.Companion.loadFromBuffer(buf);
        server.executeSync(() -> {
            if (player instanceof LastFoundPokemonSaverEntity lastFoundPokemonSaver) {
                NbtCompound nbt = lastFoundPokemonSaver.cobblenav$getLastFoundPokemonData();
                CobblenavNbtHelper.saveRenderablePokemonData(pokemon, nbt);
            }
        });
    }
}
