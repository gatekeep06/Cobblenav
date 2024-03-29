package com.metacontent.cobblenav.item;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.RenderablePokemon;
import com.metacontent.cobblenav.networking.CobblenavPackets;
import com.metacontent.cobblenav.util.BestPokemonFinder;
import com.metacontent.cobblenav.util.CobblenavNbtHelper;
import com.metacontent.cobblenav.util.FoundPokemon;
import com.metacontent.cobblenav.util.LastFoundPokemonSaverEntity;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class PokefinderItem extends Item {
    public PokefinderItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        if (!world.isClient()) {
            if (playerEntity instanceof LastFoundPokemonSaverEntity lastFoundPokemonSaver) {
                RenderablePokemon renderablePokemon = CobblenavNbtHelper.getRenderablePokemonByNbtData(lastFoundPokemonSaver.cobblenav$getLastFoundPokemonData());
                if (renderablePokemon != null) {
                    BestPokemonFinder finder = new BestPokemonFinder(playerEntity, (ServerWorld) world);
                    String name = renderablePokemon.getSpecies().showdownId();
                    List<PokemonEntity> entities = finder.find(name);
                    Map.Entry<FoundPokemon, Integer> entry = BestPokemonFinder.selectBest(entities);
                    if (entry != null) {
                        FoundPokemon foundPokemon = entry.getKey();
                        PacketByteBuf buf = PacketByteBufs.create();
                        buf.writeInt(foundPokemon.getEntityId());
                        ServerPlayNetworking.send((ServerPlayerEntity) playerEntity, CobblenavPackets.TRACKED_ENTITY_ID_PACKET, buf);
                    }
                    else {
                        playerEntity.sendMessage(Text.translatable("message.cobblenav.not_found_message")
                                .setStyle(Style.EMPTY.withItalic(true).withColor(0xff9a38)));
                    }
                }
            }
            return TypedActionResult.success(playerEntity.getStackInHand(hand));
        }
        return TypedActionResult.pass(playerEntity.getStackInHand(hand));
    }

    @Override
    public void appendTooltip(ItemStack itemStack, @Nullable World world, List<Text> list, TooltipContext tooltipContext) {
        if (world != null) {
            if (itemStack.getHolder() instanceof LastFoundPokemonSaverEntity lastFoundPokemonSaver) {
                RenderablePokemon renderablePokemon = CobblenavNbtHelper.getRenderablePokemonByNbtData(lastFoundPokemonSaver.cobblenav$getLastFoundPokemonData());
                if (renderablePokemon != null) {
                    MutableText pokemon = renderablePokemon.getSpecies().getTranslatedName();
                    pokemon.append(" (");
                    List<String> formAspects = renderablePokemon.getForm().getAspects();
                    for (int i = 0; i < formAspects.size(); i++) {
                        pokemon.append(formAspects.get(i) + (i == formAspects.size() - 1 ? ")" : ", "));
                    }
                    list.add(pokemon);
                }
            }
        }
    }
}
