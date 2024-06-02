package com.metacontent.cobblenav.util;

import net.minecraft.network.PacketByteBuf;

public class ContactTeamMember {
    public final String name;
    public final int level;

    public ContactTeamMember(String name, int level) {
        this.name = name;
        this.level = level;
    }

    public void saveToBuf(PacketByteBuf buf) {
        buf.writeString(name);
        buf.writeInt(level);
    }

    public static ContactTeamMember fromBuf(PacketByteBuf buf) {
        String name = buf.readString();
        int level = buf.readInt();
        return new ContactTeamMember(name, level);
    }

    @Override
    public String toString() {
        return "ContactTeamMember{" +
                "name='" + name + '\'' +
                ", level=" + level +
                '}';
    }
}
