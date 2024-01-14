package com.metacontent.cobblenav.mixin;

import com.metacontent.cobblenav.util.ContactSaverEntity;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class ContactSaverEntityMixin implements ContactSaverEntity {
    @Unique
    private NbtCompound contactData;

    @Override
    public NbtCompound cobblenav$getContactData() {
        if (contactData == null) {
            contactData = new NbtCompound();
        }
        return contactData;
    }

    @Inject(method = "writeNbt", at = @At("HEAD"))
    protected void injectWriteMethod(NbtCompound nbt, CallbackInfoReturnable info) {
        if (contactData != null) {
            nbt.put("cobblenav", contactData);
        }
    }

    @Inject(method = "readNbt", at = @At("HEAD"))
    protected void injectReadMethod(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("cobblenav")) {
            contactData = nbt.getCompound("cobblenav");
        }
    }
}
