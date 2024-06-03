package com.metacontent.cobblenav.networking.server;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.RenderablePokemon;
import com.metacontent.cobblenav.networking.CobblenavPackets;
import com.metacontent.cobblenav.util.BestPokemonFinder;
import com.metacontent.cobblenav.util.CobblenavNbtHelper;
import com.metacontent.cobblenav.util.FoundPokemon;
import com.metacontent.cobblenav.util.LastFoundPokemonSaverEntity;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Map;

public class TrackedEntityIdRequestReceiver {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        server.execute(() -> {
            if (player instanceof LastFoundPokemonSaverEntity lastFoundPokemonSaver) {
                RenderablePokemon renderablePokemon = CobblenavNbtHelper.getRenderablePokemonByNbtData(lastFoundPokemonSaver.cobblenav$getLastFoundPokemonData());
                if (renderablePokemon != null) {
                    BestPokemonFinder finder = new BestPokemonFinder(player, player.getServerWorld());
                    String name = renderablePokemon.getForm().showdownId();
                    List<PokemonEntity> entities = finder.find(name);
                    Map.Entry<FoundPokemon, Float> entry = BestPokemonFinder.selectBest(entities);
                    PacketByteBuf responseBuf = PacketByteBufs.create();
                    if (entry != null) {
                        FoundPokemon foundPokemon = entry.getKey();
                        responseBuf.writeInt(foundPokemon.getEntityId());
                    }
                    else {
                        responseBuf.writeInt(-1);
                    }
                    responseSender.sendPacket(CobblenavPackets.TRACKED_ENTITY_ID_PACKET, responseBuf);
                }
                else {
                    player.sendMessage(Text.translatable("message.cobblenav.no_saved_pokemon")
                            .setStyle(Style.EMPTY.withItalic(true).withColor(0xff9a38)));
                }
            }
        });
    }
}