package com.metacontent.cobblenav.networking.server;

import com.cobblemon.mod.common.pokemon.RenderablePokemon;
import com.metacontent.cobblenav.networking.CobblenavPackets;
import com.metacontent.cobblenav.util.CobblenavNbtHelper;
import com.metacontent.cobblenav.util.LastFoundPokemonSaverEntity;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class RenderablePokemonRequestReceiver {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        server.executeSync(() -> {
            if (player instanceof LastFoundPokemonSaverEntity lastFoundPokemonSaver) {
                NbtCompound nbt = lastFoundPokemonSaver.cobblenav$getLastFoundPokemonData();
                RenderablePokemon pokemon = CobblenavNbtHelper.getRenderablePokemonByNbtData(nbt);

                PacketByteBuf responseBuf = PacketByteBufs.create();
                if (pokemon != null) {
                    responseBuf.writeBoolean(true);
                    pokemon.saveToBuffer(responseBuf);
                }
                else {
                    responseBuf.writeBoolean(false);
                }

                responseSender.sendPacket(CobblenavPackets.RENDERABLE_POKEMON_PACKET, responseBuf);
            }
        });
    }
}
