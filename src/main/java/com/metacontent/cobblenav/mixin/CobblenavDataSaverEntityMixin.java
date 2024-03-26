package com.metacontent.cobblenav.mixin;

import com.metacontent.cobblenav.util.ContactSaverEntity;
import com.metacontent.cobblenav.util.LastFoundPokemonSaverEntity;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class CobblenavDataSaverEntityMixin implements ContactSaverEntity, LastFoundPokemonSaverEntity {
    @Unique
    private NbtCompound contactData;
    @Unique
    private NbtCompound lastFoundPokemonData;

    @Override
    public NbtCompound cobblenav$getLastFoundPokemonData() {
        if (lastFoundPokemonData == null) {
            lastFoundPokemonData = new NbtCompound();
        }
        return lastFoundPokemonData;
    }

    @Override
    public NbtCompound cobblenav$getContactData() {
        if (contactData == null) {
            contactData = new NbtCompound();
        }
        return contactData;
    }

    @Inject(method = "writeNbt", at = @At("HEAD"))
    protected void injectWriteMethod(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> cir) {
        if (lastFoundPokemonData != null) {
            nbt.put("pokenav_last_found_pokemon", lastFoundPokemonData);
        }
        if (contactData != null) {
            nbt.put("pokenav_contacts", contactData);
        }
    }

    @Inject(method = "readNbt", at = @At("HEAD"))
    protected void injectReadMethod(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("pokenav_last_found_pokemon")) {
            lastFoundPokemonData = nbt.getCompound("pokenav_last_found_pokemon");
        }
        if (nbt.contains("pokenav_contacts")) {
            contactData = nbt.getCompound("pokenav_contacts");
        }
    }
}
