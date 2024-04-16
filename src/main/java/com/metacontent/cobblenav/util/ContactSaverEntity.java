package com.metacontent.cobblenav.util;

import net.minecraft.nbt.NbtCompound;

public interface ContactSaverEntity {
    NbtCompound cobblenav$getContactData();
    void cobblenav$clearContacts();
}
